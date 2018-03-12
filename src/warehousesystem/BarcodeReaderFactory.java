package warehousesystem;

public class BarcodeReaderFactory {

  /** The warehouse system that this BarcodeReaderFactory is in. */
  private WarehouseSystem whSystem;

  /**
   * Initiate a new BarcodeReaderFactory with the warehouse system.
   * 
   * @param whSystem The warehouse system that this BarcodReaderFactory is in.
   */
  public BarcodeReaderFactory(WarehouseSystem whSystem) {
    this.whSystem = whSystem;
  }

  /**
   * Return a BarcodeReader according to the workerType or return null if the workerType do not
   * exist. A PickerBarcodeReader for Picker; a SequencerBarcodeReader for sequencer, a
   * LoaderBarcodeReader for loader, a ReplenisherBarcodeReader for replenisher.
   * 
   * @param workerType The worker's work type for the new BarcodeReader.
   * @param name The name of the worker.
   * @return a BarcodeReader for the worker.
   */
  public BarcodeReader getBarcodeReader(String workerType, String name) {
    if (workerType.equals("Picker")) {
      return new PickerBarcodeReader(name, null, whSystem);
    } else if (workerType.equals("Sequencer")) {
      return new SequencerBarcodeReader(name, null, whSystem);
    } else if (workerType.equals("Loader")) {
      return new LoaderBarcodeReader(name, null, whSystem);
    } else if (workerType.equals("Replenisher")) {
      return new ReplenisherBarcodeReader(name, null, whSystem);
    }
    return null;
  }

}
