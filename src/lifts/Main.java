package lifts;

import static java.lang.Thread.sleep;
import java.util.Random;

/**
 * Spawns 20 floors, a controller, two lifts, a printer and begins to spawn
 * people randomly on floors.
 *
 * @author Beatriz Cortés Sánchez
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FloorBarrier[] floors = new FloorBarrier[21];
        for (int i = 0; i <= 20; i++) {
            floors[i] = new FloorBarrier();
        }
        Controller c = new Controller(null, null, floors);
        Lift l1 = new Lift("l1", c);
        Lift l2 = new Lift("l2", c);
        c.setL1(l1);
        c.setL2(l2);
        l2.breakLift();
        l1.start();
        l2.start();
        c.start();
        Printer sd = new Printer(c);
        Random r = new Random();
        //make a random person every 0.5s
        for (int i = 1; i <= 100; i++) {
            new Person("P" + i, r.nextInt(20), r.nextInt(20), c);
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException caught in Main main()");
            }
        }
    }

}
