package lifts;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Beatriz
 */
public class StopGateway extends UnicastRemoteObject implements InterfaceGateway{
    
    private boolean isClosed = false;

    public StopGateway() throws RemoteException {
    }

    public synchronized void look() {
        while (isClosed) {
            try {
                wait();
            } catch (InterruptedException ie) {
                System.out.println("InterruptedException caught in StopGateway look()");
            }
        }
    }

    public synchronized void open() {
        isClosed = false;
        notifyAll();
    }

    public synchronized void close() {
        isClosed = true;
    }
    
    public int checkPass(String pass){
        if(pass.equals("rascacielos2016")) return 0;
        else return 1;
    }
}
