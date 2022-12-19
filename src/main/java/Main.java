import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var students = new Students("resources/basicprogramming.csv", false);

        var s = new Sqlite(students);
//        s.Clear();
//        s.CreateTables();
//        s.AddStudentsAndScores();
//        s.AddModules();
        var g = new Charts(s);
        g.SaveCharts("charts");


//        var vk = new VKParser();
//        var t = vk.getGenderAndHomeTown("Мамаев Алексей");
//        System.out.println(t);
    }
}
