package lumstic.example.com.lumstic.Models;


public class Choices {
    int id;
    int optionId;
    int answerId;

    public Choices(int id, int optionId, int answerId) {
        this.id = id;
        this.optionId = optionId;
        this.answerId = answerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
}
