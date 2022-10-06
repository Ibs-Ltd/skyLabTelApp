package tel.skylab.skylabtel.apis.request;

import com.google.gson.annotations.SerializedName;
import tel.skylab.skylabtel.apis.web.ApiRequest;

public class GetMsgRequest extends ApiRequest {

    @SerializedName("to")
    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
