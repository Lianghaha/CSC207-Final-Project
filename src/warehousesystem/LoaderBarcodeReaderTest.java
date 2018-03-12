package warehousesystem;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoaderBarcodeReaderTest {

  private PickingRequest emptyPr;
  private PickingRequest fourOrderPr;
  private WarehouseSystem whSystem;
  private LoaderBarcodeReader loaderBr;

  /**
   * Before the test, set up the empty picking request, a new picking request that contains four
   * orders, and the loader barcode reader to test.
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

    loaderBr = new LoaderBarcodeReader("user", emptyPr, whSystem);
  }

  /**
   * After the test, set the empty picking request, the four order picking request and the loader
   * barcode reader to null.
   */
  @After
  public void tearDown() {
    emptyPr = null;
    fourOrderPr = null;
    loaderBr = null;
  }

  @Test
  public void testGetUser() {
    assertEquals("user", loaderBr.getUser());
  }

  @Test
  public void testGetPickingRequest() {
    assertEquals(emptyPr, loaderBr.getPickingRequest());
  }

  @Test
  public void testSetNullPickingRequest() {
    loaderBr.setPickingRequest(null);
    assertEquals(0, loaderBr.scannedSku.size());
  }

  @Test
  public void testSetPickingRequest() {
    loaderBr.setPickingRequest(fourOrderPr);
    assertEquals(fourOrderPr, loaderBr.getPickingRequest());
  }

  @Test
  public void testScanLessFascias() {
    loaderBr.setPickingRequest(fourOrderPr);
    loaderBr.getNextStep();
    loaderBr.scan("37");
    loaderBr.scan("9");
    loaderBr.scan("21");
    loaderBr.scan("3");
    assertEquals(4, loaderBr.scannedSku.size());
  }

  @Test
  public void testScanCorrectOrder() {
    loaderBr.setPickingRequest(fourOrderPr);
    loaderBr.getNextStep();
    loaderBr.scan("37");
    loaderBr.scan("9");
    loaderBr.scan("21");
    loaderBr.scan("3");
    loaderBr.scan("38");
    loaderBr.scan("10");
    loaderBr.scan("22");
    loaderBr.scan("4");
    assertEquals(8, loaderBr.scannedSku.size());
    assertEquals(loaderBr.scannedSku.toString(), fourOrderPr.getCorrectOrder().toString());
  }

  @Test
  public void testScanWrongOrder() {
    loaderBr.setPickingRequest(fourOrderPr);
    loaderBr.getNextStep();
    loaderBr.scan("38");
    loaderBr.scan("9");
    loaderBr.scan("21");
    loaderBr.scan("3");
    loaderBr.scan("37");
    loaderBr.scan("10");
    loaderBr.scan("22");
    loaderBr.scan("4");
    assertEquals(8, loaderBr.scannedSku.size());
  }

  @Test
  public void testScanMoreFascias() {
    loaderBr.setPickingRequest(fourOrderPr);
    loaderBr.getNextStep();
    loaderBr.scan("37");
    loaderBr.scan("9");
    loaderBr.scan("21");
    loaderBr.scan("3");
    loaderBr.scan("38");
    loaderBr.scan("10");
    loaderBr.scan("22");
    loaderBr.scan("4");
    loaderBr.scan("4");
    assertEquals(9, loaderBr.scannedSku.size());
  }

}
