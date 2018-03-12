package warehousesystem;

public class ReplenisherBarcodeReader extends BarcodeReader {

  /** The sku number that this ReplenisherBarcodeReader is going to replenish. */
  private String replenishSku = "0";

  /**
   * Initialize a new ReplenisherBarcodeReader.
   * 
   * @param user The user's name of this ReplenisherBarcodeReader.
   * @param pr The picking request for the user of this ReplenisherBarcodeReader to deal with.
   * @param whSystem The warehouse system that this ReplenisherBarcodeReade is in.
   */
  public ReplenisherBarcodeReader(String user, PickingRequest pr, WarehouseSystem whSystem) {
    super(user, pr, whSystem);
  }

  /**
   * Return the sku number that the ReplenisherBarcodeReader should replenish.
   * 
   * @return replenishSku
   */
  public String getSku() {
    return replenishSku;
  }

  /**
   * Set the sku number to replenishSku which is the sku number that this replenisher is going to
   * get.
   * 
   * @param sku The sku number that the reokebusger is going to get.
   */
  public void setSku(String sku) {
    replenishSku = sku;
  }

  /**
   * Log the sku for replenisher to replenish to warehouse system.
   */
  @Override
  public void getNextStep() {
    whSystem.logInfo("SIM: Replenisher " + user + " directed to replenish SKU " + replenishSku);
  }

  /**
   * Scan the sku. If there is 25 fascias, check if it has the same sku. If the skus are same,
   * finish it, else discard the fascias and re-replenish the sku.
   * 
   * @param sku The sku that the Replenisher scanned.
   */
  @Override
  public void scan(String sku) {
    if (sku.equals(replenishSku)) {
      scannedSku.add(sku);
      whSystem.logInfo("SIM: Replenisher " + this.getUser() + "'s Scanner reports SKU "
          + replenishSku + " stock successfully replenished");
    } else {
      whSystem.logWarning("SIM: Replenisher " + this.getUser()
          + "'s Scanner reports error when replenishing SKU " + replenishSku);
    }
  }

}
