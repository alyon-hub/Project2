import java.util.Random;


/* 
Name: Avriel Lyon
Course: CNT 4714 Fall 2022 
Assignment title: Project 2 – Multi-threaded programming in Java 
Date:  October 9, 2022 

Class:  station
*/ 

public class station implements Runnable {
    //configure variables
    private int workload;
    private int inputConveyorNumber;
    private int outputConveyorNumber;
    private int stationNumber;
    private conveyor input;
    private conveyor output;
    private static Random gen = new Random(); //random number generator

    //assigns the station number
    public station(int stationNumber, int workload, conveyor input, conveyor output) {
        this.stationNumber = stationNumber;
        this.workload = workload;
        this.inputConveyorNumber = input.conveyorNumber;
        this.outputConveyorNumber = output.conveyorNumber;
        this.input = input;
        this.output = output;
    }

    //sleeps the thread for a random amount of time
    public void goToSleep() {
        try {
            Thread.sleep((gen.nextInt(500)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //where the station moves packages
    public void doWork() {
        System.out.println("\n * * * * * * Routing Station " + stationNumber + ":  * * * * * *  Holds locks on both input conveyor C" + inputConveyorNumber + " and output conveyor C" + outputConveyorNumber + ". * * * * * *\n");
        System.out.println("\n * * * * * * Routing Station " + stationNumber + ":  * * * * * *  CURRENTLY HARD AT WORK MOVING PACKAGES * * * * * * \n\n");
        System.out.println("\nRouting Station " + stationNumber + ": has " + workload + " package groups left to move.\n");
        
        //decrement the workload
        workload--;
        
        if (workload == 0) {
        	System.out.print("\n\n# # Station " + stationNumber + ": Workload successfully completed. * * Station " + stationNumber + " preparing to go offline. # #\n\n");
            return;
        }
        
        goToSleep();
        return;
    }

/* 
Name: Avriel Lyon
Course: CNT 4714 Fall 2022 
Assignment title: Project 2 – Multi-threaded programming in Java 
Date:  October 9, 2022 

Class:  station
*/ 

    //acquiring the locks 
    public void run() {
        System.out.println("\n\n % % % % % ROUTING STATION " + stationNumber + ": Coming Online - Initializing Conveyors % % % % % \n\n");
        System.out.println("Routing Station " + stationNumber + ": Input conveyor set to conveyor number C" + inputConveyorNumber + ".");
        System.out.println("Routing Station " + stationNumber + ": Output conveyor set to conveyor number C" + outputConveyorNumber + ".");
        System.out.println("Routing Station " + stationNumber + ": has a Total Workload of " + workload + " Package Groups.");
        
        while (workload != 0) {
        	
        	
            //try lock
            if (input.lockConveyor()) {
            	System.out.println("Routing Station " + stationNumber + ": Entering Lock Aquisition Phase.");
                System.out.println("Routing Station " + stationNumber + ": holds lock on input conveyor C" + inputConveyorNumber + ".");
                input.setOwnerName(stationNumber);
                
                //try lock and do work once both locks are obtained
                if (output.lockConveyor()) {
                	System.out.println("Routing Station " + stationNumber + ": holds lock on output conveyor C" + outputConveyorNumber + ".");
                    output.setOwnerName(stationNumber);
                    doWork();
                    
                    //release locks to avoid issues 
                    System.out.println("Routing Station " + stationNumber + ": Entering Lock Release Phase.");
                    System.out.println("Routing Station " + stationNumber + ": unlocks/releases input conveyor C" + inputConveyorNumber + ".");
                    System.out.println("Routing Station " + stationNumber + ": unlocks/releases output conveyor C" + outputConveyorNumber + ".");
                    
                    output.unlockConveyor();
                    input.unlockConveyor();
                }
                //can't obtain both locks so release the input lock
                else {
                    System.out.println("Routing Station " + stationNumber + ": unable to lock output conveyor C" + outputConveyorNumber + ". SYNCHRONIZATION ISSUE: Station " + output.getOwnerName() + " currently holds the lock on output conveyor C" + outputConveyorNumber + " - releasing lock on input conveyor C" + inputConveyorNumber + ".\n\n");
                    System.out.println("Routing Station " + stationNumber + ": unlocks/releases input conveyor C" + inputConveyorNumber + ".");
                    
                    input.unlockConveyor();

                    //sleep the thread to try again later
                    goToSleep();
                }
            } 
        }
        
        System.out.print("\n\n @ @ @ @ @ @ ROUTING STATION " + stationNumber + ": OFF LINE  @ @ @ @ @ @\n\n");
        return;
    }
}