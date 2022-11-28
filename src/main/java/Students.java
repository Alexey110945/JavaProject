import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Students {
    private final ArrayList<Student> students;

    public Students() throws IOException {
        var vk = new VKParser();
        students = new CSVParser().getStudents();
        for (Student student : students)
            student.setGender(vk.getSex(student.getFullName()));
    }

    public ArrayList<Student> getStudents(){ return students; }

    public Student getStudent(String fullName){
        for(var i: students){
            if (Objects.equals(i.getFullName(), fullName)){
                return i;
            }
        }
        return null;
    }
}
