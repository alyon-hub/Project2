import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;


/* 
Name: Avriel Lyon
Course: CNT 4714 Fall 2022 
Assignment title: Project 2 – Multi-threaded programming in Java 
Date:  October 9, 2022 

Class:  facility
*/ 

public class facility {
	
    //configure variables
    static int MAX = 10;
    
    //arrays
    private static int configArray[] = new int[MAX]; //stores the integers from config.txt
    private static conveyor conveyorObjectArray[] = new conveyor[MAX]; //stores the conveyor objects
    private static station stationObjectArray[] = new station[MAX]; //stores the station objects

    public static void main(String[] args) {
    	//create the thread pool
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX);

        File setupFile = new File("config.txt");
        Scanner aScanner;
        
        System.out.println("\n * * * * * * * * * * * THERE ARE PACKAGES TO MOVE * * * PACKAGE MANAGEMENT FACILITY SIMULATION STARTS * * * * * * * * * * * \n\n");
        
        try {
            aScanner = new Scanner(setupFile);
            
            //read in the number of stations
            int numberOfStations = Integer.parseInt(aScanner.nextLine());
            
            //add in values to the configArray
            for (int i = 0; i < numberOfStations; i++) {
                configArray[i] = Integer.parseInt(aScanner.nextLine());
            }

            aScanner.close();

            //create the conveyor objects
            for (int i = 0; i < conveyorObjectArray.length; i++) {
                conveyorObjectArray[i] = new conveyor(i);
            }
            
            System.out.println("The parameters for this simulation run are:\n");
            
            //create the station objects and execute thread pool
            for (int i = 0; i < numberOfStations; i++) {
                int workload = configArray[i];
                int inputConveyorNumber, outputConveyorNumber;
                
                System.out.println("Routing Station " + i + " Has Total Workload Of " + workload + " Package Groups.");
                
                //set the input and output conveyor number
                //station 0 always uses the first and last conveyor
                if (i == 0) {
                    inputConveyorNumber = 0;
                    outputConveyorNumber = numberOfStations - 1;
                } else {
                    inputConveyorNumber = i - 1;
                    outputConveyorNumber = i;
                }
                
                //create the station object
                //pass through:
                //the station number (i)
                //the workload
                //the input and output conveyor objects
                stationObjectArray[i] = new station(i, workload, conveyorObjectArray[inputConveyorNumber], conveyorObjectArray[outputConveyorNumber]);
                threadPool.execute(stationObjectArray[i]);
            }
            
            //application shutdown
            threadPool.shutdown();
            
            //needed to wait to display the last print
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



/* 
Name: Avriel Lyon
Course: CNT 4714 Fall 2022 
Assignment title: Project 2 – Multi-threaded programming in Java 
Date:  October 9, 2022 

Class:  facility
*/ 


        System.out.println("\n * * * * * * * * * * * ALL WORKLOADS COMPLETE * * * PACKAGE MANAGEMENT FACILITY SIMULATION ENDS * * * * * * * * * * * \n");
    }

}