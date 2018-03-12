package warehousesystem;

public class Order {

  /** The color of the van in this Order. */
  private String color;
  /** The model of the van in this Order. */
  private String model;
  /** The SKU of front fascia in this Order. */
  private String frontSku;
  /** The SKU of back fascia in this Order. */
  private String backSku;
  /** The status of the Order. */
  private String status;
  /** The picking request that the Order is in. */
  private PickingRequest pickingRequest;

  /**
   * Initialize a new Order with the color, model of the van and the front and back skus.
   * 
   * @param color the color of the van.
   * @param model the model of the van.
   * @param front the front sku String of this order.
   * @param back the back sku String of this order.
   */
  public Order(String color, String model, String front, String back) {
    this.color = color;
    this.model = model;
    frontSku = front;
    backSku = back;
  }

  /**
   * Return the front sku number of this order.
   * 
   * @return frontSku
   */
  public String getFrontsku() {
    return frontSku;
  }

  /**
   * Return the back sku number of this order.
   * 
   * @return backSku
   */
  public String getBacksku() {
    return backSku;
  }

  /**
   * Return the status of this order.
   * 
   * @return Status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Change the status to new status.
   * 
   * @param status The new status of this order.
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Return the picking request that this order is in.
   * 
   * @return pickingRequest
   */
  public PickingRequest getPr() {
    return pickingRequest;
  }

  /**
   * Set picking request to the order where it involves.
   * 
   * @param pickingRequest The new picking request created involved this order.
   */
  public void setPickingRequest(PickingRequest pickingRequest) {
    this.pickingRequest = pickingRequest;
  }

  /**
   * Return the color and model of this order.
   *
   * @return a string representation of the order.
   */
  public String toString() {
    return color + "," + model;
  }

}
