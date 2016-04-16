package lifts;

/**
 * Handles events in a floor.
 * Keeps track of people waiting for the elevator and releases them when 
 * doors open, that is, an event is issued. 
 * Event also issued if elevator breaks.
 * @author Beatriz Cortés Sánchez
 */
public class EventBarrier {
    
    private int peopleWaiting;
    private boolean event;
    
    /**
     * Constructor of EventBarrier. Starts off empty with no events.
     */
    public EventBarrier(){
        peopleWaiting = 0;
        event = false;
    }
    
    /**
     * Person signals its arrival. They're waiting for an elevator
     */
    public synchronized void arrive(){
        peopleWaiting++;
        if(event){
            return;
        }
        while(!event){
            try{
                super.wait();//person will wait in controller's queue!
            }catch (InterruptedException ie){
                System.out.println("InterruptedException caught in EventBarrier arrive()");
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
        notifyAll();
        while(peopleWaiting != 0){
            try{
                super.wait();//lift will wait in controller's queue!
            }catch (InterruptedException ie){
                System.out.println("InterruptedException caught in EventBarrier raiseArrival()");
            }
        }
        event = false;
    }
    
    /**
     * Person signals they got off the barrier.
     */
    public synchronized void exit(){
        peopleWaiting--;
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
