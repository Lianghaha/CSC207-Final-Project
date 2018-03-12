package warehousesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class WarehousePickingTest {

  @Test
  public void testConstructor() {
    WarehousePicking whp = new WarehousePicking();
    assertNotNull(whp);
  }

  @Test
  public void testOptimize() throws IOException {
    ArrayList<String> skus = new ArrayList<>();
    skus.add("3");
    skus.add("10");
    skus.add("9");
    skus.add("4");
    ArrayList<String> output = new ArrayList<>();
    output.add("A,0,0,2" + ",3");
    output.add("A,0,0,3" + ",4");
    output.add("A,0,2,0" + ",9");
    output.add("A,0,2,1" + ",10");

    assertEquals(output, WarehousePicking.optimize(skus));
  }

}
