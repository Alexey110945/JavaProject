import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CSVParser {
    ArrayList<Student> students = new ArrayList<Student>();
    String[] columns;
    String[] maxScores;

    String[] moduleNames;


    public CSVParser() throws IOException {
        var path = Path.of("basicprogramming_2.csv");
        var fileLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        var data = fileLines.stream().map(line -> line.split(";", -1)).toList();
        moduleNames = data.get(0);
        columns = data.get(1);
        maxScores = data.get(2);
        createStudents(data);
    }

    public ArrayList<Student> getStudents(){ return students; }

    private void createStudents(List<String[]> data){
        for(int i = 3; i < data.size(); i++){
            students.add(createCourse(new Student(data.get(i)[0], data.get(i)[1]), data.get(i)));
        }
    }

    private Student createCourse(Student student, String[] inf){
        var activity = Float.parseFloat(maxScores[2]);
        var exercises = Float.parseFloat(maxScores[3]);
        var homework = Float.parseFloat(maxScores[4]);
        var sem = Float.parseFloat(maxScores[5]);
        var course = new Course(activity, exercises, homework, sem);

        Module module = null;
        int i = 8;

        while (i < inf.length){
            var titles = Arrays.copyOfRange(columns, i, columns.length);

            var md = Arrays.copyOfRange(inf, i, Arrays.asList(titles).indexOf("Сем") + i + 1);
            var hw = Arrays.copyOfRange(columns, i, Arrays.asList(titles).indexOf("Сем") + i + 1);
            var mScore = Arrays.copyOfRange(maxScores, i, Arrays.asList(titles).indexOf("Сем") + i + 1);
            var hMaxScore = find(hw, "ДЗ") != -1 ? maxScores[find(hw, "ДЗ") + i]: "0";

            var exercisesMaxScore = Float.parseFloat(maxScores[i + 1]);
            var homeworkMaxScore = Float.parseFloat(hMaxScore);
            var activityMaxScore = Float.parseFloat(maxScores[i]);
            var activityScore = Float.parseFloat(maxScores[i + md.length - 1]);
            var semMaxScore = Float.parseFloat(md[0]);
            var semScore = Float.parseFloat(md[md.length - 1]);

            module = new Module(moduleNames[i], exercisesMaxScore, homeworkMaxScore,
                    activityMaxScore, activityScore, semMaxScore, semScore);

            i = Arrays.asList(titles).indexOf("Сем") + i + 1;
            course.addModule(addTasks(module, md, mScore, hw));
        }
        student.addCourse(course);
        return student;
    }

    private Module addTasks(Module module, String[] scores, String[] mScores, String[] names){
        for (var i = 0; i < scores.length;i++){
            var ms = Float.parseFloat(mScores[i]);
            var ps = Float.parseFloat(scores[i]);
            if(names[i].startsWith("Упр:"))
                module.addExercise(new TaskInfo(names[i], ms, ps));
            else if (names[i].startsWith("ДЗ:"))
                module.addHomework(new TaskInfo(names[i], ms, ps));
        }
        return module;
    }

    private static int find(String[] a, String elem)
    {
        for (int i = 0; i < a.length; i++)
            if (Objects.equals(a[i], elem))
                return i;
        return -1;
    }
}