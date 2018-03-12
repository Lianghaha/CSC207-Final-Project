package warehousesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CsvTools {

  /** A mapping of String (sku) to Integer (amount). */
  private HashMap<String, Integer> inventory = new HashMap<>();
  /** A mapping of String (sku) to String (location). */
  private HashMap<String, String> skuToLocation = new HashMap<>();
  /** A mapping of String (location) to Integer (sku). */
  private HashMap<String, String> locationToSku = new HashMap<>();
  /** A mapping of String (color and model) to Integer[] (sku). */
  private HashMap<String, String[]> translations = new HashMap<>();

  /**
   * Initialize a new CsvTools. Read traversal_table.csv, initial.csv, and translation.csv to set up
   * HashMap of inventory, skuToLoction, locationToSku and translations. Reset order.csv and
   * final.csv to rewrite two files.
   * 
   * @throws IOException For readFile.
   */
  public CsvTools() throws IOException {
    readTraversal("traversal_table.csv");
    updateInventory("initial.csv");
    readTranslation("translation.csv");
    resetFile("order.csv");
    resetFile("final.csv");
  }

  /**
   * Return Inventory of this CsvTools.
   * 
   * @return Inventory
   */
  public HashMap<String, Integer> getInventory() {
    return inventory;
  }

  /**
   * Return the skuToLoctaion of this CsvTools.
   * 
   * @return skuToLocation
   */
  public HashMap<String, String> getSkuToLocation() {
    return skuToLocation;
  }

  /**
   * Return the locationToSku of this CsvTools.
   * 
   * @return locationToSku
   */
  public HashMap<String, String> getLocationToSku() {
    return locationToSku;
  }

  /**
   * Return the translations of this CsvTools.
   * 
   * @return translations
   */
  public HashMap<String, String[]> getTranslations() {
    return translations;
  }

  /**
   * Return the ArrayList of lines from the file after read the file with filePath as file path.
   * 
   * @param filePath The file path that need to be read.
   * @return An ArrayList that contains all the lines in the file.
   * @throws IOException for FileReader
   */
  public ArrayList<String> readFile(String filePath) throws IOException {
    ArrayList<String> result = new ArrayList<>();
    File file = new File(filePath);
    if (!file.exists()) {
      return result;
    }
    BufferedReader br = new BufferedReader(new FileReader(filePath));
    while (br.ready()) {
      result.add(br.readLine());
    }
    br.close();
    return result;
  }

  /**
   * Return true if and only if the reset file the file with filePath exist, clear the file and
   * write new things. If the file does not exist, creat a new file and return false;
   * 
   * @param filePath The file path that need to be check if it exist or not.
   * @return return true iff file with filePath exist , else return false.
   * @throws IOException for createNewFile and PrintWriter
   */
  public boolean resetFile(String filePath) throws IOException {
    File file = new File(filePath);
    if (!file.exists()) {
      file.createNewFile();
      return false;
    }
    PrintWriter writer = new PrintWriter(file);
    writer.print("");
    writer.close();
    return true;
  }

  /**
   * Read the traversal_table.csv to initial the skuToLocation, locaitonToSku and inventory maps.
   * 
   * @param filePath The file path that need to be read.
   * @throws IOException for readFile
   */
  public void readTraversal(String filePath) throws IOException {
    ArrayList<String> lines = readFile(filePath);
    for (String line : lines) {
      String[] lineContent = line.split(",", 5);
      String location = line.substring(0, 7);
      String sku = lineContent[4];
      skuToLocation.put(sku, location);
      locationToSku.put(location, sku);
      inventory.put(sku, 30);
    }
  }

  /**
   * Return true if and only if initial.csv exist and read the initial.csv to update the Inventory
   * map.
   * 
   * @param filePath The file path that need to be read.
   * @return return true iff initial.csv exist.
   * @throws IOException for readFile
   */
  public boolean updateInventory(String filePath) throws IOException {
    boolean flag = false;
    File file = new File(filePath);
    if (file.exists()) {
      flag = true;
      ArrayList<String> lines = readFile(filePath);
      for (String line : lines) {
        String[] lineContent = line.split(",", 5);
        String initialLocation = line.substring(0, 7);
        int initialAmount = Integer.parseInt(lineContent[4]);
        String targetedSku = locationToSku.get(initialLocation);
        inventory.put(targetedSku, initialAmount);
      }
    }
    return flag;
  }

  /**
   * Read the translation.csv to initial the translations map.
   * 
   * @param filePath The file path that need to be read.
   * @throws IOException for readFile
   */
  public void readTranslation(String filePath) throws IOException {
    ArrayList<String> lines = readFile(filePath);
    lines.remove(0);
    int index = 0;
    for (String line : lines) {
      if (index != 25) {
        String[] lineContent = line.split(",", 4);
        String colour = lineContent[0];
        String model = lineContent[1];
        String skuf = lineContent[2];
        String skub = lineContent[3];
        String order = colour + model;
        String[] skuNumbers = {skuf, skub};
        translations.put(order, skuNumbers);
      }
      index += 1;
    }
  }

  /**
   * Write the final.csv in project folder according to the storages.
   * 
   * @param storages The map that need to be wrote.
   * @throws IOException for FileWriter
   */
  public void writeFinal(Map<String, Integer> storages) throws IOException {
    String filePath = "final.csv";
    if (!storages.isEmpty()) {
      File file = new File(filePath);
      FileWriter fw = new FileWriter(file);
      BufferedWriter bw = new BufferedWriter(fw);
      int index = 1;
      while (index <= 48) {
        String sku = "" + index;
        int amount = storages.get(sku);
        String location = skuToLocation.get(sku);
        if (amount < 30) {
          bw.write(location + "," + amount + "\r\n");
        }
        index++;
      }
      bw.close();
    }
  }

  /**
   * Write the order.csv in project folder according to the ArrayList of picking requests.
   * 
   * @param prList The ArrayList of picking requests that is going to be wrote.
   * @throws IOException for FileWriter
   */

  public void writeOrder(ArrayList<PickingRequest> prList) throws IOException {
    String filePath = "order.csv";
    File file = new File(filePath);
    FileWriter fw = new FileWriter(file, true);
    BufferedWriter bw = new BufferedWriter(fw);
    for (PickingRequest pr : prList) {
      for (Order order : pr.getOrders()) {
        bw.write(order.toString() + "\r\n");
      }
    }
    bw.close();
  }
}
