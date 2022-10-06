package tel.skylab.skylabtel.roomDatabase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "received_messages_table")
public class MessagesEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "from")
    private String from;

    @ColumnInfo(name = "to")
    private String to;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "messaging_profile_id")
    private String messaging_profile_id;

    @ColumnInfo(name = "createdAt")
    private String createdAt;

    public MessagesEntity(
            String from, String to, String message, String messaging_profile_id, String createdAt) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.messaging_profile_id = messaging_profile_id;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
