import java.util.concurrent.locks.ReentrantLock;

/* 
Name: Avriel Lyon
Course: CNT 4714 Fall 2022 
Assignment title: Project 2 – Multi-threaded programming in Java 
Date:  October 9, 2022 

Class:  conveyor
*/ 

public class conveyor extends ReentrantLock {
    /**
     * 
     */
    //configure variables
    private static final long serialVersionUID = 1L;
    public int conveyorNumber; //which conveyor it is
    private int owner; //who currently owns the lock
    public ReentrantLock conveyorLock = new ReentrantLock(); //define the lock

    //assigns the conveyor number
    public conveyor(int conveyorNumber) {
        this.conveyorNumber = conveyorNumber;
    }

    //try lock
    public boolean lockConveyor() {
        if (conveyorLock.tryLock()) {
            return true;
        }
        return false;
    }

    //unlocks the thread
    public void unlockConveyor() {
        conveyorLock.unlock();
    }

    //sets the owner value
    public void setOwnerName(int owner) {
        this.owner = owner;
    }

/* 
Name: Avriel Lyon
Course: CNT 4714 Fall 2022 
Assignment title: Project 2 – Multi-threaded programming in Java 
Date:  October 9, 2022 

Class:  conveyor
*/ 

    //gets the owner value
    public int getOwnerName() {
        return owner;
    }

}