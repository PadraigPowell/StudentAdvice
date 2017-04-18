package project.year.afinal.studentadvice;

/**
 * Created by Lorcan on 08/03/2017.
 */

public class User {
    private String name;
    private String email;
    private long viewed;


    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.viewed = 0;
    }

    public void incrementViewed(){this.viewed += 1;}

    public long getViewed(){return this.viewed;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
