package tel.skylab.skylabtel.apis.request;

import com.google.gson.annotations.SerializedName;
import tel.skylab.skylabtel.apis.web.ApiRequest;

public class SendMsgRequest extends ApiRequest {

    @SerializedName("from")
    private String from;

    @SerializedName("to")
    private String to;

    @SerializedName("text")
    private String text;

    @SerializedName("messaging_profile_id")
    private String messaging_profile_id;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessaging_profile_id() {
        return messaging_profile_id;
    }

    public void setMessaging_profile_id(String messaging_profile_id) {
        this.messaging_profile_id = messaging_profile_id;
    }
}
