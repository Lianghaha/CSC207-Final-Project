package warehousesystem;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

public class CsvToolsTest {

  @Test
  public void testReadMultipleLinesFile() throws IOException {
    CsvTools csvTestor = new CsvTools();
    ArrayList<String> result = csvTestor.readFile("testMultiple.txt");
    ArrayList<String> expect = new ArrayList<>(Arrays.asList("123", "abc", "--?"));
    assertEquals(expect, result);
  }

  @Test
  public void testReadEmptyFile() throws IOException {
    CsvTools csvTestor = new CsvTools();
    ArrayList<String> result = csvTestor.readFile("testEmpty.txt");
    ArrayList<String> expect = new ArrayList<>();
    assertEquals(expect, result);
  }

  @Test
  public void testReadNotExistFile() throws IOException {
    CsvTools csvTestor = new CsvTools();
    ArrayList<String> result = csvTestor.readFile("NotExist.txt");
    ArrayList<String> expect = new ArrayList<>();
    assertEquals(expect, result);
  }

  @Test
  public void testResetFile() throws IOException {
    File file = new File("testEmpty.txt");
    FileWriter fw = new FileWriter(file, true);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write("123\r\n");
    bw.write("456");
    bw.close();
    CsvTools csvTestor = new CsvTools();
    assertEquals(new ArrayList<String>(Arrays.asList("123", "456")),
        csvTestor.readFile("testEmpty.txt"));
    assertEquals(true, csvTestor.resetFile("testEmpty.txt"));
    assertEquals(new ArrayList<String>(), csvTestor.readFile("testEmpty.txt"));
  }

  @Test
  public void testResetFileWithNotExistFile() throws IOException {
    File file = new File("NotExist.txt");
    assertEquals(false, file.exists());
    CsvTools csvTestor = new CsvTools();
    assertEquals(false, csvTestor.resetFile("NotExist.txt"));
    assertEquals(true, file.exists());
    assertEquals(new ArrayList<String>(), csvTestor.readFile("NotExist.txt"));
    file.delete();
  }

  @Test
  public void testReadTraversal() throws IOException {
    CsvTools csvTestor = new CsvTools();
    csvTestor.readTraversal("traversal_table.csv");
    int sku = Integer.valueOf("1");
    Integer expectStock = 30;
    while (sku <= 48) {
      String realSku = "" + sku;
      String location = csvTestor.getSkuToLocation().get(realSku);
      assertEquals(expectStock, csvTestor.getInventory().get(realSku));
      assertEquals(realSku, csvTestor.getLocationToSku().get(location));
      sku++;
    }
  }

  @Test
  public void testUpdateInventory() throws IOException {
    CsvTools csvTestor = new CsvTools();
    csvTestor.updateInventory("initial.csv");
    ArrayList<String> lines = csvTestor.readFile("initial.csv");
    for (String line : lines) {
      String[] lineContent = line.split(",", 5);
      String location = line.substring(0, 7);
      Integer amount = Integer.parseInt(lineContent[4]);
      String sku = csvTestor.getLocationToSku().get(location);
      assertEquals(amount, csvTestor.getInventory().get(sku));

    }
  }

  @Test
  public void testUpdateInventoryWhenInitialNotExist() throws IOException {
    CsvTools csvTestor = new CsvTools();
    assertEquals(false, csvTestor.updateInventory("NotExist.txt"));
  }


  @Test
  public void testReadTranslation() throws IOException {
    CsvTools csvTestor = new CsvTools();
    csvTestor.readTranslation("translation.csv");
    String expectSku1 = "1";
    assertEquals(expectSku1, csvTestor.getTranslations().get("WhiteS")[0]);
    String expectSku2 = "10";
    assertEquals(expectSku2, csvTestor.getTranslations().get("BeigeS")[1]);
    String expectSku3 = "19";
    assertEquals(expectSku3, csvTestor.getTranslations().get("RedSE")[0]);
    String expectSku4 = "30";
    assertEquals(expectSku4, csvTestor.getTranslations().get("GreenSES")[1]);
    String expectSku5 = "48";
    assertEquals(expectSku5, csvTestor.getTranslations().get("BlackSEL")[1]);
  }


  @Test
  public void testWriteFinalWithEmptyStorage() throws IOException {
    String filePath = "final.csv";
    CsvTools csvTestor = new CsvTools();
    csvTestor.resetFile(filePath);
    HashMap<String, Integer> testStorage = new HashMap<>();
    assertEquals(true, testStorage.isEmpty());
    csvTestor.writeFinal(testStorage);
    assertEquals(true, csvTestor.readFile(filePath).isEmpty());
  }

  @Test
  public void testWriteFinalWithFullStorage() throws IOException {
    String filePath = "final.csv";
    CsvTools csvTestor = new CsvTools();
    csvTestor.resetFile(filePath);
    HashMap<String, Integer> testStorage = new HashMap<>();
    for (int i = 1; i < 49; i++) {
      String sku = "" + i;
      testStorage.put(sku, 30);
    }
    assertEquals(false, testStorage.isEmpty());
    csvTestor.writeFinal(testStorage);
    assertEquals(true, csvTestor.readFile(filePath).isEmpty());
  }

  @Test
  public void testWriteFinalWithOneNotFullStorage() throws IOException {
    String filePath = "final.csv";
    CsvTools csvTestor = new CsvTools();
    csvTestor.resetFile(filePath);
    HashMap<String, Integer> testStorage = new HashMap<>();
    for (int i = 1; i < 48; i++) {
      String sku = "" + i;
      testStorage.put(sku, 30);
    }
    testStorage.put("48", 6);
    csvTestor.writeFinal(testStorage);
    assertEquals(1, csvTestor.readFile(filePath).size());
    assertEquals("B,1,2,3,6", csvTestor.readFile(filePath).get(0));
  }

  @Test
  public void testWriteOrder() throws IOException {
    CsvTools csvTestor = new CsvTools();
    String filePath = "order.csv";
    csvTestor.resetFile(filePath);
    Order o1 = new Order("Blue", "SES", "37", "38");
    Order o2 = new Order("Beige", "S", "9", "10");
    Order o3 = new Order("Red", "SES", "21", "22");
    Order o4 = new Order("White", "SE", "3", "4");
    ArrayList<Order> orders = new ArrayList<>(Arrays.asList(o1, o2, o3, o4));
    PickingRequest pr = new PickingRequest(orders);
    ArrayList<PickingRequest> prList = new ArrayList<>();
    prList.add(pr);
    csvTestor.writeOrder(prList);
    ArrayList<String> lines = csvTestor.readFile(filePath);
    for (int i = 0; i < lines.size(); i++) {
      assertEquals(orders.get(i).toString(), lines.get(i));
    }
  }

}
