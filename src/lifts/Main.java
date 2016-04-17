package lifts;

/**
 * Spawns 20 floors, a controller, two lifts, a printer and begins to spawn people randomly on floors.
 * @author Beatriz Cortés Sánchez
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventBarrier[] floors = new EventBarrier[21];
        for(int i = 0; i < 20; i++){
            floors[i] = new EventBarrier();
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
        Person p = new Person("P1", 6, 20, c);
    }
    
}
