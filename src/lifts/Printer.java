package lifts;

import java.io.PrintWriter;
import static java.lang.Thread.sleep;

/**
 * Periodically prints the status of the building.
 * @author Beatriz Cortés Sánchez
 */
public class Printer extends Thread{
    private Controller c;
    private Lift l1;
    private Lift l2;
    private PrintWriter printOut;

    public Printer(Controller c, Lift l1, Lift l2, PrintWriter printOut) {
        this.c = c;
        this.l1 = l1;
        this.l2 = l2;
        this.printOut = printOut;
        this.start();
    }

    public void run() {
        while (!c.areMovementsExhausted()) {
            drawState();
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException caught in Printer run()");
            }
        }
    }
    
        public void drawState() {
        String toDrawL1;// | if not there. Else status + # + numPeople
        String toDrawL2;
        String toDrawButton;//Yes if pressed. No if not pressed
        String peopleToLeave;//Makes a list of people who want to get off at that floor
        String destList = "";
        System.out.println("Floor:  Lift1:   Lift2:   ButtonPressed?:    Destination: ");
        printOut.println("Floor:  Lift1:   Lift2:   ButtonPressed?:    Destination: ");
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
            System.out.println(i + "\t" + toDrawL1 + "\t" + toDrawL2 + "\t\t" + toDrawButton + "\t\t" + destList);
            printOut.println(i + "\t" + toDrawL1 + "\t" + toDrawL2 + "\t\t" + toDrawButton + "\t\t" + destList);
        }
        /*if(areMovementsExhausted()){
            System.out.println("Lifts will stop moving now.");
        }*/
    }
}
