package tel.skylab.skylabtel.apis.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import tel.skylab.skylabtel.apis.web.ApiResponse;

public class GetMsgResponseModel extends ApiResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<ResponseData> responseData;

    public ArrayList<ResponseData> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<ResponseData> responseData) {
        this.responseData = responseData;
    }

    public class ResponseData {

        @SerializedName("from")
        @Expose
        private String from;

        @SerializedName("to")
        @Expose
        private String to;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("messaging_profile_id")
        @Expose
        private String messaging_profile_id;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getMessage() {
            return message;
        }

        public String getMessaging_profile_id() {
            return messaging_profile_id;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
