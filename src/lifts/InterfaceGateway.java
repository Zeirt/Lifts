package lifts;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines an interface. Used so it can be accessed remotely.
 * @author Beatriz Cortés Sánchez
 */
public interface InterfaceGateway extends Remote{
    /**
     * Lift looks if the gateway is closed. 
     * If it is, it'll wait until it's opened.
     * @throws RemoteException 
     */
    void look() throws RemoteException;
    /**
     * Open the gateway and notify any lift waiting.
     * @throws RemoteException 
     */
    void open() throws RemoteException;

    /**
     * Close the gateway.
     * @throws RemoteException 
     */
    void close() throws RemoteException;
    
    /**
     * Check the password for a client to access the gateway.
     * @param pass to check
     * @return 0 if it's correct, 1 if it's not
     * @throws RemoteException 
     */
    int checkPass(String pass) throws RemoteException;
}
