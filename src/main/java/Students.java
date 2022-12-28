import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Students {
    private final ArrayList<Student> students;

    public Students(String path, Boolean setGender) throws IOException {
        students = new CSVParser(path).getStudents();

        var vk = new VKParser();
        if (setGender){
            for (var i = 0; i < students.size(); i++){
                var student = students.get(i);
                var res = vk.getGenderAndCity(student.getFullName());
                if (res != null){
                    var sex = res.getItems().get(0).getSex();
                    if (sex != null)
                        student.setGender(sex.getValue());
                    var city = res.getItems().get(0).getCity();
                    if (city != null)
                        student.setCity(city.getTitle());
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
