import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Students {
    private final ArrayList<Student> students;

    public Students() throws IOException {
        students = new CSVParser().getStudents();
    }

    public ArrayList<Student> getStudents(){ return students; }

    public Student getStudent(String surnameName){
        for(var i: students){
            if (Objects.equals(i.getSurnameName(), surnameName)){
                return i;
            }
        }
        return null;
    }
}
