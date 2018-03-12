package warehousesystem;

import java.util.Map;

public class StorageManager {

  /** This StorageManager's main system. */
  private WarehouseSystem mainSystem;

  /** A mapping of sku to amount for StorageManager. */
  private Map<String, Integer> storages;

  /**
   * Initialize an StorageManager.
   * 
   * @param system the ware house system of the order manager.
   */
  public StorageManager(WarehouseSystem system, Map<String, Integer> storages) {
    this.storages = storages;
    this.mainSystem = system;
    mainSystem.logConfig("SYSTEM: StorageManager initialized");
  }

  /**
   * Pick the fascia from storages. Minus one from the amount and check if the amount is less than
   * 5. If it is, then request for a re-supply.
   * 
   * @param sku The sku number that the picker picked.
   */
  public void pickFascia(String sku) {
    storages.put(sku, storages.get(sku) - 1);
    mainSystem.logInfo("SIM: StorageManager detected one item " + sku + " removed from storage. " + storages.get(sku) + " remaining.");
    if (storages.get(sku) <= 5) {
      mainSystem.logInfo("SIM: StorageManager detected Storage SKU " + sku + " low on stock, requesting replenishment");
      mainSystem.requestResupply(sku);
    }
  }

  /**
   * Put the fasica back when the picker rescan the sku.
   * 
   * @param sku The sku number that the picker return.
   */
  public void putFasciaBack(String sku) {
    mainSystem.logInfo("SIM: StorageManager detected one item " + sku + " returned to storage "
        + storages.get(sku));
    storages.put(sku, storages.get(sku) + 1);
  }

  /**
   * Return true if and only if the storage level is empty.
   * 
   * @param sku The sku that need to be checked.
   * @return true iff the storage level is empty.
   */
  public Boolean levelEmpty(String sku) {
    return storages.get(sku).equals(0);
  }

  /**
   * Replenish the level with 25 more fascias.
   * 
   * @param sku The sku number that need to be replenished.
   */
  public void replenish(String sku) {
    storages.put(sku, storages.get(sku) + 25);
    mainSystem.logInfo(
        "SIM: StorageManager detected Storage " + storages.get(sku) + " has been replenished");
  }

  protected Map<String, Integer> getStorages() {
    return storages;
  }

}
