package project.year.afinal.studentadvice;

import com.firebase.client.ServerValue;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;
import java.util.HashMap;
import java.lang.annotation.Annotation;

/**
 * Created by Lorcan on 27/03/2017.
 */

@IgnoreExtraProperties
public class Post {
    public String uid;
    public long timestamp;
    public String title;
    public long disagreeCount;
    public String message;
    public long likeCount;
    public String author;

    @Exclude
    public String advice;


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

    public String getTitle(){return this.title;}

    public String getMassagePreview(int CharAmount){
        return this.message.substring(0, Math.min(message.length(), CharAmount));
    }

    @Exclude
    public void setAdviceKey(String advice) {this.advice = advice;}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("message", message);
        result.put("likeCount", likeCount);
        result.put("disagreeCount", disagreeCount);
        result.put("timestamp", timestamp);

        return result;
    }

}
