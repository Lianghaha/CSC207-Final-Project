package warehousesystem;

import java.io.IOException;
import java.util.ArrayList;

public class PickingRequest {

  /** The id of this PickingRequest. */
  private int id;
  /**
   * Record the number of PickingRequest created. It is static since it keep track of the number of
   * picking request when new PickingRequest is created.
   */
  private static int PRId = 1;

  /**
   * The status of this picking request. This can only be one of "Waiting", "Picking", "Picked",
   * "Sequencing", "Sequenced", "Loading", "Loaded", "Finish".
   */
  private String status = "Waiting";

  /** The four orders in the PickingRequest. */
  private ArrayList<Order> orders = new ArrayList<>(4);
  /** The correct order of eight fascias to be sequenced and loaded in this PickingRequest. */
  private ArrayList<String> correctOrder = new ArrayList<>(8);
  /** The picking order of eight fascias in this PickingRequest. */
  private ArrayList<String> pickingOrder = new ArrayList<>(8);
  /** The location of eight fascias in this PickingRequest. */
  private ArrayList<String> location = new ArrayList<>(8);

  /**
   * Initialize a new PickingRequest with four orders.
   * 
   * @param orders The ArrayList of four orders to create a new picking request.
   * @throws IOException for findFasciaLocation.
   */
  public PickingRequest(ArrayList<Order> orders) throws IOException {
    id = PRId;
    PRId++;
    this.orders = orders;
    setStatus(status);
    findFasciaLocation();
    findCorrectOrder();
  }

  /**
   * Return the ArrayList of eight fascias' location.
   *
   * @throws IOException for WarehousePicking optimize.
   */
  public void findFasciaLocation() throws IOException {
    ArrayList<String> skuNumbers = new ArrayList<>();
    for (Order o : orders) {
      skuNumbers.add(o.getFrontsku());
      skuNumbers.add(o.getBacksku());
    }
    ArrayList<String> result = (ArrayList<String>) WarehousePicking.optimize(skuNumbers);
    for (String str : result) {
      String sku = str.substring(8);
      pickingOrder.add(sku);
      location.add(str.substring(0, 7));
    }
  }

  /**
   * Return the correct order of sku numbers in this picking request for sequencer to sequencing and
   * loader to check.
   * 
   * @return ArrayList of String with eight correct order of sku numbers.
   */
  public ArrayList<String> findCorrectOrder() {
    ArrayList<String> skuNumbers = new ArrayList<>();
    for (Order o : orders) {
      skuNumbers.add(o.getFrontsku());
    }
    for (Order o : orders) {
      skuNumbers.add(o.getBacksku());
    }
    correctOrder = skuNumbers;
    return skuNumbers;
  }

  /**
   * Return the status of the picking request.
   * 
   * @return status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Set the status of the picking request to a new status.
   * 
   * @param status The new status of this picking request.
   */
  public void setStatus(String status) {
    this.status = status;
    for (Order order : orders) {
      order.setStatus(status);
    }
  }

  /**
   * Return the correct order of the picking request.
   * 
   * @return correctOrder
   */
  public ArrayList<String> getCorrectOrder() {
    return correctOrder;
  }

  /**
   * Return next fascia's sku number while picker is picking this picking request.
   * 
   * @return The String of next fascia's sku.
   */
  public String getNextSku() {
    return pickingOrder.remove(0);
  }

  /**
   * Return the location of the next fascia.
   * 
   * @return The String of next fascia's location.
   */
  public String getNextLocation() {
    return location.remove(0);
  }

  /**
   * Return the id of the picking request.
   * 
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * Return the ArrayList of four orders in this picking request.
   * 
   * @return orders
   */
  public ArrayList<Order> getOrders() {
    return orders;
  }

  /**
   * Return the static PRID for PickingRequest.
   * 
   * @return PRID
   */
  public int getStaticPrId() {
    return PRId;
  }

}
