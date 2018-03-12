package warehousesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WarehouseSystem {

  /** The order manager of this WarehouseSystem. */
  private OrderManager orderManager;
  /** The storage manager of this WarehouseSystem. */
  private StorageManager storageManager;
  /** The BarcodeReader factory for this WarehouseSystem. */
  private BarcodeReaderFactory brFactory;
  /** The translations map of color and model of van to the sku numbers of van. */
  private HashMap<String, String[]> translations;
  /** The list of picking requests in the system. */
  private ArrayList<PickingRequest> prList = new ArrayList<>();
  /** The list of picking requests need to be processed in the system. */
  private ArrayList<PickingRequest> pendingPrList = new ArrayList<>();
  /** The list of barcode readers in the system. */
  private ArrayList<BarcodeReader> brList = new ArrayList<>();
  /** The list of barcode readers need to be processed in the system. */
  private ArrayList<BarcodeReader> pendingBrList = new ArrayList<>();
  /** The list of replenish request need to be processed in the system. */
  private ArrayList<String> replenishRequest = new ArrayList<>();
  /** The list of picking request that is finished in the system. */
  private ArrayList<PickingRequest> completedPrList = new ArrayList<>();
  /** The list of all workers' names in the system. Assume that each worker has a unique name. */
  private ArrayList<String> workerNames = new ArrayList<>();
  /** The logger of this system. */
  private static final Logger logger = Logger.getLogger("Warehouse System Logger");
  /** The console handler of this system. */
  private ConsoleHandler consoleHandler;

  /**
   * Initialize a new WareHouseSystem with the translations map, the inventory map, the file handler
   * and the custom formatter.
   * 
   * @param translations The translations map for warehouse system.
   * @param inventory The initial inventory for warehouse system.
   * @param handler The file handler for warehouse system.
   * @param formatter The custom formatter for warehouse system.
   */
  public WarehouseSystem(HashMap<String, String[]> translations, HashMap<String, Integer> inventory,
      FileHandler handler, CustomFormatter formatter) {
    orderManager = new OrderManager(this);
    storageManager = new StorageManager(this, inventory);
    brFactory = new BarcodeReaderFactory(this);
    this.translations = translations;

    logger.setLevel(Level.ALL);
    logger.setUseParentHandlers(false);
    consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.ALL);
    consoleHandler.setFormatter(formatter);
    logger.addHandler(consoleHandler);
    logger.addHandler(handler);
    logger.config("SYSTEM: Logging System Initialized");
  }

  /**
   * Update orderManager by receiving one new Order.
   * 
   * @param color the color of the van from the order.
   * @param model the model of the van from the order.
   * @throws IOException for receiveOrder
   */
  public void receiveOrder(String color, String model) throws IOException {
    String[] skuNumbers = translations.get(color + model);
    orderManager.receiveOrder(color, model, skuNumbers[0], skuNumbers[1]);
    this.logInfo("EVENT: Order Received - " + color + ", " + model);
  }

  /**
   * Process the picking request. If there is someone available to do the next step of the picking
   * request, give this picking request to the worker. Otherwise, put the picking request to pending
   * picking request list and put the re-pick picking request at the very front of the pending
   * picking request list. Finally, put the picking request to prList in the system if system do not
   * have this picking request.
   * 
   * @param pr The picking request that need to be processed.
   */
  public void processPr(PickingRequest pr) {
    // Check if there is someone available for this picking request <pr>.
    String status = pr.getStatus();
    if (status.equals("Waiting")) {
      for (BarcodeReader br : pendingBrList) {
        if (br instanceof PickerBarcodeReader) {
          br.setPickingRequest(pr);
          pendingBrList.remove(br);
          pr.setStatus("Picking");
          this.logInfo("SIM: Picker " + br.getUser() + " picking request " + pr.getId());
          break;
        }
      }
    } else if (status.equals("Picked")) {
      for (BarcodeReader br : pendingBrList) {
        if (br instanceof SequencerBarcodeReader) {
          br.setPickingRequest(pr);
          pendingBrList.remove(br);
          pr.setStatus("Sequencing");
          this.logInfo("SIM: Sequencer " + br.getUser() + " sequencing request " + pr.getId());
          break;
        }
      }
    } else if (status.equals("Sequenced")) {
      for (BarcodeReader br : pendingBrList) {
        if (br instanceof LoaderBarcodeReader & isNextPRtoLoad(pr)) {
          br.setPickingRequest(pr);
          pendingBrList.remove(br);
          pr.setStatus("Loading");
          this.logInfo("SIM: Loader " + br.getUser() + " loading request " + pr.getId());
          break;
        }
      }
    } else if (status.equals("Loaded")) {
      this.logInfo("SIM: Request " + pr.getId() + " Completed");
      pr.setStatus("Finished");
    }
    // If the picking request is already in the prList and is still waiting, put this picking
    // request to the front of the pendingPRList so that it will be picked first.
    if (prList.contains(pr) & status.equals("Waiting")) {
      pendingPrList.add(0, pr);
    } else if (status == pr.getStatus()) {
      // If the picking request <pr> is not assign to anyone, then put it in the pendingPRList.
      pendingPrList.add(pr);
    }

    // Add the picking request <pr> to the prList.
    if (!prList.contains(pr)) {
      prList.add(pr);
    }
  }

  /**
   * When the worker is ready, create a new BarcodeReader and process it for the worker if the
   * worker is not in the system. If the worker is already in the system, just give a picking
   * request to the worker by process the BarcodeReader.
   * 
   * @param workerType The worker type of the worker used to create a new BarcodeReader.
   * @param name The name of the worker. It should not equals to any existing name in workerNames.
   */
  public void workerReady(String workerType, String name) {
    // Check if the worker is already exist, if exist, process this BarcodeReader.
    if (workerNames.contains(name)) {
      BarcodeReader br = brList.get(workerNames.indexOf(name));
      if (br instanceof ReplenisherBarcodeReader) {
        // Check if the replenisher has work or not.
        if ((((ReplenisherBarcodeReader) br)).getSku().equals("0")) {
          this.logInfo("EVENT: New " + workerType + " " + name + " starting shift");
          processBr(br);
        } else {
          this.logWarning(
              "SIM: " + workerType + " " + name + " has not completed current assigned work");
        }
      } else {
        // Check if the worker has work or not.
        if (br.getPickingRequest() == null) {
          this.logInfo("EVENT: New " + workerType + " " + name + " starting shift");
          processBr(br);
        } else {
          this.logWarning(
              "SIM: " + workerType + " " + name + " has not completed current assigned work");
        }
      }
    } else {
      // If the worker do not exist, create a new br and process it.
      BarcodeReader br = brFactory.getBarcodeReader(workerType, name);
      // Check if the workerType is valid.
      if (br != null) {
        brList.add(br);
        workerNames.add(name);
        this.logInfo("EVENT: New " + workerType + " " + name + " starting shift");
        processBr(br);
      } else {
        this.logWarning("EVENT: " + workerType + " " + name + " is not valid");
      }
    }
  }

  /**
   * Process the BarcodeReader. If there is some picking request or replenish request available for
   * the correct kind of BarcodeReader, give it to the worker. Otherwise, put the BarcodeReader to
   * pending BarcodeReader list.
   * 
   * @param br The BarcodeReader that need to be processed.
   */
  public void processBr(BarcodeReader br) {
    // Check if there is some picking request available for this worker with br as BarcodeReader.
    if (br instanceof PickerBarcodeReader) {
      for (PickingRequest pr : pendingPrList) {
        if (pr.getStatus().equals("Waiting")) {
          this.logInfo("SIM: System sending request " + pr.getId() + " to Picker " + br.getUser()
              + "'s Barcode Scanner");
          br.setPickingRequest(pr);
          pendingPrList.remove(pr);
          pr.setStatus("Picking");
          break;
        }
      }
    } else if (br instanceof SequencerBarcodeReader) {
      for (PickingRequest pr : pendingPrList) {
        if (pr.getStatus().equals("Picked")) {
          this.logInfo("SIM: System sending request " + pr.getId() + " to Sequencer " + br.getUser()
              + "'s Barcode Scanner");
          br.setPickingRequest(pr);
          pendingPrList.remove(pr);
          pr.setStatus("Sequencing");
          break;
        }
      }
    } else if (br instanceof LoaderBarcodeReader) {
      for (PickingRequest pr : pendingPrList) {
        if (pr.getStatus().equals("Sequenced")) {
          this.logInfo("SIM: System sending request " + pr.getId() + " to Loader " + br.getUser()
              + "'s Barcode Scanner");
          br.setPickingRequest(pr);
          pendingPrList.remove(pr);
          pr.setStatus("Loading");
          break;
        }
      }
    } else if (br instanceof ReplenisherBarcodeReader) {
      if (replenishRequest.size() > 0) {
        ((ReplenisherBarcodeReader) br).setSku(replenishRequest.remove(0));
        this.logInfo(
            "SIM: System sending replenish request to " + br.getUser() + "'s Barcode Scanner");
        ((ReplenisherBarcodeReader) br).getNextStep();
      } else {
        pendingBrList.add(br);
        this.logInfo("SIM: System sending wait status request to replenisher " + br.getUser()
            + "'s Barcode Scanner");
      }
    }

    // If the worker with BarcodeReader do not find any request, then put it in the pendingBRList.
    if (!(br instanceof ReplenisherBarcodeReader) & br.getPickingRequest() == null) {
      pendingBrList.add(br);
      this.logInfo("SIM: System sending wait status request to Worker " + br.getUser()
          + "'s Barcode Scanner");
    }
  }

  /**
   * Scan the sku number by the worker. If the picker scan an SKU, remember to minus one in the
   * storage and check if there is less than five fascia exist.
   * 
   * @param workerType The worker type of the worker.
   * @param name The name of the worker. It should not equals to any existing name in workerNames.
   * @param sku The sku number scan by the worker.
   */
  public void scan(String workerType, String name, String sku) {
    BarcodeReader br = brList.get(workerNames.indexOf(name));
    br.scan(sku);
    if (workerType.equals("Picker")) {
      storageManager.pickFascia(sku);
      if (storageManager.levelEmpty(sku)) {
        this.logWarning("SIM: System sending wait request to " + workerType + " " + br.getUser()
            + "'s Barcode Scanner since there are no available fascias in level.");
      }
    }
  }

  /**
   * Rescan the sku number by the worker. If the worker is a picker, put the rescanned sku back to
   * level. If the worker is replenisher, replenish the sku again and check if it is a correct sku.
   * Otherwise, the worker checks if the wrong order is due to some human mistakes.
   * 
   * @param workerType The worker type of the worker.
   * @param name The name of the worker. It should not equals to any existing name in workerNames.
   * @param sku The sku number scan by the worker.
   */
  public void rescan(String workerType, String name, String sku) {
    BarcodeReader br = brList.get(workerNames.indexOf(name));
    if (br instanceof PickerBarcodeReader) {
      this.logWarning("SIM: " + workerType + " " + name + " put " + sku + " back");
      storageManager.putFasciaBack(sku);
      br.scannedSku.remove(br.scannedSku.size() - 1);
    } else if (br instanceof ReplenisherBarcodeReader) {
      this.logWarning("SIM: " + workerType + " " + name + " replenishs " + sku + " again");
      br.scan(sku);
    } else {
      br.scannedSku = new ArrayList<>();
      this.logWarning("SIM: " + workerType + " " + name + " rescans picking request"
          + br.getPickingRequest().getId());
    }
  }

  /**
   * Discard the eight fascias and re-pick the eight fascias by the free picker.
   * 
   * @param workerType The worker type of the worker.
   * @param name The name of the worker. It should not equals to any existing name in workerNames.
   * @throws IOException for findFasciaLocation
   */
  public void discard(String workerType, String name) throws IOException {
    BarcodeReader br = brList.get(workerNames.indexOf(name));
    PickingRequest pr = br.getPickingRequest();
    this.logWarning("SIM: " + workerType + " " + name + " discards picking request" + pr.getId());
    pr.setStatus("Waiting");
    pr.findFasciaLocation();
    processPr(pr);
  }

  /**
   * The worker finished the work. Change the status of the picking request and the picking request
   * for the worker to null.
   * 
   * @param workerType The worker type of the worker.
   * @param name The name of the worker. It should not equals to any existing name in workerNames.
   * @throws IOException for replenishAmount and updateOrder
   */
  public void workerFinished(String workerType, String name) throws IOException {
    BarcodeReader br = brList.get(workerNames.indexOf(name));

    if (workerType.equals("Replenisher")) {
      String sku = ((ReplenisherBarcodeReader) br).getSku();
      if (br.scannedSku.size() == 1) {
        replenishAmount(sku);
        this.logInfo("SIM: System confirms SKU " + sku + " replenished by " + name);
      } else {
        this.logWarning("SIM: System rejects SKU " + sku + " replenished by " + name);
        replenishRequest.add(sku);
      }
      if (replenishRequest.size() > 0) {
        ((ReplenisherBarcodeReader) br).setSku(replenishRequest.remove(0));
      } else {
        ((ReplenisherBarcodeReader) br).setSku("0");
      }
    } else {
      PickingRequest pr = br.getPickingRequest();
      if (pr.getStatus().equals("Waiting")) {
        this.logInfo("SIM: System setting picking request " + pr.getId() + " to hold");
      } else if (workerType.equals("Picker")) {
        pr.setStatus("Picked");
        this.logInfo(
            "SIM: System confirms Picker " + name + " has finished picking request " + pr.getId());
      } else if (workerType.equals("Sequencer")) {
        pr.setStatus("Sequenced");
        this.logInfo("SIM: System confirms Sequencer " + name + " has finished sequencing request "
            + pr.getId());
      } else if (workerType.equals("Loader")) {
        pr.setStatus("Loaded");
        this.logInfo(
            "SIM: System confirms Loader " + name + " has finished loading request " + pr.getId());
        updateOrder(pr);
      }
      processPr(pr);
    }

    br.setPickingRequest(null);
  }

  /**
   * Return true if and only if the picking request picking request is the one that need to be
   * loaded next to make sure that the picking requests are in correct order.
   * 
   * @param pr The picking request that need to be checked.
   * @return true iff picking request is the one that need to be loaded next.
   */
  public boolean isNextPRtoLoad(PickingRequest pr) {
    PickingRequest pickingRequest = prList.get(0);
    int index = 0;
    System.out.println(pickingRequest.getStatus());
    while (pickingRequest.getStatus().equals("Loaded")) {
      index += 1;
      pickingRequest = prList.get(index);
    }
    return prList.get(index) == pr;
  }

  /**
   * Request resupplier to re-supply 25 fasicas with sku number.
   * 
   * @param sku The sku number that is going to re-supply.
   */
  public void requestResupply(String sku) {
    boolean replenisherExist = false;
    for (BarcodeReader br : brList) {
      if (br instanceof ReplenisherBarcodeReader) {
        if (((ReplenisherBarcodeReader) br).getSku().equals("0")) {
          replenisherExist = true;
          ((ReplenisherBarcodeReader) br).setSku(sku);
          ((ReplenisherBarcodeReader) br).getNextStep();
          break;
        }
      }
    }
    if (!(replenisherExist & replenishRequest.contains(sku))) {
      replenishRequest.add(sku);
    }
  }

  /**
   * Replenish the fascias with sku number.
   * 
   * @param sku The sku number that the replenisher is going to replenish.
   */
  public void replenishAmount(String sku) {
    storageManager.replenish(sku);
  }

  /**
   * If loader finished work, update the order.csv file with the picking request load by the loader.
   * 
   * @param pr The picking request that the loader loads.
   * @throws IOException The IOException
   */
  public void updateOrder(PickingRequest pr) throws IOException {
    completedPrList.add(pr);
  }

  /**
   * Allows classes to access the system and log system information by info.
   * 
   * @param log the message that the logger should record
   */
  public void logInfo(String log) {
    logger.info(log);
  }

  /**
   * Allows classes to access the system and log system information by config.
   * 
   * @param log the message that the logger should record
   */
  public void logConfig(String log) {
    logger.config(log);
  }

  /**
   * Allows classes to access the system and log system information by warning.
   * 
   * @param log the message that the logger should record
   */
  public void logWarning(String log) {
    logger.warning(log);
  }

  /**
   * Return the prList in the system after all the event input.
   * 
   * @return prList
   */
  public ArrayList<PickingRequest> getPrList() {
    return prList;
  }

  /**
   * Return the storages from storageManager.
   * 
   * @return the storages from storageManager.
   */
  public Map<String, Integer> outputInventory() {
    return storageManager.getStorages();
  }

  /**
   * Return the compeletedPrList in the system after all the event input.
   * 
   * @return completedPrList
   */
  public ArrayList<PickingRequest> outputOrders() {
    return completedPrList;
  }

  /**
   * Set the orderManager for the system. This method is create for test.
   * 
   * @param orderManager The new storageManager of system.
   */
  public void setOrderManager(OrderManager orderManager) {
    this.orderManager = orderManager;
  }

  /**
   * Set the storageManager for the system. This method is create for test.
   * 
   * @param storageManager The new storageManager of system.
   */
  public void setStorageManager(StorageManager storageManager) {
    this.storageManager = storageManager;
  }

  /**
   * Return the pendingPrList processed in the system. This method is create for test.
   * 
   * @return pendingPrList
   */

  public ArrayList<PickingRequest> getPendingPrList() {
    return pendingPrList;
  }


}
