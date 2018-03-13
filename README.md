# CSC207-Project

1. Leaning objective of the project.
    
    The learning objective of this project is to utilize the design concepts and Java knowledge we learned from the course to develop a Java program that solves problems. We were only given the demands and requirements of the project, so we had to figure out the boundries and design the stucture of the program by outselives. We were also requied to write lot of unittests, work with other people and implement our program in a short amount of time. 
    
    We were required to write unittest and make the test coerage as high as possible. We wrote unitest for all methods that can be tested and reached 90%+ coverage. 
    
    We were required to work in group of four people, so we needed to write organized and readable code that can be easily explained to and shared with others, and we also should be ablt to work on the code that are developed by other people.
    
    We only had several weeks to design and implement our program while we had other school works to do. Thus, Our ability of intense coding and debugging were greatly enhanced by completing this project.  
    
    
2. What is the project about?
    The purpose of this project is to desigin a system that provides computer support to a warehousing company. This program should be able to track the status of the orders from the customer, provide computer support for warehousing workers and keep track of inventory levels in the warehouse. Thus, the program should be able to distinguish different types of inputs(orders from customer, actions from worker) and generate correct outputs(order status, level of inventory, tell workers where to go, what to do......). In reality, the program might communicate with workers and customes by receving and sending data through barcode reader or website, but, in this project, the input and output of the system are simplified to lines of texts that are stored in txt files. This simplification helps us to focus on the processing part of the program.    
    
3. Program Unit Tests:

    More than 90% of the code is covered by the tests. We've used 2 tools to accomplish it:
    
    1. Mockito:
    A third-party Java Framework Library that allows us to create mock objects for testing dependency-heavy classes, in our case WarehouseController and WarehouseSystem
    2. JUnit 4: 
    An Eclipse built-in library that allows us to consolidate all of our tests into a test suite, which we've called "AllTest.java"
               

4. Details about How the program works?
    1. To make the program organized and readable, we create OrderManager, WarehousPicking and StorageManager Classes that interact with other classes to handle different part of work. 
    
    2. Recieve and translate order
    The program recieve the order from input and send it to OrderManager. OrderManager will translate the order (using translation.csv) from text to SKU number which the warehouse uses to represent product. The OrderManager will hold the order until there is certain amount of Orders recieved.
    
    3. Generate Picking Request. 
    When certain amount of orde is recieved, the OrderManager will send data to WarehousePicking to generate PickingRequest. The WarehousePicking will assign PickingRequest a Picker(a type of Worker) if the Picker is ready. The WarehousePicking will also get the location of the product(using traversal_table.csv) and tell the Picker where to pick the product. If no worker is ready, the WarehousePicking will hold the PickingRequest in a waitlist.   
    
    4. Picker(a type of Worker) picks product.
    Picker will notify the system when he picks a product by scanning the SKU number on the product. System will call StorageManager to modify the inventory.
    
    5. Sequencer sequance the product.
    
    
    
Below are contents from help.txt:

cd group_0371/project
javac -cp src src/warehousesystem/Simulation.java
java -cp src warehousesystem/Simulation 16orders.txt
java -cp src warehousesystem/Simulation eventPickerRescan.txt
java -cp src warehousesystem/Simulation eventSequencerRescan.txt
java -cp src warehousesystem/Simulation eventLoaderRescan.txt
java -cp src warehousesystem/Simulation eventOutOfInventory.txt
java -cp src warehousesystem/Simulation eventSetReadyBeforeFinished.txt
java -cp src warehousesystem/Simulation eventReplenisherRescan.txt


Running Our Program:
	- Using the Terminal:
		1. Simply copy and paste the command lines above to run our program. The main method in Simulation.java will run the program simulation.
		2. After navigating to our project directory (as shown with line 1), start by copying and pasting the second line as described above.
		3. Lines 3-9 will handle passing file path directory as arguments automatically, simply copy and paste any of the line to run the simulation with the respective files
		4. To run a custom input file, use the format shown on line 3 and simply replace the parameter with the name of the new input file

	- Using Eclipse IDE:
		I. Create a Java Project for the program:
			1. Create a new workspace in Eclipse if needed
			2. Using the Eclipse toolbar, create a new Java Project: File -> New -> Java Project
			3. Rename the project as needed
			4. Uncheck "Use Default Location"
			5. Click Browse and navigate to the directory "group_0371/project", then click open
			6. Click "Finish" and the program will be accessible as a Java Project in Eclipse

		II: Installing the necessary plugins for JUnit Tests
			1. Locate the Java Project in Project Explorer or Package Explorer
			2. Right click on the project header
			3. Select Build Path -> Configure Build Path -> Libraries
			A) JUnit 4
				1. Click on "Add Library"
				2. Select JUnit and continue
				3. Select JUnit4 and finish
				4. Apply the changes and press "Ok"
			B) Mockito
				1. Click on Add External JARs
				2. Locate "mockito-all-2.0.2-beta.jar" in the project directory
				3. Apply the changes and press "Ok"

		III: Running the program with input files
			1. Open Simulation.java from the Java Project
			2. Using the Eclispe toolbar, select Run -> Run Configurations…
			3. Ensure the Main Class is warehousesystem.Simulation
			4. Select the tab "(x) = Arguments"
			5. In "Program Arguments", add in "16orders.txt" or the file name of the input file desired
			6. Select Run to run the program
			7. The Program's log outputs will also be shown in the Eclipse console
            
Program Outputs:
	After the program runs, it will create 3 new files that display the final results.
		I) log.txt: 
			A text file which contains the information recorded by WarehouseSystem's logger. The log contains information on:
				- Input read from Input Files
				- Simulation Events from Processing Input
				- Information exchanged between classes, such as messages between Barcode Readers and the Warehouse System
				- Simulation Events from Worker actions
				- Errors during simulation
				- Other information recorded by classes
		
		II) final.csv:
			A CSV file which records the state of all storages in the warehouse that are not full, in the same forma as initial.csv

		III) order.csv:
			A CSV file that records all orders completed and loaded onto the truck in the simulation. Uses the format Colour, Model




Project Overview:
	- List of Files in the Project Directory:
		> 16orders.txt: The default input file which processes 16 orders with no errors
		> design.pdf: The UML representation of our project files
		> eventLoaderRescan.txt: An alternate input file. Details described below
		> eventOutOfInventory.txt: An alternate input file. Details described below
		> eventPickerRescan.txt: An alternate input file. Details described below
		> eventReplenisherRescan.txt: An alternate input file. Details described below
		> eventSequencerRescan.txt: An alternate input file. Details described below
		> eventSetReadyBeforeFinished.txt: An alternate input file. Details described below
		> help.txt: A text file containing information pertaining to the project, as you've probably noticed by now
		> initial.csv: A CSV file detailing the initial state of the warehouse used for the simulation
		> testEmpty.txt: A test file used for testing the I/O by CsvToolsText.java
		> testMultiple.txt: A test file used for testing the I/O by CsvToolsText.java
		> translation.csv: A CSV file detailing SKU numbers of items used for the simulation
		> traversal_table.csv: A CSV file detailing location of storages used for the simulation
		> mockito-alla-2.02-beta.jar: The jar file of the Mockito Library we used for testing

	- Customised Input File Format:
		- Worker events process SKU numbers instead of the worker's process order
		- Worker events use the following format:
			[WorkerType] [Action] [SKU]
			WorkerType: denotes the type of the worker, such as Picker, Sequencer, Loader etc.
			Action: denotes the action taken by the worker, using past tense and lower case, such as "picked", "loaded", etc.
			SKU: the SKU string of which the worker will act on
		- New/Modified Worker Actions:
			> ready: used to denote a worker being available to accept new orders
			> rescanned: 
				i) Used by Pickers to return wrong fascias
				ii) Used by Sequencers to double check picking requests
				iii) Used by Loaders to double check picking requests
				iv) Used by Replenishers to replenish the correct storage if error occured
			> replenished: used to denote Replenishers resupplying a storage
			> finished: used to denote a worker completing their assigned work, which for a loader designates loading a picking request onto the truck
	
	- Custom Input Files:
		We have created some input files which contains errors during the simulation. These errors can be seen as Warning events in log.txt
		I) eventPickerRescan.txt:
			> In this file a Picker picks the wrong fascia as needed
				- Line 9: Picker Alice picked a fascia with SKU 12 instead of 13. Noted in Log Number 31
				- Line 10: Picker Alice returned the fascia with SKU 12. Noted in Log Number 34

		II) eventSequencerRescan.txt:
			> In this file a Sequencer finds error in the fascias and discards the inventory
				- Line 19: Sequencer Sue finds an error when sequencing a fascia with SKU 23 instead of 21. Noted in Log Number 66
					   Sequencer Sue requests to discard the wrong fascias, noted in Log Number 67
				- Line 26: Sequencer Sue allowed to discard the wrong items. Noted in Log Number 82
				- Line 42: Sequencer Sue finds an error when sequencing a fascia with SKU 3 instead of 13. Noted in Log Number 136
				- Line 43: Sequencer Sue rescans the items and corrects the mistake. Noted in Log Number 139

		III) eventLoaderRescan.txt:
			> In this file a Loader finds error while loading the fascias
				- Line 36: Loader Bill finds an error when loading picking request 1 (Occured in Line 33 where correct SKU should be 38). Noted in Log Number 105
				- Line 37: Loader Bill rescans and corrects the error. Noted in Log Number 107

		IV) eventSetReadyBeforeFinished.txt:
			> In this file workers are attempted to be set to a ready status when they have not completed their work
				- Line 10: Picker Alice was set to ready status before completing work. Noted in Log Number 35
				- Line 23: Sequencer Sue was set to ready status before completing work. Noted in Log Number 74
				- Line 35: Loader Bill was set to ready status before completing work. Noted in Log Number 102

		V) eventOutOfInventory.txt:
			> In this file a picker tries to retrieve an item from an empty storage
				- Line 44: Picker Alice attempted to retrieve a fascia with SKU 11 when the storage is empty. Noted in Log Number 142

		VI) eventReplenisherRescan.txt:
			> In this file a replenisher resupplies the wrong storage
				- Line 37: Replenisher Ruby replenished the storage for SKU 12 instead of 11. Noted in Log Number 111
				- Line 38: Replenisher Ruby replenished the Storage for 11 properly. Noted in Log Number 113
