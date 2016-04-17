package lifts;

/**
 * Spawns in a floor and calls the lift to go to destination.
 * @author Beatriz Cortés Sánchez
 */
public class Person extends Thread{
    
    private String id;
    private int position;
    private int destination;
    private Controller controller;
    
    /**
     * Constructor of Person. 
     * @param id identifier of person
     * @param position starting floor
     * @param destination floor person wants to go to
     * @param controller reference to controller
     */
    public Person(String id, int position, int destination, Controller controller){
        this.id = id;
        this.position = position;
        this.destination = destination;
        this.controller = controller;
        this.start();
    }
    
    /**
     * Position of the person is changed to proper floor
     * @param position they're now in
     */
    public void setPosition(int position){
        this.position = position;
    }
    
    /**
     * Get destination of person
     * @return floor they want to go to
     */
    public int getDestination(){
        return destination;
    }
    
    public void run(){
        while(position != destination){
            System.out.println("I want lift to pick me up in floor " + position);
            controller.call(position);
            System.out.println("I'm going to enter the elevator.");
            controller.enterElevator(this);
            System.out.println("I want to go to floor " + destination);
            controller.call(destination);
            System.out.println("I'm leaving the elevator.");
            controller.exitElevator(this);
            System.out.println("I'm in floor " + position);
        }
        System.out.println("I'm going to end.");
    }
    
    
}
