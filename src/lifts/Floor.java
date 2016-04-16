package lifts;

import java.util.ArrayList;

/**
 * Identified with a number from 0 to 20, holds all the people in that floor
 * @author Beatriz Cortés Sánchez
 */
public class Floor {
    
    private int id;
    private ArrayList<Person> people = new ArrayList<>();
    
    /**
     * Constructor of Floor. Starts off empty.
     * @param id the number identifying the floor
     */
    public Floor(int id){
        this.id = id;
    }
    
    /**
     * Getter of id
     * @return the id of the floor
     */
    private int getFloorId(){
        return id;
    }
    
    /**
     * Insert a person in a floor
     * @param p  person to insert
     */
    private void insertPerson(Person p){
        people.add(p);
    }
    
    /**
     * Removes a certain person from the floor
     * @param p  person to remove
     */
    private void removePerson(Person p){
        people.remove(p);
    }
}
