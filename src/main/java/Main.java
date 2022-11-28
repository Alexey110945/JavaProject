
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var students = new Students();
        var st = students.getStudent("Белобородова Полина");

        System.out.println(st);
        System.out.println(st.getGender());
    }
}
