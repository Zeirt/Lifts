package lifts;

/**
 * Handles events in a floor. Keeps track of people waiting for the elevator and
 * releases them when doors open, that is, an event is issued. This is only used
 * for people to get INSIDE the elevator.
 *
 * @author Beatriz Cortés Sánchez
 */
public class FloorBarrier {

    private int peopleWaiting;
    private boolean event;
    private Lift currentLift;

    /**
     * Constructor of FloorBarrier. Starts off empty with no events.
     */
    public FloorBarrier() {
        peopleWaiting = 0;
        event = false;
    }

    /**
     * Person signals its arrival. They're waiting for an elevator
     */
    public synchronized void arrive() {
        peopleWaiting++;
        //System.out.println("Person is now waiting for lift.");
        if (event) {
            return;
        }
        do{//don't move until event is issued PLUS the lift is not empty
            while (!event) {
                try {
                    this.wait();//person will wait in queue!
                } catch (InterruptedException ie) {
                    System.out.println("InterruptedException caught in FloorBarrier arrive()");
                }
            } 
        }while(currentLift.isFull());
    }

    /**
     * Event raised (elevator door opening).
     */
    public synchronized void raiseArrival(Lift l) {
        if (event) {
            return;
        }
        currentLift = l;
        event = true;
        notifyAll();
        while (peopleWaiting != 0 && !l.isFull()) {//leave when floor empty or lift full
            try {
                this.wait();//lift will wait in queue!
            } catch (InterruptedException ie) {
                System.out.println("InterruptedException caught in FloorBarrier raiseArrival()");
            }
        }
        event = false;
    }

    /**
     * Person signals they got off the barrier.
     */
    public synchronized void exit() {
        peopleWaiting--;
        if(currentLift.isFull() || peopleWaiting == 0){
            notifyAll();
        }
    }

    /**
     * Get people waiting on the floor.
     *
     * @return number of people waiting
     */
    public int getPeopleWaiting() {
        return peopleWaiting;
    }

}
