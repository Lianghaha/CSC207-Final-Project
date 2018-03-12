package warehousesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StorageManagerTest {

  private WarehouseSystem whSysem;
  private StorageManager storageManagerTester;

  /**
   * Before the test, set up new CsvTools, WarehouseSystem and StorageManager.
   * 
   * @throws IOException For CsvTools and FileHandler.
   */
  @Before
  public void setUp() throws IOException {
    CsvTools csv = new CsvTools();
    FileHandler handler = new FileHandler("testLog.txt");
    handler.setLevel(Level.ALL);
    CustomFormatter formatter = new CustomFormatter();
    handler.setFormatter(formatter);
    whSysem = new WarehouseSystem(csv.getTranslations(), csv.getInventory(), handler, formatter);
    storageManagerTester = new StorageManager(whSysem, csv.getInventory());
  }

  /**
   * After the test, set CsvTools, WarehouseSystem and StorageManager to null.
   * 
   */
  @After
  public void tearDown() {
    whSysem = null;
    storageManagerTester = null;
  }

  @Test
  public void testConstructor() {
    assertTrue(storageManagerTester instanceof StorageManager);
  }

  @Test
  public void testPickFascia() throws IOException {
    storageManagerTester.getStorages().put("1", 5);
    storageManagerTester.pickFascia("1");
    assertEquals(Integer.valueOf(4), storageManagerTester.getStorages().get("1"));
  }

  @Test
  public void testReplenish() throws IOException {
    storageManagerTester.getStorages().put("1", 5);
    storageManagerTester.replenish("1");
    assertEquals(Integer.valueOf(30), storageManagerTester.getStorages().get("1"));
  }

  @Test
  public void testLevelEmpty() {
    storageManagerTester.getStorages().put("1", 5);
    assertFalse(storageManagerTester.levelEmpty("1"));
  }

  @Test
  public void testPutFasciaBack() {
    storageManagerTester.getStorages().put("1", 5);
    storageManagerTester.putFasciaBack("1");
    assertEquals(Integer.valueOf(6), storageManagerTester.getStorages().get("1"));
  }

}
