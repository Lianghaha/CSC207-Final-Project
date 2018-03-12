package warehousesystem;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WarehouseControllerTest {

  private WarehouseSystem whs;
  private WarehouseController wc;

  @Before
  public void creat() throws SecurityException, IOException {
    whs = mock(WarehouseSystem.class);
    wc = new WarehouseController("16orders.txt");
  }

  @After
  public void tearDown() {
    whs = null;
    wc = null;
  }

  @Test
  public void testOrderEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "Order SES Blue");
    Mockito.doNothing().when(whs).receiveOrder("Blue", "SES");
    wc.setLines(new ArrayList<>(Arrays.asList("Order SES Blue")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "Order SES Blue");
    verify(whs, times(1)).receiveOrder("Blue", "SES");
  }

  @Test
  public void testWorkReadyEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "Replenisher Ruby ready");
    Mockito.doNothing().when(whs).workerReady("Replenisher", "Ruby");
    wc.setLines(new ArrayList<>(Arrays.asList("Replenisher Ruby ready")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "Replenisher Ruby ready");
    verify(whs, times(1)).workerReady("Replenisher", "Ruby");
  }

  @Test
  public void testPickerPickedEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "Picker Alice picked 11");
    Mockito.doNothing().when(whs).scan("Picker", "Alice", "11");
    wc.setLines(new ArrayList<>(Arrays.asList("Picker Alice picked 11")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "Picker Alice picked 11");
    verify(whs, times(1)).scan("Picker", "Alice", "11");
  }

  @Test
  public void testPickerRecansEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "Picker Alice rescanned 11");
    Mockito.doNothing().when(whs).rescan("Picker", "Alice", "11");
    wc.setLines(new ArrayList<>(Arrays.asList("Picker Alice rescanned 11")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "Picker Alice rescanned 11");
    verify(whs, times(1)).rescan("Picker", "Alice", "11");
  }

  @Test
  public void testSequencerRecansEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "Sequencer Alice rescanned");
    Mockito.doNothing().when(whs).rescan("Sequencer", "Alice", "0");
    wc.setLines(new ArrayList<>(Arrays.asList("Sequencer Alice rescanned")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "Sequencer Alice rescanned");
    verify(whs, times(1)).rescan("Sequencer", "Alice", "0");
  }

  @Test
  public void testSequencerDiscardsEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "Sequencer Alice discarded");
    Mockito.doNothing().when(whs).discard("Sequencer", "Alice");
    wc.setLines(new ArrayList<>(Arrays.asList("Sequencer Alice discarded")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "Sequencer Alice discarded");
    verify(whs, times(1)).discard("Sequencer", "Alice");
  }

  @Test
  public void testPickerToMarshallingEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "Picker Alice to Marshaling");
    Mockito.doNothing().when(whs).workerFinished("Picker", "Alice");
    wc.setLines(new ArrayList<>(Arrays.asList("Picker Alice to Marshaling")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "Picker Alice to Marshaling");
    verify(whs, times(1)).workerFinished("Picker", "Alice");
  }

  @Test
  public void testSequencerFinishedEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "Sequencer Sue finished");
    Mockito.doNothing().when(whs).workerFinished("Sequencer", "Sue");
    wc.setLines(new ArrayList<>(Arrays.asList("Sequencer Sue finished")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "Sequencer Sue finished");
    verify(whs, times(1)).workerFinished("Sequencer", "Sue");
  }

  @Test
  public void testInvalidEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "HAHA 123");
    Mockito.doNothing().when(whs).logConfig("SYSTEM: Invalid input read, continuing");
    wc.setLines(new ArrayList<>(Arrays.asList("HAHA 123")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "HAHA 123");
    verify(whs, times(1)).logConfig("SYSTEM: Invalid input read, continuing");
  }

  @Test
  public void testSecondInvalidEvent() throws IOException {
    Mockito.doNothing().when(whs).logConfig("INPUT: " + "Sequencer Sue haha");
    Mockito.doNothing().when(whs).logConfig("SYSTEM: Invalid input read, continuing");
    wc.setLines(new ArrayList<>(Arrays.asList("Sequencer Sue haha")));
    wc.setWhs(whs);
    wc.runModel();
    verify(whs, times(1)).logConfig("INPUT: " + "Sequencer Sue haha");
    verify(whs, times(1)).logConfig("SYSTEM: Invalid input read, continuing");
  }
}
