package project.year.afinal.studentadvice;

import java.util.Date;

/**
 * Created by Lorcan on 08/03/2017.
 */

public class User {
    private String id;
    private String name;
    private String DOB;
    private String email;
    private String password;


    public User() {
    }

    public User(String id, String name, String email, String DOB, String password) {
        this.id = id;
        this.name = name;
        this.DOB = DOB;
        this.email = email;
        this.password = password;
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

    public void setDOB(String DOB) {this.DOB = DOB;}

    public String getDOB() {return DOB;}

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

}
