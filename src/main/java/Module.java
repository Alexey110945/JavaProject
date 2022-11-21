import java.util.ArrayList;

public class Module {
    private final String moduleName;
    private final float exercisesMaxScore;
    private final float homeworkMaxScore;
    private final float activityMaxScore;
    private final float activityScore;
    private final float semMaxScore;
    private final float semScore;

    private final ArrayList<TaskInfo> exercises = new ArrayList<>();
    private final ArrayList<TaskInfo> homeworks = new ArrayList<>();

    public Module(String moduleName, float exercisesMaxScore,
                  float homeworkMaxScore, float activityMaxScore,
                  float semMaxScore, float activityScore, float semScore) {
        this.moduleName = moduleName;
        this.exercisesMaxScore = exercisesMaxScore;
        this.homeworkMaxScore = homeworkMaxScore;
        this.activityMaxScore = activityMaxScore;
        this.activityScore = activityScore;
        this.semMaxScore = semMaxScore;
        this.semScore = semScore;
    }

    public String getModuleName(){
        return moduleName;
    }

    public float getExercisesMaxScore(){
        return exercisesMaxScore;
    }

    public float getHomeworkMaxScore(){
        return homeworkMaxScore;
    }
    public float getActivityMaxScore(){
        return activityMaxScore;
    }
    public float getActivityScore(){
        return activityScore;
    }

    public float getHomeworkScore(){
        var score = 0f;
        for(var i: homeworks)
            score += i.getPointsScored();
        return score;
    }

    public float getExercisesScore(){
        var score = 0f;
        for(var i: exercises)
            score += i.getPointsScored();
        return score;
    }

    public float getSemScore(){
        return semScore;
    }

    public float getSemMaxScore(){
        return semMaxScore;
    }

    public ArrayList<TaskInfo> getExercises(){
        return exercises;
    }

    public ArrayList<TaskInfo> getHomeworks(){
        return homeworks;
    }

    public void addExercise(TaskInfo task){
        exercises.add(task);
    }

    public void addHomework(TaskInfo task){
        homeworks.add(task);
    }

    public String getResult(){
        var result = new StringBuilder();
        result.append("Модуль: ").append(getModuleName());
        for(var i: exercises){
            var res = "\n " + i.getTaskName() + "  " + i.getPointsScored() + " из " + i.getMaxScore();
            result.append(res);
        }
        for(var i: homeworks){
            var res = "\n " + i.getTaskName() + "  " + i.getPointsScored() + " из " + i.getMaxScore();
            result.append(res);
        }
        return result.toString();
    }

    public String toString(){
        return "Модуль: " + getModuleName() +
                "\n Активности: " + getActivityScore() + " из " + getActivityMaxScore() +
                "\n Упражнения: " + getExercisesScore() + " из " + getExercisesMaxScore() +
                "\n Домание работы: " + getHomeworkScore() + " из " + getHomeworkMaxScore() +
                "\n Сем: " + getActivityScore() + " из " + getActivityMaxScore() + "\n\n";

    }
}
