package tel.skylab.skylabtel.utils;

import android.graphics.Bitmap;

public class Constants {

    public static final String APP_TAG = "SkyLabTel";
    // Local Session Name
    public static final String KEY_SHARED_PREFERENCES =
            "tel.skylab.skylabtel.utils.KEY_SHARED_PREFERENCES";
    // Permission Status
    public static final String PREF_KEY_PERMISSION_STATUS = "user_permission_status";
    // Login Details
    public static final String PREF_KEY_CUSTOMER_EMAIL = "login_customer_email";
    public static final String PREF_KEY_PBX_HOSTNAME = "login_pbx_hostname";
    public static final String PREF_KEY_PBX_PASSWORD = "login_pbx_password";
    public static final String PREF_KEY_PBX_NUMBER = "login_pbx_number";
    public static final String PREF_KEY_SMS_ALLOWED = "login_sms_allowed";
    public static final String PREF_KEY_SUBSCRIPTION_ACTIVE = "login_subscription_active";
    public static final String PREF_KEY_Username = "login_username";
    public static final String PREF_KEY_Password = "login_password";
    public static final String PREF_KEY_SMS_API_KEY = "login_sms_apikey";
    public static final String PREF_KEY_SMS_SECRET_KEY = "login_sms_secretkey";
    public static final String PREF_KEY_MESSAGING_PROFILE_ID = "login_messaging_profile_id";
    public static final String PREF_KEY_BUSINESS_NAME = "login_business_name";
    public static final String PREF_KEY_TOKEN = "login_token";
    public static final String PREF_KEY_LOGGED_IN = "loggedIn";
    // Server Details
    public static final int skyLabProxyPort = 443;
    //    public static final String skyLabProxyHost = "35.178.172.195";
    public static final String skyLabProxyHost = "proxy.skylab.tel";
    public static final String skyLabUsername = "rootUser";
    public static final String skyLabPassword = "rootPassword";
    // For Contact
    public static final String PREF_SAVED_CONTACTS = "saved_contacts";
    public static final String PREF_SAVED_CONTACTS_STATUS = "saved_contacts_status";
    // For Picture Calling Time
    public static Bitmap picturePtahSkyLab = null;
}
