package lifts;

/**
 * Spawns in a floor and calls the lift to go to destination.
 *
 * @author Beatriz Cortés Sánchez
 */
public class Person extends Thread {

    private String id;
    private int position;
    private int destination;
    private Controller controller;
    private String liftRef;

    /**
     * Constructor of Person.
     *
     * @param id identifier of person
     * @param position starting floor
     * @param destination floor person wants to go to
     * @param controller reference to controller
     */
    public Person(String id, int position, int destination, Controller controller) {
        this.id = id;
        this.position = position;
        this.destination = destination;
        this.controller = controller;
        liftRef = "";
        this.start();
    }

    /**
     * Position of the person is changed to proper floor
     *
     * @param position they're now in
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Ask for this person's destination
     *
     * @return destination
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Ask for a person's ID
     *
     * @return id
     */
    public String getPersonId() {
        return id;
    }

    /**
     * Until it reaches the destination, a person will call the lift, enter it
     * when it arrives, tell the lift where they want to go and leave once the
     * elevator signals to.
     */
    public void run() {
        while (position != destination) {
            System.out.println("I want lift to pick me up in floor " + position);
            controller.call(position);
            System.out.println("I'm going to enter the elevator.");
            liftRef = controller.enterElevator(this);
            System.out.println("I want to go to floor " + destination);
            controller.callStop(destination, liftRef);
            System.out.println("I'm leaving the elevator.");
            controller.exitElevator(this, destination);
        }
    }

}
