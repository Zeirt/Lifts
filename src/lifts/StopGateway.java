package lifts;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Gateway used to stop and start the lifts remotely.
 * Lifts are supposed to check every now and then if they can go ahead.
 * @author Beatriz Cortés Sánchez
 */
public class StopGateway extends UnicastRemoteObject implements InterfaceGateway{
    
    private boolean isClosed = false;

    /**
     * Constructor of StopGateway.
     * @throws RemoteException 
     */
    public StopGateway() throws RemoteException {
    }
    
    /**
     * Lift looks if the gateway is closed. 
     * If it is, it'll wait until it's opened. 
     */
    public synchronized void look() {
        while (isClosed) {
            try {
                wait();
            } catch (InterruptedException ie) {
                System.out.println("InterruptedException caught in StopGateway look()");
            }
        }
    }

    /**
     * Open the gateway and notify any lift waiting.
     */
    public synchronized void open() {
        isClosed = false;
        notifyAll();
    }

    /**
     * Close the gateway.
     */
    public synchronized void close() {
        isClosed = true;
    }
    
    /**
     * Check the password for a client to access the gateway.
     * @param pass to check
     * @return 0 if it's correct, 1 if it's not
     */
    public int checkPass(String pass){
        if(pass.equals("rascacielos2016")) return 0;
        else return 1;
    }
}
