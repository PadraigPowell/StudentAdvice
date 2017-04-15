package project.year.afinal.studentadvice;

import com.firebase.client.ServerValue;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;
import java.util.HashMap;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.DateUtils;

/**
 * Created by Lorcan on 27/03/2017.
 */

@IgnoreExtraProperties
public class Post {
    public String uid;
    public String author;
    public String title;
    public String message;
    public Long timestamp;
    public long disagreeCount;
    public long agreeCount;
    public long commentCount;
    public long saveCount;

    @Exclude
    public String adviceKey;


    public Post() {
    }

    public Post(String uid, String author, String title, String message, int disagreeCount, int agreeCount, int saveCount, int commentCount) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.message = message;
        this.disagreeCount = disagreeCount;
        this.agreeCount = agreeCount;
        this.saveCount = saveCount;
        this.commentCount = commentCount;
    }

    public void incrementSave(){this.saveCount += 1;}

    public String getTitle(){return this.title;}

    public String getMassage(){return this.message;}

    public String getAuthor(){return this.author;}

    public String getAgreeMsg(){return this.agreeCount + " Agree";}

    public String getDisagreeMsg(){return this.disagreeCount + " Disagree";}

    public String getCommentMsg(){return this.commentCount + " Comment";}

    public String getSaveMsg(){return this.saveCount + " Save";}

    public String getMassagePreview(int CharAmount) {
        if (this.message.length() > CharAmount)
            return this.message.substring(0, Math.min(message.length(), CharAmount-3)) + "...";
        else
            return this.message;
    }

    public String getDateTime(Context context)
    {
        String date = DateUtils.getRelativeDateTimeString(context,
                timestamp,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                0).toString();
        return date;
    }

    @Exclude
    public void setAdviceKey(String advice) {this.adviceKey = advice;}

    @Exclude
    public String getAdviceKey()
    {
        return this.adviceKey;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("message", message);
        result.put("message", message);
        result.put("disagreeCount", disagreeCount);
        result.put("agreeCount", agreeCount);
        result.put("commentCount", commentCount);
        result.put("saveCount", saveCount);
        result.put("timestamp", ServerValue.TIMESTAMP);

        return result;
    }

}