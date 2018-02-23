package apretaste;

/**
 * Created by cjam on 8/10/2017.
 */

public class Notifications {
    public int id;
    public String received;
    public String service;
    public String text;
    public String read;
    public String link;


    public void setId(int id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getService() {
        return service;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getReceived() {
        return received;
    }

    public String getLink() {
        return link;
    }

    public String getRead() {
        return read;
    }


    public String getText() {
        return text;
    }
}