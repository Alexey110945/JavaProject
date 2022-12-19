import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Students {
    private final ArrayList<Student> students;

    public Students(String path, Boolean setGender) throws IOException {
        students = new CSVParser(path).getStudents();

        if (setGender){
            var vk = new VKParser();
            for (var i = 0; i < students.size(); i++){
                var student = students.get(i);
                var res = vk.getGenderAndHomeTown(student.getFullName());
                if (res != null && res.size() > 0){
                    student.setGender(res.get(0));
                    if (res.size() == 2)
                        student.setCity(res.get(1));
                }
                System.out.println((i + 1) + " из " + students.size() + " "
                        + student.getGender() + " " + student.getCity());
            }
        }
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
