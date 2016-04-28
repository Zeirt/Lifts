package lifts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread that gets a connection from a client and sends the status of the lifts
 * every second.
 *
 * @author Beatriz Cortés Sánchez
 */
public class MonitorThread extends Thread {

    private Socket connection;
    private DataOutputStream output;
    private DataInputStream input;
    private Controller c;
    private Lift l1;
    private Lift l2;

    /**
     * Constructor of a MonitorThread
     *
     * @param connection to sent to
     * @param c controller reference
     * @param l1 lift1 reference
     * @param l2 lift2 reference
     */
    public MonitorThread(Socket connection, Controller c, Lift l1, Lift l2) {
        this.connection = connection;
        this.c = c;
        this.l1 = l1;
        this.l2 = l2;
        this.start();
    }

    /**
     * Sends the client information about the system every second.
     */
    @Override
    public void run() {
        try {
            //Open input-output channels
            input = new DataInputStream(connection.getInputStream());
            output = new DataOutputStream(connection.getOutputStream());

            //Start sending him data. This is a copy of printer's method
            while (!c.areMovementsExhausted()) {
                sendState(output);
                try {
                    sleep(1000);//every second send
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException caught in Printer run()");
                }
            }
            output.writeUTF("Lifts aren't going to move any more. Connection is going to end.");

            //Close connection
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(MonitorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends the client the status of the system.
     *
     * @param output
     */
    public void sendState(DataOutputStream output) {
        String toDrawL1;// | if not there. Else status + # + numPeople
        String toDrawL2;
        String toDrawButton;//Yes if pressed. No if not pressed
        String peopleToLeave;//Makes a list of people who want to get off at that floor
        String destList = "";
        try {
            output.writeUTF("Floor:  Lift1:   Lift2:   ButtonPressed?:    Destination: ");
            for (int i = 20; i >= 0; i--) {
                if (l1.getLiftLocation() == i) {
                    if (l1.getStatus() != 3) {
                        toDrawL1 = "" + l1.getStatusChar() + "#" + l1.getPeopleInside();
                    } else {
                        toDrawL1 = "" + l1.getStatusChar();
                    }
                } else {
                    toDrawL1 = "|";
                }
                if (l2.getLiftLocation() == i) {
                    if (l2.getStatus() != 3) {
                        toDrawL2 = "" + l2.getStatusChar() + "#" + l2.getPeopleInside();
                    } else {
                        toDrawL2 = "" + l2.getStatusChar();
                    }
                } else {
                    toDrawL2 = "|";
                }
                if (l1.isWorking()) {
                    if (l1.isRideCalled(i)) {
                        toDrawButton = "Yes";
                    } else {
                        toDrawButton = "No";
                    }
                } else if (l2.isRideCalled(i)) {
                    toDrawButton = "Yes";
                } else {
                    toDrawButton = "No";
                }
                if (l1.isWorking()) {
                    destList = l1.getListOfPeopleToStop(i);
                } else {
                    destList = l2.getListOfPeopleToStop(i);
                }
                output.writeUTF(i + "\t" + toDrawL1 + "\t" + toDrawL2 + "\t\t" + toDrawButton + "\t\t" + destList);
            }
        } catch (IOException ex) {
            System.out.println("IOException caught in Monitor sendState()");
        }
    }
}
