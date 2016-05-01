package lifts;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server creating the registry for the controller client.
 * @author Beatriz Cortés Sánchez
 */
public class RMIController extends Thread{
    
    private StopGateway gateway;

    /**
     * Constructor of RMIController.
     * Starts at creation.
     * @param gateway reference
     */
    public RMIController(StopGateway gateway) {
        this.gateway = gateway;
        this.start();
    }

    /**
     * Creates the registry, binds the gateway object.
     */
    public void run() {
        try {
            //Keep in mind this is supposed to go as localhost.
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/ObjectGateway", gateway);

            System.out.println("Objet StopGateway registered");
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
