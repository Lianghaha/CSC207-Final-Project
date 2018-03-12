package warehousesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WarehouseSystemTest {

  private CsvTools csv;
  private WarehouseSystem tester;
  private HashMap<String, String[]> translations;
  private HashMap<String, Integer> inventory;
  private FileHandler handler;
  private CustomFormatter formatter;
  private StorageManager mockSM;
  private OrderManager mockOM;

  @Before
  public void setUp() throws Exception {
    csv = new CsvTools();
    translations = csv.getTranslations();
    inventory = csv.getInventory();
    handler = new FileHandler("testLog.txt");
    formatter = new CustomFormatter();
    mockSM = mock(StorageManager.class);
    mockOM = mock(OrderManager.class);
    tester = new WarehouseSystem(translations, inventory, handler, formatter);
  }

  @After
  public void tearDown() throws Exception {
    tester = null;
    translations = null;
    inventory = null;
    handler = null;
    formatter = null;
  }

  @Test
  public void testConstructor() {
    assertTrue(tester instanceof WarehouseSystem);
  }

  @Test
  public void testReceiveOrder() throws IOException {
    Mockito.doNothing().when(mockOM).receiveOrder("White", "S", "1", "2");
    tester.setOrderManager(mockOM);
    tester.receiveOrder("White", "S");
    verify(mockOM, times(1)).receiveOrder("White", "S", "1", "2");
  }

  @Test
  public void testPickerProcessPr() {
    PickingRequest pr = mock(PickingRequest.class);
    when(pr.getStatus()).thenReturn("Waiting");
    PickerBarcodeReader pbr = mock(PickerBarcodeReader.class);
    SequencerBarcodeReader sbr = mock(SequencerBarcodeReader.class);
    tester.processBr(sbr);
    tester.processBr(pbr);
    tester.processPr(pr);
    tester.processPr(pr);
    verify(pbr, times(1)).setPickingRequest(pr);
    verify(pr, times(1)).setStatus("Picking");
  }

  @Test
  public void testSequencerProcessPr() {
    PickingRequest pr = mock(PickingRequest.class);
    when(pr.getStatus()).thenReturn("Picked");
    PickerBarcodeReader pbr = mock(PickerBarcodeReader.class);
    SequencerBarcodeReader sbr = mock(SequencerBarcodeReader.class);
    tester.processPr(pr);
    tester.processBr(pbr);
    tester.processBr(sbr);
    tester.processPr(pr);
    verify(sbr, times(2)).setPickingRequest(pr);
    verify(pr, times(2)).setStatus("Sequencing");
  }

  @Test
  public void testLoaderProcessPr() {
    PickingRequest pr = mock(PickingRequest.class);
    when(pr.getStatus()).thenReturn("Sequenced");
    LoaderBarcodeReader lbr = mock(LoaderBarcodeReader.class);
    tester.processPr(pr);
    tester.processBr(lbr);
    tester.processPr(pr);
    verify(lbr, times(2)).setPickingRequest(pr);
    verify(pr, times(2)).setStatus("Loading");
  }

  @Test
  public void testProcessPrWithLoadedPr() {
    PickingRequest pr = mock(PickingRequest.class);
    when(pr.getStatus()).thenReturn("Loaded");
    tester.processPr(pr);
    verify(pr, times(1)).setStatus("Finished");
  }

  @Test
  public void testProcessPrWithNoBrWaiting() {
    PickingRequest pr = mock(PickingRequest.class);
    when(pr.getStatus()).thenReturn("Waiting");
    tester.processPr(pr);
    assertEquals(pr, tester.getPendingPrList().get(0));
  }

  @Test
  public void testScan() {
    tester = new WarehouseSystem(translations, inventory, handler, formatter);
    PickerBarcodeReader br = mock(PickerBarcodeReader.class);
    tester.workerReady("Picker", "Alice");
    tester.workerReady("Loader", "Alice");
    tester.processBr(br);
    tester.setStorageManager(mockSM);
    when(mockSM.levelEmpty("1")).thenReturn(true);
    tester.scan("Picker", "Alice", "1");
    tester.scan("Loader", "Alice", "2");
    verify(mockSM, times(1)).pickFascia("1");
    when(mockSM.levelEmpty("1")).thenReturn(false);
    tester.scan("Picker", "Alice", "1");
  }

  @Test
  public void testRescan() {
    tester.setStorageManager(mockSM);
    tester.workerReady("Picker", "Alice");
    tester.scan("Picker", "Alice", "1");
    tester.rescan("Picker", "Alice", "1");
    verify(mockSM, times(1)).putFasciaBack("1");
  }

  @Test
  public void testDiscard() throws IOException {
    tester = new WarehouseSystem(translations, inventory, handler, formatter);
    tester.workerReady("Sequencer", "HAHA");
    PickingRequest pr = mock(PickingRequest.class);
    when(pr.getStatus()).thenReturn("Picked");
    when(pr.getId()).thenReturn(1);
    tester.processPr(pr);
    tester.discard("Sequencer", "HAHA");
    verify(pr, times(1)).setStatus("Waiting");
    verify(pr, times(1)).findFasciaLocation();
  }

  @Test
  public void testIsNextPrToLoad() throws IOException {
    ArrayList<Order> orders = new ArrayList<>();
    orders.add(new Order("Blue", "SES", "37", "38"));
    PickingRequest pr = new PickingRequest(orders);
    tester.processPr(pr);
    assertTrue(tester.isNextPRtoLoad(pr));
  }

  @Test
  public void testNotNextPrToLoad() throws IOException {
    ArrayList<Order> orders = new ArrayList<>();
    orders.add(new Order("Blue", "SES", "37", "38"));
    PickingRequest pr1 = new PickingRequest(orders);
    PickingRequest pr2 = new PickingRequest(orders);
    PickingRequest pr3 = new PickingRequest(orders);
    tester.processPr(pr1);
    tester.processPr(pr2);
    tester.processPr(pr3);
    pr1.setStatus("Loaded");
    pr3.setStatus("Sequenced");
    assertFalse(tester.isNextPRtoLoad(pr3));
  }

  @Test
  public void testReplenishAmount() {
    tester.setStorageManager(mockSM);
    tester.replenishAmount("11");
    verify(mockSM, times(1)).replenish("11");
  }

  @Test
  public void testUpdateOrder() throws IOException {
    ArrayList<Order> orders = new ArrayList<>();
    orders.add(new Order("Blue", "SES", "37", "38"));
    PickingRequest pr = new PickingRequest(orders);
    tester.updateOrder(pr);
    assertEquals(1, tester.outputOrders().size());
  }

  @Test
  public void testGetPrList() throws IOException {
    ArrayList<Order> orders = new ArrayList<>();
    orders.add(new Order("Blue", "SES", "37", "38"));
    PickingRequest pr = new PickingRequest(orders);
    tester.processPr(pr);
    assertEquals(pr, tester.getPrList().get(0));
  }

  @Test
  public void testOutputInventory() {
    tester.setStorageManager(mockSM);
    tester.outputInventory();
    verify(mockSM, times(1)).getStorages();
  }

  @Test
  public void testOutputOrders() throws IOException {
    ArrayList<Order> orders = new ArrayList<>();
    orders.add(new Order("Blue", "SES", "37", "38"));
    PickingRequest pr = new PickingRequest(orders);
    tester.updateOrder(pr);
    assertEquals(pr, tester.outputOrders().get(0));
  }

}
