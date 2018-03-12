package warehousesystem;

import java.io.IOException;
import java.util.ArrayList;

public class OrderManager {

  /** The warehouse system that this OrderManager is in. */
  private WarehouseSystem whSystem;

  /** The ArrayList of Orders that this OrderManager records. */
  private ArrayList<Order> orders = new ArrayList<>();
  /** The ArrayList of pending orders for this OrderManager which is no more than four. */
  private ArrayList<Order> pendingOrders = new ArrayList<>(4);

  /**
   * Initialize an OrderManager with the warehouse system.
   * 
   * @param system the ware house system of the order manager.
   */
  public OrderManager(WarehouseSystem system) {
    whSystem = system;
    whSystem.logConfig("SYSTEM: OrderManager initialized");
  }

  /**
   * Receive the order from system, and create a new order.
   * 
   * @param color the color of the van fascia from the order.
   * @param model the model of the van fascia from the order.
   * @param front the front fascia's sku.
   * @param back the back fascia's sku.
   * @throws IOException For PickingRequest.
   */
  public void receiveOrder(String color, String model, String front, String back)
      throws IOException {
    Order order = new Order(color, model, front, back);
    orders.add(order);
    pendingOrders.add(order);
    whSystem.logInfo("SIM: OrderManager confirms order received");
    if (pendingOrders.size() == 4) {
      ArrayList<Order> fourOrder = new ArrayList<>();
      for (Order newOrder : pendingOrders) {
        fourOrder.add(newOrder);
      }
      PickingRequest pickingRequest = new PickingRequest(fourOrder);
      whSystem.logInfo("SIM: OrderManager generated new Picking Request " + pickingRequest.getId());
      whSystem.processPr(pickingRequest);
      
      // Set the picking request in all the order in orders.
      for (Order o : pendingOrders) {
        o.setPickingRequest(pickingRequest);
      }

      pendingOrders.clear();
    }
  }

  /**
   * Return the pendingOrders that this OrderManager is pending.
   * 
   * @return pendingOrders
   */
  protected ArrayList<Order> getPendingOrders() {
    return pendingOrders;
  }

}
