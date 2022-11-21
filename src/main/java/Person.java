public class Person {
    private final String surnameName;

    enum Gender{
        Male,
        Female
    }

    public Person(String surnameName){
        this.surnameName = surnameName;
    }

    public String getSurname() {
        return "";
    }

    public String getName() {
        return "";
    }

    public String getSurnameName() {
        return surnameName;
    }
}
