package lifts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.Random;

/**
 * Spawns 20 floors, a controller, two lifts, a printer and begins to spawn
 * people randomly on floors.
 *
 * @author Beatriz Cortés Sánchez
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //create PrintWriter
        PrintWriter printOut = null;
        try{
            printOut= new PrintWriter(new BufferedWriter(new FileWriter("liftEvolution.txt")));
        }catch(IOException ioe){
            System.out.println("IO error: " + ioe.getMessage());
        }
        StopGateway gateway = null;
        try {
            //Create gateway for the server
            gateway = new StopGateway();
        } catch (RemoteException ex) {
            System.out.println("RemoteException caught in Main main()");
        }
        //Create floors
        FloorBarrier[] floors = new FloorBarrier[21];
        for (int i = 0; i <= 20; i++) {
            floors[i] = new FloorBarrier();
        }
        //Create controller and lifts
        Controller c = new Controller(null, null, gateway, floors, printOut);
        Lift l1 = new Lift("l1", c);
        Lift l2 = new Lift("l2", c);
        //set lifts inside controller
        c.setL1(l1);
        c.setL2(l2);
        //break lift 2 and start lifts and controller
        l2.breakLift();
        l1.start();
        l2.start();
        c.start();
        //create and start printer
        Printer sd = new Printer(c, l1, l2, printOut);
        //Create and start monitor and controller server
        Monitor m = new Monitor(c, l1, l2);
        RMIController con = new RMIController(gateway);
        Random r = new Random();
        //make a random person every 0.5s - 2s
        for (int i = 1; i <= 100; i++) {
            new Person("P" + i, r.nextInt(20), r.nextInt(20), c, printOut);
            try {
                sleep(r.nextInt(2000 - 500) + 500);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException caught in Main main()");
            }
        }
        //There's a chance the lifts will stop moving before everyone has been spawned.
        //If this happens, floors will just get clogged, but the lifts will remain stopped.
        while(!c.areMovementsExhausted()){
            //Wait until the lifts are done to close the program forcefuly.
            try{
                sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException caught in Main main()");
            }
        }
        System.exit(0);
    }

}
