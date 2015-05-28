package lumstic.example.com.lumstic.Models;

import java.util.ArrayList;

/**
 * Created by work on 17/4/15.
 */
public class Answers {
    int recordId;
    int webId;
    int updatedAt;
    int id;
    int responseId;
    int questionId;
    String image;
    String content;
    ArrayList<Integer> optionIds;

    public ArrayList<Integer> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(ArrayList<Integer> optionIds) {
        this.optionIds = optionIds;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getWebId() {
        return webId;
    }

    public void setWebId(int webId) {
        this.webId = webId;
    }

    public int getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(int updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
