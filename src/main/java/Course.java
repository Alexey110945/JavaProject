import java.util.ArrayList;

public class Course {
    private final ArrayList<Module> modules = new ArrayList<Module>();
    private final float activityMaxScore;
    private final float exercisesMaxScore;
    private final float homeworkMaxScore;
    private final float semMaxScore;

    public Course(float activityMaxScore, float exercisesMaxScore, float homeworkMaxScore, float semMaxScore){
        this.activityMaxScore = activityMaxScore;
        this.exercisesMaxScore = exercisesMaxScore;
        this.homeworkMaxScore = homeworkMaxScore;
        this.semMaxScore = semMaxScore;
    }

    public void addModule(Module module){
        modules.add(module);
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

    public float getSemMaxScore(){
        return semMaxScore;
    }

    public ArrayList<Module> getModules(){
        return modules;
    }

    public float getExercisesScore(){
        var score = 0f;
        for(var i: modules)
            score += i.getExercisesScore();
        return score;
    }

    public float getHomeworkScore(){
        var score = 0f;
        for(var i: modules)
            score += i.getHomeworkScore();
        return score;
    }

    public float getActivityScore(){
        var score = 0f;
        for(var i: modules)
            score += i.getActivityScore();
        return score;
    }

    public float getSemScore(){
        var score = 0f;
        for(var i: modules)
            score += i.getSemScore();
        return score;
    }
}
