package tel.skylab.skylabtel.apis.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import tel.skylab.skylabtel.apis.web.ApiResponse;

public class LoginResponseModel extends ApiResponse {

    @SerializedName("data")
    @Expose
    private ResponseData responseData;

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public class ResponseData {

        @SerializedName("customer_email")
        @Expose
        private String customer_email;

        @SerializedName("pbx_hostname")
        @Expose
        private String pbx_hostname;

        @SerializedName("pbx_number")
        @Expose
        private String pbx_number;

        @SerializedName("pbx_password")
        @Expose
        private String pbx_password;

        @SerializedName("sms_allowed")
        @Expose
        private String sms_allowed;

        @SerializedName("subscription_active")
        @Expose
        private String subscription_active;

        @SerializedName("sms_apikey")
        @Expose
        private String sms_apikey;

        @SerializedName("sms_secretkey")
        @Expose
        private String sms_secretkey;

        @SerializedName("messaging_profile_id")
        @Expose
        private String messaging_profile_id;

        @SerializedName("business_name")
        @Expose
        private String business_name;

        @SerializedName("token")
        @Expose
        private String token;

        public String getCustomer_email() {
            return customer_email;
        }

        public void setCustomer_email(String customer_email) {
            this.customer_email = customer_email;
        }

        public String getPbx_hostname() {
            return pbx_hostname;
        }

        public void setPbx_hostname(String pbx_hostname) {
            this.pbx_hostname = pbx_hostname;
        }

        public String getPbx_number() {
            return pbx_number;
        }

        public void setPbx_number(String pbx_number) {
            this.pbx_number = pbx_number;
        }

        public String getPbx_password() {
            return pbx_password;
        }

        public void setPbx_password(String pbx_password) {
            this.pbx_password = pbx_password;
        }

        public String getSms_allowed() {
            return sms_allowed;
        }

        public void setSms_allowed(String sms_allowed) {
            this.sms_allowed = sms_allowed;
        }

        public String getSubscription_active() {
            return subscription_active;
        }

        public void setSubscription_active(String subscription_active) {
            this.subscription_active = subscription_active;
        }

        public String getSms_apikey() {
            return sms_apikey;
        }

        public void setSms_apikey(String sms_apikey) {
            this.sms_apikey = sms_apikey;
        }

        public String getSms_secretkey() {
            return sms_secretkey;
        }

        public void setSms_secretkey(String sms_secretkey) {
            this.sms_secretkey = sms_secretkey;
        }

        public String getMessaging_profile_id() {
            return messaging_profile_id;
        }

        public void setMessaging_profile_id(String messaging_profile_id) {
            this.messaging_profile_id = messaging_profile_id;
        }

        public String getBusiness_name() {
            return business_name;
        }

        public void setBusiness_name(String business_name) {
            this.business_name = business_name;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
