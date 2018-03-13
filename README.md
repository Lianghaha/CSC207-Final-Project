# CSC207-Project

1. What is this project about?

    The purpose of this project is to desigin a system that provides computer support to a warehousing company. This program should be able to track the status of the orders from the customer, provide computer support for warehousing workers, and keep track of inventory levels in the warehouse. Thus, the program should be able to distinguish different types of input(orders from customer, actions from worker) and generate correct output(order status, level of inventory, tell workers where to go, what to do......). In reality, the program might communicate with workers and customes by receving and sending data through barcode reader or website, but, in this project, the input and output of the system are simplified to lines of texts that are stored in txt files. This simplification helps us to focus on the processing part of the program.    
    
2. Program Unit Tests:

    90% of the code is covered by the tests. We've used 2 tools to accomplish it:
    
    1. Mockito:
    A third-party Java Framework Library that allows us to create mock objects for testing dependency-heavy classes, in our case WarehouseController and WarehouseSystem
    2. JUnit 4: 
    An Eclipse built-in library that allows us to consolidate all of our tests into a test suite, which we've called "AllTest.java"
               

3. Details about How the program works?
    1. To make the program organized and readable, we create OrderManager, WarehousPicking and StorageManager Classes that interact with other classes to handle different part of work. 
    
    2. Recieve and translate order
    The program recieve the order from input and send it to OrderManager. OrderManager will translate the order (using translation.csv) from text to SKU number which the warehouse uses to represent product. The OrderManager will hold the order until there is certain amount of Orders recieved.
    
    3. Generate Picking Request. 
    When certain amount of orde is recieved, the OrderManager will send data to WarehousePicking to generate PickingRequest. The WarehousePicking will assign PickingRequest a Picker(a type of Worker) if the Picker is ready. The WarehousePicking will also get the location of the product(using traversal_table.csv) and tell the Picker where to pick the product. If no worker is ready, the WarehousePicking will hold the PickingRequest in a waitlist.   
    
    4. Picker(a type of Worker) picks product.
    Picker will notify the system when he picks a product by scanning the SKU number on the product. System will call StorageManager to modify the inventory.
    
    5. Sequencer sequance the product.
