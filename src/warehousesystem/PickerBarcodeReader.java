package warehousesystem;

public class PickerBarcodeReader extends BarcodeReader {

  /** The location that this PickerBarcodeReader is going to go next. */
  private String workingLocation;
  /** The sku that this PickerBarcodeReader is going to pick next. */
  private String workingSku;

  /**
   * Initialize a new PickerBarcodeReader.
   * 
   * @param user The user's name of this PickerBarcodeReader.
   * @param pr The picking request for the user of this PickerBarcodeReader to deal with.
   * @param whSystem The warehouse system that this PickerBarcodeReade is in.
   */
  public PickerBarcodeReader(String name, PickingRequest pr, WarehouseSystem whSystem) {
    super(name, pr, whSystem);
  }

  /**
   * Log the location of next fascia to warehouse system. Set the workingLocation and workingSku
   * according to the orders in picking request.
   */
  public void getNextStep() {
    workingLocation = pr.getNextLocation();
    workingSku = pr.getNextSku();
    whSystem.logInfo("SIM: Picker " + user + " directed to " + workingLocation);
  }

  /**
   * Scan the sku number. Add this sku to the scannedSku. If the sku number is different from
   * workingSku, either rescan it, put it back and go to the correct location or continue do
   * something wrong. If there is already eight fascia, send the picker to Marshaling. Otherwise
   * give the picker next location to pick next fascia.
   * 
   * @param sku The sku that the picker scanned.
   */
  public void scan(String sku) {
    scannedSku.add(sku);
    if (scannedSku.size() <= 8) {
      Boolean flag = true;
      // Check if sku number is correct.
      if (!sku.equals(workingSku)) {
        whSystem.logWarning("SIM: Picker " + this.getUser() + " picked incorrect fascia " + sku
            + ", retrying for " + workingSku + " at " + workingLocation);
        flag = false;
      } else {
        whSystem.logInfo("SIM: Picker " + this.getUser() + " picked fascia " + sku);
      }
      // Check if there are eight fascias.
      if (scannedSku.size() == 8) {
        whSystem.logInfo("SIM: Picker " + this.getUser() + " heading to Marshalling");
      } else if (flag) {
        getNextStep();
      }
    } else {
      whSystem.logWarning("SIM: Picker " + this.getUser() + " scanned more than eight fascias");
    }
  }
}
