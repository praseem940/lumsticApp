package lumstic.example.com.lumstic.Models;

/**
 * Created by work on 17/4/15.
 */
public class Surveys {
    int id;
    String publishedOn;
    String name;
    String description;
    String expiryDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Surveys(int id, String publishedOn, String name, String description, String expiryDate) {
        this.id = id;
        this.publishedOn = publishedOn;
        this.name = name;
        this.description = description;
        this.expiryDate = expiryDate;
    }
}
