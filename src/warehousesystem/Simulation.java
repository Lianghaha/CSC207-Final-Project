package warehousesystem;

import java.io.IOException;

public class Simulation {

  /**
   * The main method for Stimulation.
   * 
   * @param args The args
   * @throws IOException The IOException
   */
  public static void main(String[] args) throws IOException {
    WarehouseController whController = new WarehouseController(args[0]);
    whController.runModel();
  }

}
