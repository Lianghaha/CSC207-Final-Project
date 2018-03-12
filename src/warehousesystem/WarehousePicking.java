package warehousesystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WarehousePicking {

  /** The sku to location map. This is static because optimize is static. */
  private static Map<String, String> skuToLocation = new HashMap<>();

  /**
   * Based on the Integer SKUs in List 'skus', return a List of locations, where each location is a
   * String containing 5 pieces of information: the zone character (in the range ['A' .. 'B']), the
   * aisle number (an integer in the range [0 .. 1]), the rach number (an integer in the range [0 ..
   * 2]), and the level on the rack (an integer in the range [0 .. 3]), and the SKU number.
   * 
   * @param skus the list of skus to retrieve.
   * @return the List of locations.
   * @throws IOException For readTraversal.
   */
  public static ArrayList<String> optimize(ArrayList<String> skus) throws IOException {
    readTraversal();
    Map<String, String> locationMap = skuToLocation;
    ArrayList<String> locations = new ArrayList<>();
    for (String sku : sort(skus)) {
      String location = locationMap.get(sku);
      locations.add(location + "," + sku);
    }
    return locations;
  }

  /**
   * Return the ArrayList of sorted skus. For our skus, change the String skus to Integers and
   * sorted in increasing order.
   * 
   * @param skus The ArrayList of skus that is going to be sorted.
   * @return The ArrayList of skus that is sorted.
   */
  public static ArrayList<String> sort(ArrayList<String> skus) {
    ArrayList<String> sortSkus = new ArrayList<>();
    ArrayList<Integer> intSkus = new ArrayList<>();
    for (String sku : skus) {
      int intSku = Integer.valueOf(sku);
      intSkus.add(intSku);
    }
    Collections.sort(intSkus);
    for (int intSku : intSkus) {
      String sku = "" + intSku;
      sortSkus.add(sku);
    }
    return sortSkus;
  }

  /**
   * Read the traversal_table.csv to initial the Maps.
   * 
   * @throws IOException For readFile
   */
  public static void readTraversal() throws IOException {
    ArrayList<String> lines = readFile("traversal_table.csv");
    for (String line : lines) {
      String[] lineContent = line.split(",", 5);
      String location = line.substring(0, 7);
      String sku = lineContent[4];
      skuToLocation.put(sku, location);
    }
  }

  /**
   * A method to read a file from the filepath.
   * 
   * @param filepath The filepath that need to be read.
   * @return An ArrayList that contains all the lines in the file.
   * @throws IOException for FileReader.
   */
  public static ArrayList<String> readFile(String filepath) throws IOException {
    ArrayList<String> result = new ArrayList<>();
    BufferedReader br = new BufferedReader(new FileReader(filepath));
    while (br.ready()) {
      result.add(br.readLine());
    }
    br.close();
    return result;
  }

}
