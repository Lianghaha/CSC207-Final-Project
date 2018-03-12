package warehousesystem;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PickerBarcodeReaderTest {

  private PickingRequest emptyPr;
  private PickingRequest fourOrderPr;
  private WarehouseSystem whSystem;
  private PickerBarcodeReader pickerBr;

  /**
   * Before the test, set up the empty picking request, a new picking request that contains four
   * orders, and the picker barcode reader to test.
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

    pickerBr = new PickerBarcodeReader("user", emptyPr, whSystem);
  }

  /**
   * After the test, set the empty picking request, the four order picking request and the picker
   * barcode reader to null.
   */
  @After
  public void tearDown() {
    emptyPr = null;
    fourOrderPr = null;
    pickerBr = null;
  }

  @Test
  public void testGetUser() throws IOException {
    assertEquals("user", pickerBr.getUser());
  }

  @Test
  public void testGetPickingRequest() throws IOException {
    assertEquals(emptyPr, pickerBr.getPickingRequest());
  }

  @Test
  public void testSetNullPickingRequest() throws IOException {
    pickerBr.setPickingRequest(null);
    assertEquals(0, pickerBr.scannedSku.size());
  }

  @Test
  public void testSetPickingRequest() throws IOException {
    pickerBr.setPickingRequest(fourOrderPr);
    assertEquals(fourOrderPr, pickerBr.getPickingRequest());
  }

  @Test
  public void testScanLessFascias() {
    pickerBr.setPickingRequest(fourOrderPr);
    pickerBr.scan("10");
    pickerBr.scan("21");
    pickerBr.scan("22");
    pickerBr.scan("3");
    assertEquals(4, pickerBr.scannedSku.size());
  }

  @Test
  public void testScanWrongWorkedSku() {
    pickerBr.setPickingRequest(fourOrderPr);
    pickerBr.scan("10");
    pickerBr.scan("21");
    pickerBr.scan("22");
    pickerBr.scan("3");
    pickerBr.scan("37");
    pickerBr.scan("38");
    pickerBr.scan("4");
    pickerBr.scan("9");
    assertEquals(8, pickerBr.scannedSku.size());
  }

  @Test
  public void testScanCorrectWorkedSku() throws IOException {
    pickerBr.setPickingRequest(fourOrderPr);
    pickerBr.scan("3");
    pickerBr.scan("4");
    pickerBr.scan("9");
    pickerBr.scan("10");
    pickerBr.scan("21");
    pickerBr.scan("22");
    pickerBr.scan("37");
    pickerBr.scan("38");
    assertEquals(8, pickerBr.scannedSku.size());
  }

  @Test
  public void testScanMoreFascias() {
    pickerBr.setPickingRequest(fourOrderPr);
    pickerBr.scan("3");
    pickerBr.scan("4");
    pickerBr.scan("9");
    pickerBr.scan("10");
    pickerBr.scan("21");
    pickerBr.scan("22");
    pickerBr.scan("37");
    pickerBr.scan("38");
    pickerBr.scan("38");
    assertEquals(9, pickerBr.scannedSku.size());
  }

}
