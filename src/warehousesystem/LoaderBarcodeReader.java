package warehousesystem;

public class LoaderBarcodeReader extends BarcodeReader {

  /**
   * Initialize a new LoaderBarcodeReader.
   * 
   * @param user The user's name of this LoaderBarcodeReader.
   * @param pr The picking request for the user of this LoaderBarcodeReader to deal with.
   * @param whSystem The warehouse system that this LoaderBarcodeReade is in.
   */
  public LoaderBarcodeReader(String user, PickingRequest pr, WarehouseSystem whSystem) {
    super(user, pr, whSystem);
  }

  /**
   * Log the correct order for loader to check in the warehouse system.
   */
  public void getNextStep() {
    whSystem.logInfo("SIM: Loader " + user + "'s correct order is: " + pr.getCorrectOrder());
  }

  /**
   * Scan the sku. Add the sku to the scannedSku. If there is eight fascias, check if it has the
   * correct order as the correctOrder from the picking request. If the order is correct, finish the
   * work, else rescan the eight fasicas or discard the fascias and re-pick the picking request.
   * 
   * @param sku The sku that the loader scanned.
   */
  public void scan(String sku) {
    scannedSku.add(sku);
    if (scannedSku.size() <= 8) {
      whSystem.logInfo("SIM: Loader " + this.getUser() + " loaded item " + sku);

      if (scannedSku.size() == 8) {
        if (scannedSku.equals(pr.getCorrectOrder())) {
          whSystem.logInfo("SIM: Loader " + this.getUser() + "'s Scanner confirms request "
              + pr.getId() + " completed loading");
        } else {
          whSystem.logWarning("SIM: Loader " + this.getUser()
              + "'s Scanner reports error while loading items for request "
              + pr.getId());
        }
      }
    } else {
      whSystem.logWarning("SIM: Loader " + this.getUser() + " scanned more than eight fascias");
    }
  }
}
