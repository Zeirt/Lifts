package Clients;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Connects to the Monitor module of the Lifts program.
 *
 * @author Beatriz Cortés Sánchez
 */
public class MonitorClient {

    public static void main(String args[]) {
        Socket client;
        DataInputStream input;
        DataOutputStream output;
        String message, response;
        System.out.println("I'm the client monitor!");
        try {
            //Create socket to the server (local host) and port
            client = new Socket(InetAddress.getLocalHost(), 5000);
            //Create input/output channels
            input = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
            while (client.isConnected()) {//stays in until server is done
                response = input.readUTF(); // Read response and print it
                System.out.println(response);
            }
            client.close(); // Close connection
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
