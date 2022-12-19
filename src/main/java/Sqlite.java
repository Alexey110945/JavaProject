import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class Sqlite {
    private Connection con = null;
    private final Students students;

    public Sqlite(Students students){
        this.students = students;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:resources/DataBase.sqlite");
        } catch (Exception ignore){}
    }

    public void AddStudentsAndScores(){

        var stInfo = students.getStudents();
        for (int i = 0; i < stInfo.size(); i++){
            var st = stInfo.get(i);
            var id = Integer.toString(i + 1);

            var queryStudents = "INSERT INTO Students (id, fullName, studyGroup, gender, city) " +
                    "VALUES (" + id + ", '" + st.getFullName() + "', '"
                    + st.getGroup() + "', '"
                    + st.getGender() + "', '"
                    + st.getCity() + "')";
            Update(queryStudents);

            var queryFinalScores = "INSERT INTO FinalScores (student_id, activity, exercises, homework, sem) " +
                    "VALUES (" + id + ", " + st.getActivityScore() + ", "
                    + st.getExercisesScore()+ ", "
                    + st.getHomeworkScore() + ", "
                    + st.getSemScore() + ")";
            Update(queryFinalScores);

            for (var table: new String[]{"ActivityScores", "ExercisesScores", "HomeworkScores", "SemScores"}){
                var query = new StringBuilder();
                query.append("INSERT INTO ").append(table).append(" VALUES (").append(id);
                for (var module: st.getModules())
                    query.append(", ").append(getScore(table, module));
                Update(query.append(")").toString());
            }
        }
    }

    private float getScore(String tableName, Module module){
        if (Objects.equals(tableName, "ActivityScores"))
            return module.getActivityScore();
        else if (Objects.equals(tableName, "ExercisesScores"))
            return module.getExercisesScore();
        else if (Objects.equals(tableName, "HomeworkScores"))
            return module.getHomeworkScore();
        else if (Objects.equals(tableName, "SemScores"))
            return module.getSemScore();
        return 0;
    }

    public void AddModules(){
        var modules = students.getStudents().get(0).getModules();
        for (var i = 0;i < modules.size();i++){
            var moduleShortName = "module_" + (i + 1);
            var module = modules.get(i);
            var queryModule = "INSERT INTO ModulesMaxScores " +
                   "(moduleName, moduleShortName, activity, exercises, homework, sem) " +
                   "VALUES ('" + module.getModuleName() + "', '"
                    + moduleShortName + "', "
                    + Math.round(module.getActivityMaxScore()) + ", "
                    + Math.round(module.getExercisesMaxScore()) + ", "
                    + Math.round(module.getHomeworkMaxScore()) + ", "
                    + Math.round(module.getSemMaxScore()) + ");";
            Update(queryModule);
        }
    }

    public void CreateTables(){
        var queryCreateStudents = "CREATE TABLE Students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fullName VARCHAR(50), " +
                "studyGroup VARCHAR(50), " +
                "gender VARCHAR(10), " +
                "city VARCHAR(20));";
        Update(queryCreateStudents);

        var queryCreateFinalScores = "CREATE TABLE FinalScores (" +
                "student_id INTEGER, " +
                "activity INTEGER(2), " +
                "exercises INTEGER(3), " +
                "homework INTEGER(4), " +
                "sem INTEGER(2), " +
                "FOREIGN KEY (student_id) REFERENCES Students (id));";
        Update(queryCreateFinalScores);

        var queryCreateModules = "CREATE TABLE ModulesMaxScores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "moduleName VARCHAR(30), " +
                "moduleShortName VARCHAR(10), " +
                "activity INTEGER(2), " +
                "exercises INTEGER(3), " +
                "homework INTEGER(4), " +
                "sem INTEGER(2));";
        Update(queryCreateModules);

        for (var i: new String[]{"ActivityScores", "ExercisesScores", "HomeworkScores", "SemScores"}){
            var query = new StringBuilder();
            query.append("CREATE TABLE ").append(i).append(" (student_id INTEGER, ");
            for (var j = 1; j <= students.getStudents().get(0).getModules().size(); j++)
                query.append("module_").append(j).append(" INTEGER(3), ");
            query.append("FOREIGN KEY (student_id) REFERENCES Students (id));");
            Update(query.toString());
        }
    }

    private void Update(String query){
        try {
            assert con != null;
            var statement = con.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Clear(){
        var tables = new String[]{"ActivityScores", "ExercisesScores",
                                  "HomeworkScores", "SemScores",
                                  "FinalScores", "Students",
                                  "ModulesMaxScores"};
        for (var table: tables)
            Update("DELETE FROM " + table + ";");
    }

    public Integer[] getOldStudentCount() throws IOException {
        var result = new Integer[]{0, 0, 0};

        var oldStudents = new Students("resources/basicprogramming_2.csv", false);
        for (var i: students.getStudents())
            if (oldStudents.getStudent(i.getFullName()) != null)
                result[0]++;
            else
                result[2]++;
        result[1] = oldStudents.getStudents().size() - result[0];
        return result;// остались ушли пришли
    }

    public Integer[] getMeanScoresKS(String level){
        var MS = getMaxScores();
        var result = new Integer[]{0, 0, 0, 0};
        try {
            var query = "SELECT id FROM Students WHERE studyGroup LIKE '%" + level + "%'";
            var data = SelectData(query);
            while (data.next()){
                query = "SELECT exercises, homework FROM FinalScores WHERE student_id == " +
                        data.getInt("id") + ";";
                var scores = SelectData(query);
                var meanScore = (scores.getFloat("exercises") / MS[1] +
                        scores.getFloat("homework") / MS[2]) * 100 / 2;
                if(meanScore >= 80)
                    result[3]++;
                else if(meanScore >= 60)
                    result[2]++;
                else if(meanScore >= 40)
                    result[1]++;
                else
                    result[0]++;
            }
        } catch (Exception ignore) {}
        return result;
    }

    public Integer[] getMeanScoresAllStudent(String ind) {
        var MS = getMaxScores();
        var result = new Integer[]{0, 0, 0, 0};
        try {
            var query = "SELECT exercises, homework FROM FinalScores";
            var data = SelectData(query);
            while (data.next()) {
                assert MS != null;
                float meanScore;
                if (Objects.equals(ind, "exercises"))
                    meanScore = (data.getFloat(ind) / MS[1]) * 100;
                else if (Objects.equals(ind, "homework"))
                    meanScore = (data.getFloat(ind) / MS[2]) * 100;
                else
                    meanScore = (data.getFloat("exercises") / MS[1] +
                            data.getFloat("homework") / MS[2]) * 100 / 2;
                if(meanScore >= 80)
                    result[3]++;
                else if(meanScore >= 60)
                    result[2]++;
                else if(meanScore >= 40)
                    result[1]++;
                else
                    result[0]++;
            }
        } catch (Exception ignore){}
        return result;
    }

    public String[] getModuleNames(){
        String[] result = null;
        try {
            var countModules = SelectData("SELECT COUNT(moduleName) FROM ModulesMaxScores");
            var data = SelectData("SELECT moduleName FROM ModulesMaxScores");
            result = new String[countModules.getInt("COUNT(moduleName)")];
            for(var i = 0; i < result.length; i++) {
                data.next();
                result[i] = data.getString("moduleName");
            }
        } catch (Exception ignore){}
        return result;
    }

    public Integer[] getMeanScoreModules(){
        Integer[] result = null;
        try {
            var countModules = SelectData("SELECT COUNT(moduleName) FROM ModulesMaxScores");
            result = new Integer[countModules.getInt("COUNT(moduleName)")];
            var modules = SelectData("SELECT moduleShortName FROM ModulesMaxScores");
            var i = 0;
            while (modules.next()){
                var shortName = modules.getString("moduleShortName");
                var maxScores = SelectData("SELECT exercises, homework " +
                        "FROM ModulesMaxScores " +
                        "WHERE moduleShortName == '" + shortName + "';");
                var maxScoreExercises = maxScores.getFloat("exercises");
                var maxScoreHomework = maxScores.getFloat("homework");

                var scoresEx = SelectData("SELECT AVG(" + shortName +
                        ") FROM ExercisesScores").getInt("AVG(" + shortName + ")");
                var scoresHw = SelectData("SELECT AVG(" + shortName +
                        ") FROM HomeworkScores").getInt("AVG(" + shortName + ")");

                var mean = 0f;
                if (maxScoreExercises > 0)
                    mean += scoresEx / maxScoreExercises;
                if (maxScoreHomework > 0)
                    mean += scoresHw / maxScoreHomework;
                if (maxScoreExercises > 0 && maxScoreHomework > 0)
                    mean /= 2f;
                result[i] = Math.round(mean * 100) ;
                i++;
            }


        } catch (Exception ignore) {}
        return result;
    }

    public String[] getCities() {
        var result = new ArrayList<String>();
        try {
            var data = SelectData("SELECT DISTINCT city FROM Students");
            while (data.next())
                result.add(data.getString("city"));
        } catch (Exception ignore){}
        return result.toArray(new String[0]);
    }

    public Integer[] getCountStudentsFromCities(){
        var citiesNames = getCities();
        var result = new Integer[citiesNames.length];
        try {
            for (var i = 0; i < citiesNames.length; i++){
                result[i] = SelectData("SELECT COUNT(city) FROM Students " +
                        "WHERE city == '" + citiesNames[i] + "'").getInt("COUNT(city)");
            }
        } catch (Exception ignore) {}
        return result;
    }

    public Integer[] getGenderCount(){
        var result = new Integer[]{0, 0, 0};
        var gender = new String[]{"Male", "Female", "Unknown"};
        try {
            for (var i = 0; i < gender.length; i++){
                var query = "SELECT COUNT(gender) FROM Students WHERE gender = '" + gender[i] + "'";
                result[i] = SelectData(query).getInt("COUNT(gender)");
            }
        } catch (Exception ignore){}
        return result;
    }

    private Integer[] getMaxScores(){
        Integer[] maxScores = null;
        try {
            var query = "SELECT SUM(activity), SUM(exercises), SUM(homework), SUM(sem) FROM ModulesMaxScores";
            var data = SelectData(query);
            maxScores = new Integer[] {
                    data.getInt("SUM(activity)"),
                    data.getInt("SUM(exercises)"),
                    data.getInt("SUM(homework)"),
                    data.getInt("SUM(sem)")};
        } catch (Exception ignore){}
        return maxScores;
    }

    public ResultSet SelectData(String query){
        ResultSet result = null;
        try {
            assert con != null;
            var statement = con.createStatement();
            result = statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
