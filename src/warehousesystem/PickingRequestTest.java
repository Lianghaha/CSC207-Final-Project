package warehousesystem;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PickingRequestTest {

  private PickingRequest testPr;
  private ArrayList<Order> orders = new ArrayList<>();

  /**
   * Before the test, set orders with four order and testPr as new picking request.
   * 
   * @throws IOException for new PickingRequest.
   */
  @Before
  public void setUp() throws IOException {
    Order o1 = new Order("Blue", "SES", "37", "38");
    Order o2 = new Order("Beige", "S", "9", "10");
    Order o3 = new Order("Red", "SES", "21", "22");
    Order o4 = new Order("White", "SE", "3", "4");
    orders.add(o1);
    orders.add(o2);
    orders.add(o3);
    orders.add(o4);
    testPr = new PickingRequest(orders);
  }

  /**
   * After the test, set orders and testPr as null.
   */
  @After
  public void tearDown() {
    testPr = null;
    orders = null;
  }

  @Test
  public void testGetId() {
    int output = testPr.getId();
    int expected = testPr.getStaticPrId() - 1;
    assertEquals(expected, output);
  }

  @Test
  public void testGetStatus() {
    String output = testPr.getStatus();
    assertEquals("Waiting", output);
  }

  @Test
  public void testSetStatus() {
    testPr.setStatus("Loading");
    String output = testPr.getStatus();
    assertEquals("Loading", output);
  }

  @Test
  public void testFindFasciaLocation() {
    ArrayList<String> location = new ArrayList<>(8);
    location.add("A,0,0,2");
    location.add("A,0,0,3");
    location.add("A,0,2,0");
    location.add("A,0,2,1");
    location.add("A,1,2,0");
    location.add("A,1,2,1");
    location.add("B,1,0,0");
    location.add("B,1,0,1");
    String output = testPr.getNextLocation();
    assertEquals(location.remove(0), output);
  }

  @Test
  public void testGetNextLocation() {
    String output = testPr.getNextLocation();
    assertEquals("A,0,0,2", output);
  }

  @Test
  public void testGetNextSku() {
    String output1 = testPr.getNextSku();
    String output2 = testPr.getNextSku();
    assertEquals("3", output1);
    assertEquals("4", output2);
    String output3 = testPr.getNextSku();
    String output4 = testPr.getNextSku();
    assertEquals("9", output3);
    assertEquals("10", output4);
    String output5 = testPr.getNextSku();
    String output6 = testPr.getNextSku();
    assertEquals("21", output5);
    assertEquals("22", output6);
    String output7 = testPr.getNextSku();
    String output8 = testPr.getNextSku();
    assertEquals("37", output7);
    assertEquals("38", output8);
  }


  @Test
  public void testFindCorrectOrder() {
    ArrayList<String> skuNumbers = new ArrayList<>();
    skuNumbers.add("37");
    skuNumbers.add("9");
    skuNumbers.add("21");
    skuNumbers.add("3");
    skuNumbers.add("38");
    skuNumbers.add("10");
    skuNumbers.add("22");
    skuNumbers.add("4");
    ArrayList<String> output = testPr.findCorrectOrder();
    assertEquals(skuNumbers, output);
  }

  @Test
  public void testGetCorrectOrder() {
    ArrayList<String> skuNumbers = new ArrayList<>();
    skuNumbers.add("37");
    skuNumbers.add("9");
    skuNumbers.add("21");
    skuNumbers.add("3");
    skuNumbers.add("38");
    skuNumbers.add("10");
    skuNumbers.add("22");
    skuNumbers.add("4");
    ArrayList<String> output = testPr.getCorrectOrder();
    assertEquals(skuNumbers, output);
  }

  @Test
  public void testGetOrders() {
    ArrayList<Order> output = testPr.getOrders();
    assertEquals(orders.toString(), output.toString());
  }

}
