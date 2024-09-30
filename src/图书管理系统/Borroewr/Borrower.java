package 图书管理系统.Borroewr;

public class Borrower {
    private String name;
    private String ID;
    private String age;
    private char[] password;

    public void setPassword(char[] password) {
        this.password = password;
    }

    public char[] getPassword() {
        return password;
    }

    public Borrower(String name, String ID, String age, char[] password) {
        this.name = name;
        this.ID = ID;
        this.age = age;
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public String getID() {
        return ID;
    }
    public Borrower(String name, String iD, String age) {
        this.name = name;
        ID = iD;
        this.age = age;
    }
    public Borrower() {
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setID(String iD) {
        ID = iD;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getAge() {
        return age;
    }

    
    
}
