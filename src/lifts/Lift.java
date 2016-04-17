package lifts;

import java.util.ArrayList;

/**
 * Goes up and down picking up people and leaving them in destination.
 * If it breaks, it stops in last floor and kicks everyone out until fixed.
 * @author Beatriz Cortés Sánchez
 */
public class Lift extends Thread{
    //Constant of max possible people inside
    private static final int MAX_PEOPLE = 8;
    //Status of the lift
    private static final int STOPPED = 0, GOING_UP = 1, GOING_DOWN = 2, BROKEN = 3;
    
    private String id;
    private Controller controller;
    private int status;
    private int lastDirection; //GOING_UP or GOING_DOWN
    private int position;
    private int nextDestination;
    private boolean doorsOpen;
    private int peopleInside;
    private boolean[] toStop = new boolean[21]; //list of requested stops
    private ArrayList<Person> people = new ArrayList<>();
    
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
        peopleInside = 0;
        status = STOPPED;//start stopped
        lastDirection = GOING_DOWN;
        doorsOpen = false;
        for(int i = 0; i < 21; i++){//initialize stops to false
            toStop[i] = false;
        }
    }
    
    /**
     * Set the lift's status to broken.
     */
    public void breakLift(){
        status = BROKEN;
    }
    
    /**
     * Set the lift's status to stopped if it was broken.
     */
    public void fixLift(){
        if(status == BROKEN) status = STOPPED;
    }
    
    /**
     * Signal incoming and outcoming people that doors are open.
     */
    public void openDoors(){
        doorsOpen = true;
        controller.raiseArrivalInFloor(position);
    }
    
    /**
     * Close doors of the elevator when incoming and outcoming people
     * have finished.
     */
    public void closeDoors(){
        doorsOpen = false;
        synchronized (controller){
            controller.notifyAll();
        }
    }
    
    /**
     * Get current floor of the lift
     * @return floor
     */
    public int getLiftLocation(){
        return position;
    }
    
    /**
     * Get quantity of people in the elevator
     * @return people
     */
    public int getPeopleInside(){
        return peopleInside;
    }
    
    /**
     * Get current status of elevator
     * @return 0 = STOPPED, 1 = GOING_UP, 2 = GOING_DOWN, 3 = BROKEN
     */
    public int getStatus(){
        return status;
    }
    
    /**
     * Get if call to that stop is called.
     * @param floor to look in
     * @return true if called, else false
     */
    public boolean isStopCalled(int floor){
        return toStop[floor];
    }
    
    /**
     * Get the array of stops of the elevator
     * @return array of stops
     */
    public boolean[] getStops(){
        return toStop;
    }
    
    /**
     * Set an array of stops for the elevator
     * @param stops must be a boolean[21] array
     */
    public void setStops(boolean[] stops){
        toStop = stops;
    }
    
    /**
     * Ask if elevator works at that moment.
     * @return true if it works. False if it's broken
     */
    public boolean isWorking(){
        if(status == BROKEN) return false;
        else return true;
    }
    
    /**
     * Ask if elevator is at full capacity.
     * @return true if full, else false
     */
    public boolean isFull(){
        if(peopleInside == MAX_PEOPLE) return true;
        else return false;
    }
    
    public boolean isEmpty(){
        if(peopleInside == 0) return true;
        else return false;
    }
    
    /**
     * Person requests a stop in a floor and waits there for lift
     * @param floor to wait in
     */
    public void requestFloor(int floor){
        System.out.println(id + " got a request to get to floor " + floor);
        synchronized(this){
            toStop[floor] = true;
            notifyAll();
        }
        controller.arriveInFloor(floor);
    }
    
    /**
     * Used by a person to enter the elevator and leave the floor
     */
    public synchronized void enter(Person p){
        peopleInside++;
        people.add(p);
        controller.leaveFloor(position);
    }
    
    /**
     * Used by a person to leave the elevator.
     */
    public synchronized void exit(Person p){
        peopleInside--;
        people.remove(p);
        controller.leaveFloor(position);
    }
    
    public void kickPeopleOut(){
        int notifyFloor;
        for(int i = 0; i < people.size(); i++){
            notifyFloor = people.get(i).getDestination();
            controller.raiseArrivalInFloor(notifyFloor);
        }
    }
    
    /**
     * Give the nearest destination up from lift.
     * @return new destination. If new destination is 21, it found nothing.
     */
    public int getNearestDestUp() {
        int auxPos = position;
        int foundPos = 21; //if returns 21, no destination up
        boolean found = false;
        while (auxPos < 21 && found == false) {
            if (toStop[auxPos] == true) {
                found = true;
                foundPos = auxPos;
            } else {
                auxPos++;
            }
        }
        return foundPos;
    }
    
     /**
     * Give the nearest destination down from lift.
     * @return new destination. If new destination is -1, it found nothing.
     */
    public int getNearestDestDown() {
        int auxPos = position;
        int foundPos = -1; //if returns -1, no destination down
        boolean found = false;
        while (auxPos > -1 && found == false) {
            if (toStop[auxPos] == true) {
                found = true;
                foundPos = auxPos;
            } else {
                auxPos--;
            }
        }
        return foundPos;
    }

    /**
     * Give the nearest destination to the lift. 
     * It won't stop until it finds something or the lift breaks.
     * @return nearest destination. If lift broke, result should be ignored.
     */
    public int getNearestDest() {
        int down = position;
        int up = position;
        int foundPos = 0;//placeholder. Will change to actual destination
        boolean found = false;//will notify when it's found something
        while (found == false && status != BROKEN) {
            if (down == 0) {
                down = position;
                if (down == 0) {//if still 0, do nothing
                }
                if (toStop[down] == true) {
                    found = true;
                    foundPos = down;
                }
            } else {
                down--;
                if (toStop[down] == true) {
                    found = true;
                    foundPos = down;
                }
            }
            if (!found) {
                if (up == 20) {
                    up = position;
                    if (up == 20) {//if still 0, do nothing
                    }
                    if (toStop[up] == true) {
                        found = true;
                        foundPos = up;
                    }
                } else {
                    up++;
                    if (toStop[up] == true) {
                        found = true;
                        foundPos = up;
                    }
                }
            }
        }
        return foundPos;
    }
    
    /**
     * Ask if there are stops currently asked.
     * @return true if at least 1. Else false
     */
    public boolean areThereStops(){
        for(int i = 0; i < 21; i++){
            if(toStop[i] == true) return true;
        }
        return false;
    }

    public void run(){
        while(true){
            switch(status){
                case STOPPED:{
                    System.out.println(id + " opening doors");
                    openDoors();
                    System.out.println(id + " closing doors");
                    closeDoors();
                    switch(lastDirection){
                        case GOING_UP: {
                            nextDestination = getNearestDestUp();
                            if (nextDestination == 21){//destination not valid
                                nextDestination = getNearestDest();
                            }
                            break;
                        }
                        case GOING_DOWN: {
                            nextDestination = getNearestDestDown();
                            if (nextDestination == -1){//destination not valid
                                System.out.println(id + " checking where to go");
                                nextDestination = getNearestDest();
                                System.out.println(id + " decided where to go");
                            }
                            break;
                        }
                    }
                    System.out.println(id + " going to move");
                    if(nextDestination > position) status = GOING_UP;
                    else status = GOING_DOWN;
                    break;
                }
                case GOING_UP: {
                    while(position != nextDestination && status != BROKEN) {
                        try {
                            sleep(500);
                        } catch (InterruptedException ex) {
                            System.out.println("InterruptedException caught in Lift " + id + " run() GOING_UP");
                        }
                        position++;
                        System.out.println(id + " moved to floor " + position);
                    }
                    lastDirection = GOING_UP;
                    toStop[position] = false;
                    status = STOPPED;
                    break;
                }
                case GOING_DOWN: {
                    while(position != nextDestination && status != BROKEN) {
                        try {
                            sleep(500);
                        } catch (InterruptedException ex) {
                            System.out.println("InterruptedException caught in Lift " + id + " run() GOING_DOWN");
                        }
                        position--;
                        System.out.println(id + " moved to floor " + position);
                    }
                    lastDirection = GOING_DOWN;
                    toStop[position] = false;
                    status = STOPPED;
                    break;
                }
                case BROKEN: {
                    kickPeopleOut();
                }
            }
        }
    }
}