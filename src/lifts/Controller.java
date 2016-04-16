package lifts;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages both lifts.
 * Records calls from floors and manages breaking-fixing of the lifts.
 * @author Beatriz Cortés Sánchez
 */
public class Controller extends Thread{
    
    private Lift l1;
    private Lift l2;
    private EventBarrier[] floors;
    
    /**
     * Constructor of Controller. Needs floors and lifts
     * @param l1 first lift
     * @param l2 second lift
     * @param floors  array of floors
     */
    public Controller(Lift l1, Lift l2, EventBarrier[] floors){
        this.l1 = l1;
        this.l2 = l2;
        this.floors = floors;
    }
    
    public void setL1(Lift l1){
        this.l1 = l1;
    }
    
    public void setL2(Lift l2){
        this.l2 = l2;
    }
    
    /**
     * Person arrives to wait in a certain floor.
     * @param floor  to wait in
     */
   public void arriveInFloor(int floor){
       floors[floor].arrive();
   }
   
   /**
    * Person gets out of floor.
    * @param floor to get out of
    */
   public void leaveFloor(int floor){
       floors[floor].exit();
   }
    
    /**
     * Raise event of lift arrival in a certain floor.
     * @param floor  to raise event in
     */
    public void raiseArrivalInFloor(int floor){
        floors[floor].raiseArrival();
    }
    
    /**
     * Person gets inside elevator.
     */
    public synchronized void enterElevator(){
        if(l1.isWorking()){
            l1.enter();
        }else l2.enter();
    }
    
    /**
     * Person gets out of elevator.
     */
    public synchronized void exitElevator(){
        if(!l1.isEmpty()){
            l1.exit();
        }else l2.exit();
    }
    
    /**
     * Call working elevator to a floor.
     * Person then waits in floor until it arrives.
     * @param floor 
     */
    public void call(int floor){
        Lift liftToUse;
        if(l1.isWorking()){
            liftToUse = l1;
        } else liftToUse = l2;
        synchronized (this){
            while(liftToUse.isFull()){
                try {
                    wait();//waits in controller
                } catch (InterruptedException ie) {
                    System.out.println("InterruptedException caught in Controller call()");
                }
            }
        }
        liftToUse.requestFloor(floor);
    }
    
     public void drawState() {
        String toDrawL1;// | if not there. Else status + # + numPeople
        String toDrawL2;
        String toDrawButton;//Yes if pressed. No if not pressed
        String peopleToLeave;//Makes a list of people who want to get off at that floor
        String destList = "";
        System.out.println("Floor:  Lift1:   Lift2:   ButtonPressed?:    Destination: ");
        for (int i = 20; i >= 0; i--) {
            if (l1.getLiftLocation() == i) {
                if (l1.getStatus() != 'N') {
                    toDrawL1 = "" + l1.getStatus() + "#" + l1.getPeopleInside();
                } else {
                    toDrawL1 = "" + l1.getStatus();
                }
            } else {
                toDrawL1 = "|";
            }
            if (l2.getLiftLocation() == i) {
                if (l2.getStatus() != 'N') {
                    toDrawL2 = "" + l2.getStatus() + "#" + l2.getPeopleInside();
                } else {
                    toDrawL2 = "" + l2.getStatus();
                }
            } else {
                toDrawL2 = "|";
            }
            if (l1.isWorking()) {
                if (l1.isStopCalled(i)) {
                    toDrawButton = "Yes";
                } else {
                    toDrawButton = "No";
                }
            } else if (l2.isStopCalled(i)) {
                toDrawButton = "Yes";
            } else {
                toDrawButton = "No";
            }
            /*if (l1.isWorking()) {
                destList = l1.getListOfPeopleToFloor(i);
            } else {
                destList = l2.getListOfPeopleToFloor(i);
            }*/
            System.out.println(i + "        " + toDrawL1 + "        " + toDrawL2 + "          " + toDrawButton + "           " + destList);
        }
    }
    
}
