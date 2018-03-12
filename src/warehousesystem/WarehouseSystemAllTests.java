package warehousesystem;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({BarcodeReaderFactoryTest.class, CsvToolsTest.class, LoaderBarcodeReaderTest.class,
    OrderManagerTest.class, OrderTest.class, PickerBarcodeReaderTest.class,
    PickingRequestTest.class, ReplenisherBarcodeReaderTest.class, SequencerBarcodeReaderTest.class,
    StorageManagerTest.class, WarehouseControllerTest.class, WarehousePickingTest.class,
    WarehouseSystemTest.class})
public class WarehouseSystemAllTests {

}
