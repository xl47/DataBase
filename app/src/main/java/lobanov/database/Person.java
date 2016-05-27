package lobanov.database;

import java.io.Serializable;
import java.util.Comparator;

public class Person implements Comparator<Person>,Serializable {
    private long _id;
    private String _firstname;
    private String _lastname;
    public Person(){
        _lastname = "notsetted";
        _firstname = "notsetted";
    }
    public Person(long id, String lastname, String firstname) {
        _id = id;
        _firstname = firstname;
        _lastname = lastname;
    }
    public long get_id()
    {
        return _id;
    }
    public String get_firstname(){
        return _firstname;
    }
    public String get_lastname(){
        return  _lastname;
    }
    public void set_firstname(String newfirstname){
        _firstname = newfirstname;
    }
    public void set_lastname(String newlastname){
        _lastname = newlastname;
    }

    public int compare(Person A, Person B) {
        return A.get_lastname().compareTo(B.get_lastname());
    }

    @Override
    public String toString() {
        return (_firstname+" "+_lastname);
    }
}