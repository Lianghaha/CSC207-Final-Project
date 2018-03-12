package warehousesystem;

public class SequencerBarcodeReader extends BarcodeReader {

  /**
   * Initialize a new SequencerBarcodeReader.
   * 
   * @param user The user's name of this SequencerBarcodeReader.
   * @param pr The picking request for the user of this SequencerBarcodeReader to deal with.
   * @param whSystem The warehouse system that this SequencerBarcodeReade is in.
   */
  public SequencerBarcodeReader(String user, PickingRequest pr, WarehouseSystem whSystem) {
    super(user, pr, whSystem);
  }

  /**
   * Log the correct order for sequencer to sequence in warehouse system.
   */
  public void getNextStep() {
    whSystem.logInfo("SIM: Sequencer " + user + "'s correct order is: " + pr.getCorrectOrder());
  }

  /**
   * Scan the sku. Add the sku to the scannedSku. If there is eight fascias, check if it has the
   * correct order as the correctOrder from the picking request. If the order is correct, finish the
   * work, else rescan the eight fasicas or discard the fascias and re-pick the picking request.
   * 
   * @param sku The sku that the sequencer scanned.
   */
  public void scan(String sku) {
    scannedSku.add(sku);
    if (scannedSku.size() <= 8) {
      if (sku.equals(pr.getCorrectOrder().get(scannedSku.size() - 1))) {
        whSystem
            .logInfo("SIM: Sequencer " + this.getUser() + "'s Scanner confirms sequence " + sku);
      } else {
        whSystem.logWarning("SIM: Sequencer " + this.getUser() + "'s Scanner reports error sequencing "
            + sku);
        whSystem.logWarning("SIM: Sequencer " + this.getUser() + " requesting to discard items for request " + pr.getId());
      }
      if (scannedSku.size() == 8) {
        whSystem.logInfo(
            "SIM: Sequencer " + this.getUser() + "'s Scanner already sequenced eight fascias");
      }
    } else {
      whSystem.logWarning("SIM: Sequencer " + this.getUser() + " scanned more than eight fascias");
    }
  }
}
