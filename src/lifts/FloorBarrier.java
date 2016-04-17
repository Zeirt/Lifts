package lifts;

/**
 * Handles events in a floor.
 * Keeps track of people waiting for the elevator and releases them when 
 * doors open, that is, an event is issued. 
 * There is one FloorBarrier by floor and ONLY handles people wanting to get in.
 * @author Beatriz Cortés Sánchez
 */
public class FloorBarrier {
    
    private int peopleWaiting;
    private boolean event;
    
    /**
     * Constructor of FloorBarrier. Starts off empty with no events.
     */
    public FloorBarrier(){
        peopleWaiting = 0;
        event = false;
    }
    
    /**
     * Person signals its arrival. They're waiting for an elevator
     */
    public synchronized void arrive(){
        peopleWaiting++;
        System.out.println("Person is now waiting for lift.");
        if(event){
            return;
        }
        while(!event){
            try{
                super.wait();//person will wait in this queue
            }catch (InterruptedException ie){
                System.out.println("InterruptedException caught in FloorBarrier arrive()");
            }
        }
    }
    
    /**
     * Event raised (elevator door opening).
     */
    public synchronized void raiseArrival(){
        if(event){
            return;
        }
        event = true;
        System.out.println("Lift has arrived");
        notifyAll();
        while(peopleWaiting != 0){
            try{
                super.wait();//lift will wait in this queue
            }catch (InterruptedException ie){
                System.out.println("InterruptedException caught in FloorBarrier raiseArrival()");
            }
        }
        event = false;
    }
    
    /**
     * Person signals they got off the barrier.
     */
    public synchronized void exit(){
        peopleWaiting--;
        System.out.println("Person is not waiting anymore.");
        if(peopleWaiting == 0){//if nobody's waiting to get in, tell
            notifyAll();
        }
    }
    
    /**
     * Get people waiting on the floor.
     * @return number of people waiting
     */
    public int getPeopleWaiting(){
        return peopleWaiting;
    }
    
}
