package warehousesystem;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReplenisherBarcodeReaderTest {

  private WarehouseSystem whSystem;
  private ReplenisherBarcodeReader resupplierBr;

  /**
   * Before the test, set up the the resupplier barcode reader to test.
   * 
   * @throws IOException IOException
   */
  @Before
  public void setUp() throws IOException {

    CsvTools csv = new CsvTools();
    FileHandler handler = new FileHandler("testLog.txt");
    handler.setLevel(Level.ALL);
    CustomFormatter formatter = new CustomFormatter();
    handler.setFormatter(formatter);
    whSystem = new WarehouseSystem(csv.getTranslations(), csv.getInventory(), handler, formatter);

    resupplierBr = new ReplenisherBarcodeReader("user", null, whSystem);
  }

  /**
   * After the test, set the whsystem and resupplier barcode reader to null.
   */
  @After
  public void tearDown() {
    whSystem = null;
    resupplierBr = null;
  }

  @Test
  public void testGetUser() throws IOException {
    assertEquals("user", resupplierBr.getUser());
  }

  @Test
  public void testGetSku() {
    String exceptedNum = "0";
    assertEquals(exceptedNum, resupplierBr.getSku());
  }

  @Test
  public void testSetSku() {
    resupplierBr.setSku("5");
    String exceptedNum = "5";
    assertEquals(exceptedNum, resupplierBr.getSku());
  }

  @Test
  public void testScanCorrectSku() throws IOException {
    resupplierBr.setSku("5");
    resupplierBr.getNextStep();
    resupplierBr.scan("5");
    assertEquals(resupplierBr.scannedSku.get(0), "5");
  }

  @Test
  public void testScanWrongSku() throws IOException {
    resupplierBr.setSku("5");
    resupplierBr.getNextStep();
    resupplierBr.scan("6");
    assertEquals(resupplierBr.scannedSku.size(), 0);
  }
}
