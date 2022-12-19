import java.util.ArrayList;
import java.util.Objects;

public class Student extends Person{

    private Course course;
    private final String group;

    public Student(String fullName, String group){
        super(fullName);
        this.group = group;
    }

    public void addCourse(Course course){
        this.course = course;
    }

    public String getFinalScores(){
        return "Итог\n" +
                " Активности: " + course.getActivityScore() + " из " + course.getActivityMaxScore() +
                "\n Упражнения: " + course.getExercisesScore() + " из " + course.getExercisesMaxScore() +
                "\n Домашние работы: " + course.getHomeworkScore() + " из " + course.getHomeworkMaxScore() +
                "\n Сем: " + course.getSemScore() + " из " + course.getSemMaxScore();
    }

    public String getResultModules(){
        var modules = course.getModules();
        var result = new StringBuilder();
        for(var i: modules)
            result.append(i);
        return result.toString();
    }

    public ArrayList<Module> getModules(){
        return course.getModules();
    }

    public Module getModule(String moduleName){
        var modules = course.getModules();
        for(var i: modules){
            if(Objects.equals(moduleName, i.getModuleName()))
                return i;
        }
        return null;
    }

    public String getGroup(){
        return group;
    }

    public String toString() {
        return super.getFullName() + "\nГруппа: " + group;
    }

    public float getHomeworkScore(){
        return course.getHomeworkScore();
    }
    public float getExercisesScore(){
        return course.getExercisesScore();
    }

    public float getActivityScore(){
        return course.getActivityScore();
    }

    public float getSemScore(){
        return course.getSemScore();
    }

}
