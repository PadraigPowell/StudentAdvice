package project.year.afinal.studentadvice;

/**
 * Created by Lorcan on 27/03/2017.
 */

public class Post {
    private String heading;
    private String message;


    public Post() {
    }

    public Post(String heading, String message) {
        this.heading = heading;
        this.message = message;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
