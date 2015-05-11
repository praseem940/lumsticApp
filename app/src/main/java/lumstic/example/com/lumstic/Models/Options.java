package lumstic.example.com.lumstic.Models;


import java.io.Serializable;

public class Options implements Serializable{
    int orderNumber;
    int id;
    int questionId;
    String content;
    Questions questions;

    public Questions getQuestions() {
        return questions;
    }
    public void setQuestions(Questions questions) {
        this.questions = questions;
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
