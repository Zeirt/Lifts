package lifts;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Beatriz Cortés Sánchez
 */
public class RMIController extends Thread{
    
    private StopGateway gateway;

    public RMIController(StopGateway gateway) {
        this.gateway = gateway;
        this.start();
    }

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
