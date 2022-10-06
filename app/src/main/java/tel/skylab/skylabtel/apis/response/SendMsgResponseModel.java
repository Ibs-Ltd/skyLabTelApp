package tel.skylab.skylabtel.apis.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import tel.skylab.skylabtel.apis.web.ApiResponse;

public class SendMsgResponseModel extends ApiResponse {

    @SerializedName("data")
    @Expose
    private ResponseData responseData;

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public class ResponseData {}
}
