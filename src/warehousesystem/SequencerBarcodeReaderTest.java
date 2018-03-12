package warehousesystem;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SequencerBarcodeReaderTest {

  private PickingRequest emptyPr;
  private PickingRequest fourOrderPr;
  private WarehouseSystem whSystem;
  private SequencerBarcodeReader sequencerBr;

  /**
   * Before the test, set up the empty picking request, a new picking request that contains four
   * orders, and the sequencer barcode reader to test.
   * 
   * @throws IOException IOException
   */
  @Before
  public void setUp() throws IOException {
    Order o1 = new Order("Blue", "SES", "37", "38");
    Order o2 = new Order("Beige", "S", "9", "10");
    Order o3 = new Order("Red", "SES", "21", "22");
    Order o4 = new Order("White", "SE", "3", "4");
    ArrayList<Order> orders = new ArrayList<>();
    orders.add(o1);
    orders.add(o2);
    orders.add(o3);
    orders.add(o4);

    emptyPr = new PickingRequest(new ArrayList<Order>());
    fourOrderPr = new PickingRequest(orders);

    CsvTools csv = new CsvTools();
    FileHandler handler = new FileHandler("testLog.txt");
    handler.setLevel(Level.ALL);
    CustomFormatter formatter = new CustomFormatter();
    handler.setFormatter(formatter);
    whSystem = new WarehouseSystem(csv.getTranslations(), csv.getInventory(), handler, formatter);

    sequencerBr = new SequencerBarcodeReader("user", emptyPr, whSystem);
  }

  /**
   * After the test, set the empty picking request, the four order picking request and the sequencer
   * barcode reader to null.
   */
  @After
  public void tearDown() {
    emptyPr = null;
    fourOrderPr = null;
    sequencerBr = null;
  }

  @Test
  public void testGetUser() {
    assertEquals("user", sequencerBr.getUser());
  }

  @Test
  public void testGetPickingRequest() {
    assertEquals(emptyPr, sequencerBr.getPickingRequest());
  }

  @Test
  public void testSetNullPickingRequest() {
    sequencerBr.setPickingRequest(null);
    assertEquals(0, sequencerBr.scannedSku.size());
  }

  @Test
  public void testSetPickingRequest() {
    sequencerBr.setPickingRequest(fourOrderPr);
    assertEquals(fourOrderPr, sequencerBr.getPickingRequest());
  }

  @Test
  public void testScanLessFascias() {
    sequencerBr.setPickingRequest(fourOrderPr);
    sequencerBr.getNextStep();
    sequencerBr.scan("37");
    sequencerBr.scan("9");
    sequencerBr.scan("21");
    sequencerBr.scan("3");
    assertEquals(4, sequencerBr.scannedSku.size());
  }

  @Test
  public void testScanCorrectOrder() {
    sequencerBr.setPickingRequest(fourOrderPr);
    sequencerBr.getNextStep();
    sequencerBr.scan("37");
    sequencerBr.scan("9");
    sequencerBr.scan("21");
    sequencerBr.scan("3");
    sequencerBr.scan("38");
    sequencerBr.scan("10");
    sequencerBr.scan("22");
    sequencerBr.scan("4");
    assertEquals(8, sequencerBr.scannedSku.size());
    assertEquals(sequencerBr.scannedSku.toString(), fourOrderPr.getCorrectOrder().toString());
  }

  @Test
  public void testScanWrongOrder() {
    sequencerBr.setPickingRequest(fourOrderPr);
    sequencerBr.getNextStep();
    sequencerBr.scan("38");
    sequencerBr.scan("9");
    sequencerBr.scan("21");
    sequencerBr.scan("3");
    sequencerBr.scan("37");
    sequencerBr.scan("10");
    sequencerBr.scan("22");
    sequencerBr.scan("4");
    assertEquals(8, sequencerBr.scannedSku.size());
  }

  @Test
  public void testScanMoreFascias() {
    sequencerBr.setPickingRequest(fourOrderPr);
    sequencerBr.getNextStep();
    sequencerBr.scan("38");
    sequencerBr.scan("9");
    sequencerBr.scan("21");
    sequencerBr.scan("3");
    sequencerBr.scan("37");
    sequencerBr.scan("10");
    sequencerBr.scan("22");
    sequencerBr.scan("4");
    sequencerBr.scan("4");
    assertEquals(9, sequencerBr.scannedSku.size());
  }
}
