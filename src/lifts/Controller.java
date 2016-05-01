package lifts;

import java.io.PrintWriter;
import java.util.Random;

/**
 * Manages both lifts. Records calls from floors and manages breaking-fixing of
 * the lifts.
 *
 * @author Beatriz Cortés Sánchez
 */
public class Controller extends Thread {

    private Lift l1;
    private Lift l2;
    private FloorBarrier[] floors;
    private LiftBarrier bl1;
    private LiftBarrier bl2;
    private PrintWriter printOut;
    private int movementsDone;//will count until 100 to finish

    /**
     * Constructor of Controller. Needs floors and lifts
     *
     * @param l1 first lift
     * @param l2 second lift
     * @param floors array of floors
     * @param printOut reference for file printer
     */
    public Controller(Lift l1, Lift l2, FloorBarrier[] floors, PrintWriter printOut) {
        this.l1 = l1;
        this.l2 = l2;
        this.floors = floors;
        bl1 = new LiftBarrier();
        bl2 = new LiftBarrier();
        this.printOut = printOut;
        movementsDone = 0;
    }

    /**
     * Increase the movements done by 1
     */
    public void movementUp() {
        movementsDone++;
    }

    /**
     * Check if lift should terminate
     *
     * @return true if movements are >100, else false
     */
    public boolean areMovementsExhausted() {
        return movementsDone >= 100;
    }

    /**
     * Set a lift as l1
     *
     * @param l1 to use
     */
    public void setL1(Lift l1) {
        this.l1 = l1;
    }

    /**
     * Set a lift as l2
     *
     * @param l2 to use
     */
    public void setL2(Lift l2) {
        this.l2 = l2;
    }

    /**
     * Person arrives to wait in a certain floor.
     *
     * @param floor to wait in
     */
    public void arriveInFloor(int floor) {
        floors[floor].arrive();
    }

    /**
     * Person arrives to wait inside a lift.
     *
     * @param floor they want to get off at
     */
    public void arriveInLift(int floor) {
        if (l1.isWorking()) {
            bl1.arrive(floor);
        } else {
            bl2.arrive(floor);
        }
    }

    /**
     * Person gets out of floor.
     *
     * @param floor to get out of
     */
    public void leaveFloor(int floor) {
        floors[floor].exit();
    }

    /**
     * Person gets out of lift
     *
     * @param id of lift
     * @param floor they wanted to get off at
     */
    public void leaveLift(String id, int floor) {
        if (id.equals("l1")) {
            bl1.exit(floor);
        } else {
            bl2.exit(floor);
        }
    }

    /**
     * Raise event of lift arrival in a certain floor. People can now get
     * inside.
     *
     * @param floor to raise event in
     */
    public void raiseArrivalInFloor(int floor, Lift l) {
        floors[floor].raiseArrival(l);
    }

    /**
     * Raise event of lift arrival in a floor. People can now go outside.
     *
     * @param id of lift
     * @param floor to raise event in
     */
    public void raiseArrivalInLift(String id, int floor) {
        if (id.equals("l1")) {
            bl1.raiseArrival(floor);
        } else {
            bl2.raiseArrival(floor);
        }
    }

    /**
     * Raise event of broken lift. Everyone must leave.
     *
     * @param id of lift
     */
    public void raiseBrokenInLift(String id) {
        if (id.equals("l1")) {
            bl1.raiseBroken();
        } else {
            bl2.raiseBroken();
        }
    }
    
    /**
     * Raise event of finishing lift. Everyone must leave
     * @param id  of lift
     */
    public void raiseFinishInLift(String id){
        if (id.equals("l1")) {
            bl1.raiseEnding();
        } else {
            bl2.raiseEnding();
        }
    }

    /**
     * Person gets inside elevator.
     *
     * @param p to go inside
     * @return id of elevator they're in.
     */
    public synchronized String enterElevator(Person p) {
        Lift workingLift = getWorkingLift();
        while(workingLift.isFull()){
            try {
                    this.wait();
                    workingLift = getWorkingLift();//check again
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException caught in Controller enterElevator()");
            }
        }
        workingLift.enter(p);
        return workingLift.getLiftId();
    }
    
    /**
     * Gives back the lift working at that moment
     * @return lift
     */
    public Lift getWorkingLift(){
        if(l1.isWorking()) return l1;
        else return l2;
    }

    /**
     * Person gets out of elevator.
     */
    public synchronized void exitElevator(Person p, int floor, String lift) {
        if (lift.equals("l1")) {
            l1.exit(floor, p);
            p.setPosition(l1.getLiftLocation());
        } else {
            l2.exit(floor, p);
            p.setPosition(l2.getLiftLocation());
        }
    }

    /**
     * Call working elevator to a floor. Person then waits in floor until it
     * arrives.
     *
     * @param floor
     */
    public void call(int floor) {
        Lift liftToUse;
        if (l1.isWorking()) {
            liftToUse = l1;
        } else {
            liftToUse = l2;
        }
        synchronized (this) {
            while (liftToUse.isFull()) {
                try {
                    wait();//waits in controller
                } catch (InterruptedException ie) {
                    System.out.println("InterruptedException caught in Controller call()");
                }
            }
        }
        //check again. Lift may have changed
        if (l1.isWorking()) {
            liftToUse = l1;
        } else {
            liftToUse = l2;
        }
        liftToUse.requestFloor(floor);
    }

    /**
     * Call an elevator to stop in a floor. Person waits inside the elevator
     * until it arrives or breaks.
     *
     * @param floor to stop in
     * @param liftToUse reference of lift they're in
     */
    public void callStop(int floor, String liftToUse) {
        Lift l;
        if (liftToUse.equals("l1")) {
            l = l1;
        } else {
            l = l2;
        }
        l.requestStop(floor);
    }

    /**
     * After waiting between 5 or 7 seconds, it will break a lift and fix the
     * other, swapping the rides left.
     */
    public void run() {
        Random r = new Random();
        boolean[] stopSwap;
        while (!areMovementsExhausted()) {
            try {//Wait between 5 or 7 s
                sleep(r.nextInt(7000 - 5000) + 5000);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught in Controller run()");
            }
            if (l1.isWorking()) {
                System.out.println("Lift1 breaks. L2 now in operation");
                printOut.println("Lift1 breaks. L2 now in operation");
                l1.breakLift();
                try{
                    sleep(300);//wait for a bit for it to kick everyone out
                }catch (InterruptedException e) {
                System.out.println("InterruptedException caught in Controller run()");
                }
                stopSwap = l1.getRides();
                l2.setRides(stopSwap);
                l2.fixLift();
                bl2.fixLift();
            } else {
                System.out.println("Lift2 breaks. L1 now in operation");
                printOut.println("Lift2 breaks. L1 now in operation");
                l2.breakLift();
                try{
                    sleep(300);//wait for a bit for it to kick everyone out
                }catch (InterruptedException e) {
                System.out.println("InterruptedException caught in Controller run()");
                }
                stopSwap = l2.getRides();
                l1.setRides(stopSwap);
                l1.fixLift();
                bl1.fixLift();
            }
        }
        System.out.println("Lifts aren't going to move any more.");
        printOut.println("Lifts aren't going to move any more.");
    }
}
