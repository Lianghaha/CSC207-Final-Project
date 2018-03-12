package warehousesystem;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderManagerTest {

  private WarehouseSystem whSysem;
  private OrderManager orderManagerTester;

  /**
   * Before the test, set up warehouse system and the orderManagerTester.
   * 
   * @throws Exception For CsvTools and FileHandler
   */
  @Before
  public void setUp() throws Exception {
    CsvTools csv = new CsvTools();
    FileHandler handler = new FileHandler("testLog.txt");
    handler.setLevel(Level.ALL);
    CustomFormatter formatter = new CustomFormatter();
    handler.setFormatter(formatter);
    whSysem = new WarehouseSystem(csv.getTranslations(), csv.getInventory(), handler, formatter);

    orderManagerTester = new OrderManager(whSysem);
  }

  /**
   * After the test, set warehouse system and the orderManagerTester to null.
   * 
   */
  @After
  public void tearDown() {
    whSysem = null;
    orderManagerTester = null;
  }

  @Test
  public void testNoOrders() {
    assertEquals(0, orderManagerTester.getPendingOrders().size());
  }

  @Test
  public void testOneOrder() throws IOException {
    orderManagerTester.receiveOrder("Blue", "SES", "37", "38");
    assertEquals(1, orderManagerTester.getPendingOrders().size());
    assertEquals("37", orderManagerTester.getPendingOrders().get(0).getFrontsku());
    assertEquals("38", orderManagerTester.getPendingOrders().get(0).getBacksku());
  }

  @Test
  public void testTwoOrders() throws IOException {
    orderManagerTester.receiveOrder("Blue", "SES", "37", "38");
    orderManagerTester.receiveOrder("Red", "SES", "21", "22");
    assertEquals(2, orderManagerTester.getPendingOrders().size());
    assertEquals("37", orderManagerTester.getPendingOrders().get(0).getFrontsku());
    assertEquals("38", orderManagerTester.getPendingOrders().get(0).getBacksku());
    assertEquals("21", orderManagerTester.getPendingOrders().get(1).getFrontsku());
    assertEquals("22", orderManagerTester.getPendingOrders().get(1).getBacksku());
  }

  @Test
  public void testFourOrders() throws IOException {
    orderManagerTester.receiveOrder("Blue", "SES", "37", "38");
    orderManagerTester.receiveOrder("Beige", "S", "9", "10");
    orderManagerTester.receiveOrder("Red", "SES", "21", "22");
    orderManagerTester.receiveOrder("White", "SE", "3", "4");
    assertEquals(0, orderManagerTester.getPendingOrders().size());
    assertEquals(1, whSysem.getPrList().size());
  }

  @Test
  public void testSixOrders() throws IOException {
    orderManagerTester.receiveOrder("Blue", "SES", "37", "38");
    orderManagerTester.receiveOrder("Beige", "S", "9", "10");
    orderManagerTester.receiveOrder("Red", "SES", "21", "22");
    orderManagerTester.receiveOrder("White", "SE", "3", "4");
    orderManagerTester.receiveOrder("Blue", "SES", "37", "38");
    orderManagerTester.receiveOrder("Beige", "S", "9", "10");
    assertEquals(2, orderManagerTester.getPendingOrders().size());
    assertEquals(1, whSysem.getPrList().size());
  }
}
