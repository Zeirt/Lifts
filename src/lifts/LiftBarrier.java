package lifts;

import java.util.ArrayList;

/**
 * Handles events in a lift. Keeps track of people waiting for the elevator and
 * releases them when doors open and one of the conditions are met (in floor
 * requested OR lift broken) This is only used for people to get OUTSIDE the
 * elevator.
 *
 * @author Beatriz Cortés Sánchez
 */
public class LiftBarrier {

    private ArrayList<Integer> peopleWaiting;
    //person waiting will fill out with their stop.
    private boolean event;
    private int floorReached;//people look at this flag to see if they should leave
    private boolean broken;//broken flag. Will force everyone out

    /**
     * Constructor of LiftBarrier. Starts off empty with no events.
     */
    public LiftBarrier() {
        peopleWaiting = new ArrayList<>();
        event = false;
        floorReached = 0;
        broken = false;
    }

    /**
     * Person signals its arrival. They're waiting for their stop
     *
     * @param stop to get off at
     */
    public synchronized void arrive(int stop) {
        peopleWaiting.add(stop);
        System.out.println("Person is now waiting for stop " + stop);
        if ((event && (floorReached == stop)) || (event && broken)) {
            return;
        }
        while ((!event && (floorReached != stop)) || (!event && !broken)) {//leave when they get to stop OR lift broken
            try {
                this.wait();//person will wait in queue!
            } catch (InterruptedException ie) {
                System.out.println("InterruptedException caught in LiftBarrier arrive()");
            }
        }
    }

    /**
     * Event raised (arrival to a destination).
     */
    public synchronized void raiseArrival(int stop) {
        if (event) {
            return;
        }
        event = true;
        floorReached = stop;
        notifyAll();
        while (peopleGettingOffThisFloor() != 0) {//will wait until everyone who wanted to get off has left
            try {
                this.wait();//lift will wait in queue!
            } catch (InterruptedException ie) {
                System.out.println("InterruptedException caught in LiftBarrier raiseArrival()");
            }
        }
        event = false;
    }

    /**
     * Event raised (lift has broken).
     */
    public synchronized void raiseBroken() {
        if (broken) {
            return;
        }
        event = true;
        broken = true;
        notifyAll();
        while ((!peopleWaiting.isEmpty()) && (!broken)) {//will wait for everyone to leave and then wait to be fixed
            try {
                this.wait();
            } catch (InterruptedException ie) {
                System.out.println("InterruptedException caught in LiftBarrier raiseBroken()");
            }
        }
        event = false;
    }

    /**
     * Person signals they got off the barrier.
     *
     * @param floor they wanted to get off at
     */
    public synchronized void exit(int floor) {
        peopleWaiting.remove((Integer) floor);
        if (peopleGettingOffThisFloor() == 0) {//if nobody's waiting to get out, tell
            notifyAll();
        }
    }

    /**
     * Called by controller to signal lift has been fixed
     */
    public synchronized void fixLift() {
        broken = false;
        notifyAll();
    }

    /**
     * Get remaining people who want to get out in that floor. Internal use only
     *
     * @return number of people who want to leave there
     */
    private int peopleGettingOffThisFloor() {
        int result = 0;
        for (int i = 0; i < peopleWaiting.size(); i++) {
            if (peopleWaiting.isEmpty()) {
                return 0;
            }
            if (peopleWaiting.get(i) == floorReached) {
                result++;
            }
        }
        return result;
    }
}
