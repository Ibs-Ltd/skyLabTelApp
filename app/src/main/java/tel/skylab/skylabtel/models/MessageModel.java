package tel.skylab.skylabtel.models;

public class MessageModel {

    private String from;

    private String to;

    private String message;

    private String messaging_profile_id;

    private String createdAt;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessaging_profile_id() {
        return messaging_profile_id;
    }

    public void setMessaging_profile_id(String messaging_profile_id) {
        this.messaging_profile_id = messaging_profile_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
