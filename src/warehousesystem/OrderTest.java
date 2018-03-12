package warehousesystem;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class OrderTest {

  Order order = new Order("Blue", "SES", "37", "38");

  @Test
  public void testGetFrontsku() {
    String output = order.getFrontsku();
    assertEquals("37", output);
  }

  @Test
  public void testGetBacksku() {
    String output = order.getBacksku();
    assertEquals("38", output);
  }

  @Test
  public void testGetStatus() {
    String output = order.getStatus();
    assertEquals(null, output);
  }

  @Test
  public void testSetStatus() {
    order.setStatus("Picking");
    String output = order.getStatus();
    assertEquals("Picking", output);
  }

  @Test
  public void testGetPr() {
    PickingRequest output1 = order.getPr();
    assertEquals(null, output1);
  }

  @Test
  public void testSetPickingRequest() throws IOException {
    Order o1 = new Order("Blue", "SES", "37", "38");
    Order o2 = new Order("Beige", "S", "9", "10");
    Order o3 = new Order("Red", "SES", "21", "22");
    Order o4 = new Order("White", "SE", "3", "4");
    ArrayList<Order> orders = new ArrayList<>();
    orders.add(o1);
    orders.add(o2);
    orders.add(o3);
    orders.add(o4);
    PickingRequest pr = new PickingRequest(orders);
    for (Order order : orders) {
      order.setPickingRequest(pr);
      assertEquals(pr, order.getPr());
    }
  }

  @Test
  public void testToString() {
    String output = order.toString();
    assertEquals("Blue,SES", output);
  }

}
