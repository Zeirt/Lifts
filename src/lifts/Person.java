package lifts;

import java.io.PrintWriter;

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
    private PrintWriter printOut;

    /**
     * Constructor of Person.
     *
     * @param id identifier of person
     * @param position starting floor
     * @param destination floor person wants to go to
     * @param controller reference to controller
     * @param printOut reference for file printer
     */
    public Person(String id, int position, int destination, Controller controller, PrintWriter printOut) {
        this.id = id;
        this.position = position;
        this.destination = destination;
        this.controller = controller;
        this.printOut = printOut;
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
        while (position != destination && !controller.areMovementsExhausted()) {
            System.out.println("I'm " + id + " and I want lift to pick me up in floor " + position);
            printOut.println("I'm " + id + " and I want lift to pick me up in floor " + position);
            controller.call(position);
            System.out.println("I'm " + id + " and I'm going to enter the elevator.");
            printOut.println("I'm " + id + " and I'm going to enter the elevator.");
            liftRef = controller.enterElevator(this);
            System.out.println("I'm " + id + " and I want to go to floor " + destination);
            printOut.println("I'm " + id + " and I want to go to floor " + destination);
            controller.callStop(destination, liftRef);
            System.out.println("I'm " + id + " and I'm leaving the lift.");
            printOut.println("I'm " + id + " and I'm leaving the lift.");
            controller.exitElevator(this, destination, liftRef);
        }
    }

}
