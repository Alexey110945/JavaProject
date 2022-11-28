import java.util.Objects;

public class Person {
    private final String fullName;
    private Gender gender = Gender.Unknown;

    enum Gender{
        Male,
        Female,
        Unknown
    }

    public Person(String fullName){
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public Gender getGender(){
        return gender;
    }

    public void setGender(String genderNum){
        if (Objects.equals(genderNum, "2"))
            gender = Gender.Male;
        else if (Objects.equals(genderNum, "1"))
            gender = Gender.Female;
    }
}
