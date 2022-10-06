package tel.skylab.skylabtel.apis.web;
/** {@link ApiRequest} */
public abstract class ApiRequest {

    public boolean isValid() {
        return true;
    }

    public String getBaseUrl() {
        return AppConstant.SERVER_URL;
    }
}
