package lifts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that acts as a server and connects to clents. Creates a MonitorThread
 * for each client connection to send status.
 *
 * @author Beatriz Cortés Sánchez
 */
public class Monitor extends Thread {

    private ServerSocket server;
    private Socket connection;
    private Controller c;
    private Lift l1;
    private Lift l2;

    /**
     * Constructor of Monitor. Starts automatically.
     *
     * @param c controller reference
     * @param l1 lift reference
     * @param l2 lift reference
     */
    public Monitor(Controller c, Lift l1, Lift l2) {
        this.c = c;
        this.l1 = l1;
        this.l2 = l2;
        this.start();
    }

    /**
     * Waits for a client. Create a new connection handler for every client it
     * gets.
     */
    @Override
    public void run() {
        int num = 0;
        try {
            server = new ServerSocket(5000); // Create socket Port 5000
            System.out.println("Starting server...");
            while (!c.areMovementsExhausted()) {
                // Wait for a connection
                connection = server.accept();
                num++;
                System.out.println("Connection no. " + num + " from: " + connection.getInetAddress().getHostName());
                new MonitorThread(connection, c, l1, l2);
            }
        } catch (IOException e) {
        }
    }
}
