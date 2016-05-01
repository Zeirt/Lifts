package lifts;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Beatriz
 */
public interface InterfaceGateway extends Remote{
    void look() throws RemoteException;

    void open() throws RemoteException;

    void close() throws RemoteException;
    
    int checkPass(String pass) throws RemoteException;
}
