package warehousesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class WarehouseController {

  private WarehouseSystem whs;
  private CsvTools csv;
  private ArrayList<String> lines;
  private FileHandler handler;
  private CustomFormatter formatter;

  /**
   * Initiate a new WarehouseController with the inputFilePath.
   * 
   * @param inputFilePath The file with inputFilePath as a input file.
   * @throws SecurityException for setLevel and setFormatter
   * @throws IOException for CsvTools and readFile
   */
  public WarehouseController(String inputFilePath) throws SecurityException, IOException {
    csv = new CsvTools();
    handler = new FileHandler("log.txt");
    handler.setLevel(Level.ALL);
    formatter = new CustomFormatter();
    handler.setFormatter(formatter);
    whs = new WarehouseSystem(csv.getTranslations(), csv.getInventory(), handler, formatter);
    lines = csv.readFile(inputFilePath);
  }

  /**
   * Run the warehouse system model to process each line of input from the input file.
   * 
   * @throws IOException for receiveOrder, updateView, workerFinished
   */
  public void runModel() throws IOException {
    for (String line : lines) {
      whs.logConfig("INPUT: " + line);
      String object = line.split(" ", 2)[0];
      String data = line.split(" ", 2)[1];

      if (object.equals("Order")) {
        String model = data.split(" ", 2)[0];
        String color = data.split(" ", 2)[1];
        whs.receiveOrder(color, model);
      } else if (object.equals("Picker") | object.equals("Sequencer") | object.equals("Loader")
          | object.equals("Replenisher")) {
        String[] actions = data.split(" ", 4);
        String name = actions[0];
        String status = actions[1];
        if (status.equals("ready")) {
          whs.workerReady(object, name);
        } else if (status.equals("picked") | status.equals("sequenced") | status.equals("loaded")
            | status.equals("replenished")) {
          String sku = actions[2];
          whs.scan(object, name, sku);
        } else if (status.equals("rescanned")) {
          String sku;
          if (actions.length > 2) {
            sku = actions[2];
          } else {
            sku = "0";
          }
          whs.rescan(object, name, sku);
        } else if (status.equals("discarded")) {
          whs.discard(object, name);
        } else if (status.equals("to") | status.equals("finished")) {
          whs.workerFinished(object, name);
        } else {
          whs.logConfig("SYSTEM: Invalid input read, continuing");
        }
      } else {
        whs.logConfig("SYSTEM: Invalid input read, continuing");
      }
    }
    this.updateView();
  }

  /**
   * Update the final.csv, order.csv, and the log.txt.
   * 
   * @throws IOException for writeFinal and wirteOrder
   */
  public void updateView() throws IOException {
    whs.logConfig("SYSTEM: Simulation Complete");
    csv.writeFinal(whs.outputInventory());
    csv.writeOrder(whs.outputOrders());
  }

  public void setWhs(WarehouseSystem whs) {
    this.whs = whs;
  }

  public void setLines(ArrayList<String> lines) {
    this.lines = lines;
  }

}
