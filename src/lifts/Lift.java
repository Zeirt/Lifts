package lifts;

import java.util.ArrayList;

/**
 * Goes up and down picking up people and leaving them in destination.
 * If it breaks, it stops in last floor and kicks everyone out until fixed.
 * @author Beatriz Cortés Sánchez
 */
public class Lift {
    //Constant of max possible people inside
    private static final int MAX_PEOPLE = 8;
    //Status of the lift
    private static final int STOPPED = 0, GOING_UP = 1, GOING_DOWN = 2, BROKEN = 3;
    
    private String id;
    private Controller controller;
    private ArrayList<Person> people = new ArrayList<>();
    private int status;
    private int lastDirection; //GOING_UP or GOING_DOWN
    private int position;
    private int nextDestination;
    private boolean[] toStop = new boolean[21]; //list of requested stops
    
    /**
     * Constructor of Lift. Starts in floor 0, stopped.
     * @param id identifier of lift
     * @param controller  controller of lift
     */
    public Lift(String id, Controller controller){
        this.id = id;
        this.controller = controller;
        position = 0;
        nextDestination = 0;
        status = STOPPED;//start stopped
        lastDirection = GOING_DOWN;
        for(int i = 0; i < 21; i++){//initialize stops to false
            toStop[i] = false;
        }
    }
    
}
