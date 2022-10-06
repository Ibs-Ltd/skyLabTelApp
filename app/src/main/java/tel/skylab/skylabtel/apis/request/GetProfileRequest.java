package tel.skylab.skylabtel.apis.request;

import com.google.gson.annotations.SerializedName;
import tel.skylab.skylabtel.apis.web.ApiRequest;

public class GetProfileRequest extends ApiRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
