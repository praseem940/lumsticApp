package lumstic.example.com.lumstic.Models;

/**
 * Created by work on 17/4/15.
 */
public class Options {
    int orderNumber;
    int id;
    int questionId;
    String content;

    public Options(int orderNumber, int id, int questionId, String content) {
        this.orderNumber = orderNumber;
        this.id = id;
        this.questionId = questionId;
        this.content = content;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
