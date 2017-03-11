package project.year.afinal.studentadvice;

import java.util.Date;

/**
 * Created by Lorcan on 08/03/2017.
 */

public class User {

    private String id;
    private String name;
    private int DOBYear;
    private int DOBMonth;
    private int DOBDay;
    private String email;
    private String password;
    private Boolean isUserloggedIn;


    public User() {
    }

    public User(String id, String name, int DOBYear, int DOBMonth, int DOBDay, String email, String password) {
        this.id = id;
        this.name = name;
        this.DOBYear = DOBYear;
        this.DOBMonth = DOBMonth;
        this.DOBDay = DOBDay;
        this.email = email;
        this.password = password;
        this.isUserloggedIn = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDOB(int DOBYear, int DOBMonth, int DOBDay) {
        this.DOBYear = DOBYear;
        this.DOBMonth = DOBMonth;
        this.DOBDay = DOBDay;
    }

    public String getDOB() {
        StringBuilder DOB = new StringBuilder();
        DOB.append(DOBYear);
        DOB.append(":");
        DOB.append(DOBMonth);
        DOB.append(":");
        DOB.append(DOBDay);
        return DOB.toString();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsUserloggedIn(){ return isUserloggedIn;}
}
