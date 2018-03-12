package warehousesystem;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BarcodeReaderFactoryTest {

  private WarehouseSystem whSystem;
  private BarcodeReaderFactory brFactoryTester;

  /**
   * Before the test, set the brFactory with the whSystem.
   * 
   * @throws IOException Add IOException for CsvTools and FileHandler.
   */
  @Before
  public void setUp() throws IOException {
    CsvTools csv = new CsvTools();
    FileHandler handler = new FileHandler("testLog.txt");
    handler.setLevel(Level.ALL);
    CustomFormatter formatter = new CustomFormatter();
    handler.setFormatter(formatter);
    whSystem = new WarehouseSystem(csv.getTranslations(), csv.getInventory(), handler, formatter);
    brFactoryTester = new BarcodeReaderFactory(whSystem);
  }

  /**
   * After the test, set brFactory and whSystem to null.
   */
  @After
  public void tearDown() {
    brFactoryTester = null;
    whSystem = null;
  }

  @Test
  public void testGetPickerBarcodeReader() {
    BarcodeReader br = brFactoryTester.getBarcodeReader("Picker", "a picker's name");
    assertTrue(br instanceof PickerBarcodeReader);
  }

  @Test
  public void testGetSequencerBarcodeReader() {
    BarcodeReader br = brFactoryTester.getBarcodeReader("Sequencer", "a sequencer's name");
    assertTrue(br instanceof SequencerBarcodeReader);
  }

  @Test
  public void testGetLoaderBarcodeReader() {
    BarcodeReader br = brFactoryTester.getBarcodeReader("Loader", "a loader's name");
    assertTrue(br instanceof LoaderBarcodeReader);
  }

  @Test
  public void testGetResuppliererBarcodeReader() {
    BarcodeReader br = brFactoryTester.getBarcodeReader("Replenisher", "a replenisher's name");
    assertTrue(br instanceof ReplenisherBarcodeReader);
  }

  @Test
  public void testGetNullBarcodeReader() {
    BarcodeReader br = brFactoryTester.getBarcodeReader("Worker", "a worker's name");
    assertNull(br);
  }

}
