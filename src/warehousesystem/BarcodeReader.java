package warehousesystem;

import java.util.ArrayList;

public abstract class BarcodeReader {

  /** The the user's name of this BarcodeReader. */
  protected String user;
  /** The picking request for the user of this BarcodeReader to deal with. */
  protected PickingRequest pr;
  /** The ArrayList of the skus' that this BarcodeReader has already scanned. */
  protected ArrayList<String> scannedSku = new ArrayList<>();
  /** The Warehouse System that this BarcodeReader is in. */
  protected WarehouseSystem whSystem;

  /**
   * Initialize a new BarcodeReader with the user's name and the picking request that the user
   * needed to deal with.
   * 
   * @param user The user's name of this BarcodeReader.
   * @param pr The picking request for the user of this BarcodeReader to deal with.
   * @param whSystem The warehouse system that this BarcodeReade is in.
   */
  public BarcodeReader(String user, PickingRequest pr, WarehouseSystem whSystem) {
    this.user = user;
    this.pr = pr;
    this.whSystem = whSystem;
  }

  /**
   * Return the user's name of this BarcodeReader.
   * 
   * @return user
   */
  public String getUser() {
    return user;
  }

  /**
   * Return The picking request for the user of this BarcodeReader to deal with.
   * 
   * @return pr
   */
  public PickingRequest getPickingRequest() {
    return pr;
  }

  /**
   * Set a new picking request for the picker. If the new picking request is not null, call
   * getNextStep method for user who use this BarcodeReader. If the new picking request is null,
   * clear the scannedSku to an empty ArrayList.
   * 
   * @param pickingRequest The picking request that the user is going to deal with.
   */
  public void setPickingRequest(PickingRequest pickingRequest) {
    pr = pickingRequest;
    if (pr != null) {
      getNextStep();
    } else {
      scannedSku.clear();
    }
  }

  /** Scan the sku number. */
  public abstract void scan(String sku);

  /** Get the next step for the user. */
  public abstract void getNextStep();

}
