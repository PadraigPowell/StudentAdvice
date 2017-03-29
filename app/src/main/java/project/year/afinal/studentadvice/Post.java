package project.year.afinal.studentadvice;

import com.firebase.client.ServerValue;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Lorcan on 27/03/2017.
 */

public class Post {
    private String uid;
    private String author;
    private String title;
    private String message;
    private int likeCount;
    private int disagreeCount;


    public Post() {
    }

    public Post(String uid, String author, String title, String message, int likeCount, int disagreeCount) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.message = message;
        this.likeCount = likeCount;
        this.disagreeCount = disagreeCount;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("message", message);
        result.put("likeCount", likeCount);
        result.put("disagreeCount", disagreeCount);
        result.put("timestamp", ServerValue.TIMESTAMP);

        return result;
    }

}
