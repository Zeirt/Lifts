package lifts;

import static java.lang.Thread.sleep;

/**
 * Periodically prints the status of the building.
 * @author Beatriz Cortés Sánchez
 */
public class Printer extends Thread{
    private Controller c;

    public Printer(Controller c) {
        this.c = c;
        this.start();
    }

    public void run() {
        while (true) {
            c.drawState();
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException caught in Printer run()");
            }
        }
    }
}
