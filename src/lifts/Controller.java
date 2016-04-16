package lifts;

import java.util.ArrayList;

/**
 * Manages both lifts.
 * Records calls from floors and manages breaking-fixing of the lifts.
 * @author Beatriz Cortés Sánchez
 */
public class Controller extends Thread{
    
    private Lift l1;
    private Lift l2;
    private ArrayList<Floor> floors;
    
    /**
     * Constructor of Controller. Needs floors and lifts
     * @param l1 first lift
     * @param l2 second lift
     * @param floors  array of floors
     */
    public Controller(Lift l1, Lift l2, ArrayList<Floor> floors){
        this.l1 = l1;
        this.l2 = l2;
        this.floors = floors;
    }
    
}
