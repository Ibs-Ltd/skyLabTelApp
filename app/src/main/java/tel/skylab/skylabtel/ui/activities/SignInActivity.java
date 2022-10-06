package tel.skylab.skylabtel.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import org.linphone.core.AccountCreator;
import org.linphone.core.Core;
import org.linphone.core.DialPlan;
import org.linphone.core.Factory;
import org.linphone.core.ProxyConfig;
import org.linphone.core.TransportType;
import org.linphone.core.tools.Log;
import retrofit2.Call;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.apis.request.LoginRequest;
import tel.skylab.skylabtel.apis.response.LoginResponseModel;
import tel.skylab.skylabtel.apis.service.LoginService;
import tel.skylab.skylabtel.apis.web.ApiCallback;
import tel.skylab.skylabtel.apis.web.ApiException;
import tel.skylab.skylabtel.linphone.LinphoneContext;
import tel.skylab.skylabtel.linphone.LinphoneManager;
import tel.skylab.skylabtel.linphone.activities.LinphoneGenericActivity;
import tel.skylab.skylabtel.linphone.settings.LinphonePreferences;
import tel.skylab.skylabtel.utils.Constants;

public class SignInActivity extends LinphoneGenericActivity {

    LinearLayout ll_signIn_btn;
    TextView forgot_btn;
    Button tv_login_btn;
    EditText et_username, et_password;
    String Username, Password;
    ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }

        //        ll_signIn_btn = findViewById(R.id.ll_signIn_btn);
        forgot_btn = findViewById(R.id.forgot_btn);
        tv_login_btn = findViewById(R.id.tv_login_btn);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        /*ll_signIn_btn.setOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });*/

        forgot_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(SignInActivity.this, ForgotPasswordActivity.class));
                    }
                });

        tv_login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Username = et_username.getText().toString().trim();
                        Password = et_password.getText().toString().trim();
                        String rootUser = md5(et_username.getText().toString().trim());
                        String rootPassword = "!pzMLqq5j[XM" + rootUser;

                        if (Username.equalsIgnoreCase("")) {
                            et_username.setError("Please enter Email or Username");
                        } else {
                            if (Password.equalsIgnoreCase("")) {
                                et_password.setError("Please enter Password");
                            } else {
                                SharedPreferences preferences =
                                        getSharedPreferences(
                                                Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(Constants.skyLabUsername, rootUser);
                                editor.putString(Constants.skyLabPassword, rootPassword);
                                editor.apply();

                                // Creating an AsyncTask object to retrieve
                                hitLoginApi hit_login_api = new hitLoginApi();
                                // Starting the AsyncTask process to retrieve
                                hit_login_api.execute();
                            }
                        }
                    }
                });
    }

    private class hitLoginApi extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb = new ProgressDialog(SignInActivity.this);
            pb.setTitle("SignIn Process");
            pb.setMessage("Please wait.....");
            pb.setCancelable(false);
            pb.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //             String android_id =
            // Settings.Secure.getString(SignInActivity.this.getContentResolver(),
            // Settings.Secure.ANDROID_ID);
            LoginRequest request = new LoginRequest();
            request.setEmail(Username);
            request.setPassword(Password);

            new LoginService(getApplicationContext())
                    .execute(
                            request,
                            new ApiCallback<LoginResponseModel>() {
                                @Override
                                public void onSuccess(
                                        Call<LoginResponseModel> call,
                                        LoginResponseModel response) {

                                    if (response.getCodes() == 0) {

                                        Toast.makeText(
                                                        SignInActivity.this,
                                                        response.getMessage(),
                                                        Toast.LENGTH_SHORT)
                                                .show();

                                        SharedPreferences preferences =
                                                getSharedPreferences(
                                                        Constants.KEY_SHARED_PREFERENCES,
                                                        MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString(
                                                Constants.PREF_KEY_CUSTOMER_EMAIL,
                                                response.getResponseData().getCustomer_email());
                                        editor.putString(Constants.PREF_KEY_Username, Username);
                                        editor.putString(Constants.PREF_KEY_Password, Password);
                                        editor.putString(
                                                Constants.PREF_KEY_PBX_HOSTNAME,
                                                response.getResponseData().getPbx_hostname());
                                        editor.putString(
                                                Constants.PREF_KEY_PBX_PASSWORD,
                                                response.getResponseData().getPbx_password());
                                        editor.putString(
                                                Constants.PREF_KEY_PBX_NUMBER,
                                                response.getResponseData().getPbx_number());
                                        editor.putString(
                                                Constants.PREF_KEY_SMS_ALLOWED,
                                                response.getResponseData().getSms_allowed());
                                        editor.putString(
                                                Constants.PREF_KEY_SUBSCRIPTION_ACTIVE,
                                                response.getResponseData()
                                                        .getSubscription_active());
                                        editor.putString(
                                                Constants.PREF_KEY_SMS_API_KEY,
                                                response.getResponseData().getSms_apikey());
                                        editor.putString(
                                                Constants.PREF_KEY_SMS_SECRET_KEY,
                                                response.getResponseData().getSms_secretkey());
                                        editor.putString(
                                                Constants.PREF_KEY_MESSAGING_PROFILE_ID,
                                                response.getResponseData()
                                                        .getMessaging_profile_id());
                                        editor.putString(
                                                Constants.PREF_KEY_BUSINESS_NAME,
                                                response.getResponseData().getBusiness_name());
                                        editor.putString(
                                                Constants.PREF_KEY_TOKEN,
                                                response.getResponseData().getToken());
                                        editor.apply();

                                        /*tartActivity(
                                                new Intent(
                                                                SignInActivity.this,
                                                                VerificationActivity.class)
                                                        .addFlags(
                                                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                        | Intent
                                                                                .FLAG_ACTIVITY_NEW_TASK));
                                        finish();*/

                                        configureAccount(
                                                response.getResponseData().getPbx_number(),
                                                response.getResponseData().getPbx_hostname(),
                                                response.getResponseData().getPbx_password(),
                                                response.getResponseData().getBusiness_name());

                                    } else {
                                        Toast.makeText(
                                                        SignInActivity.this,
                                                        response.getMessage(),
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    pb.dismiss();
                                }

                                @Override
                                public void onComplete() {
                                    pb.dismiss();
                                }

                                @Override
                                public void onFailure(ApiException e) {
                                    pb.dismiss();
                                    Toast.makeText(
                                                    SignInActivity.this,
                                                    "API Error: " + e,
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pb.dismiss();
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result =
                ContextCompat.checkSelfPermission(
                        SignInActivity.this, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(
                SignInActivity.this,
                new String[] {
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_SYNC_SETTINGS,
                    Manifest.permission.WRITE_CONTACTS
                },
                101);
    }

    private void configureAccount(
            String Username, String Domain, String Password, String Display_Name) {
        Core core = LinphoneManager.getCore();
        if (core != null) {
            Log.i("[Generic Connection Assistant] Reloading configuration with default");
            reloadDefaultAccountCreatorConfig();
        }

        AccountCreator accountCreator = getAccountCreator();
        //        accountCreator.setUsername(mUsername.getText().toString());
        accountCreator.setUsername(Username);
        accountCreator.setDomain(Domain);
        accountCreator.setPassword(Password);
        accountCreator.setDisplayName(Display_Name);
        accountCreator.setTransport(TransportType.Tls);

        /*switch (mTransport.getCheckedRadioButtonId()) {
            case R.id.transport_udp:
                accountCreator.setTransport(TransportType.Udp);
                break;
            case R.id.transport_tcp:
                accountCreator.setTransport(TransportType.Tcp);
                break;
            case R.id.transport_tls:
                accountCreator.setTransport(TransportType.Tls);
                break;
        }*/

        createProxyConfigAndLeaveAssistant(true);
    }

    public void createProxyConfigAndLeaveAssistant(boolean isGenericAccount) {
        Core core = LinphoneManager.getCore();
        boolean useLinphoneDefaultValues =
                getString(R.string.default_domain).equals(getAccountCreator().getDomain());

        if (isGenericAccount) {
            if (useLinphoneDefaultValues) {
                Log.i(
                        "[Assistant] Default domain found for generic connection, reloading configuration");
                core.loadConfigFromXml(
                        LinphonePreferences.instance().getLinphoneDynamicConfigFile());
            } else {
                Log.i("[Assistant] Third party domain found, keeping default values");
            }
        }

        ProxyConfig proxyConfig = getAccountCreator().createProxyConfig();

        if (isGenericAccount) {
            if (useLinphoneDefaultValues) {
                // Restore default values
                Log.i("[Assistant] Restoring default assistant configuration");
                core.loadConfigFromXml(
                        LinphonePreferences.instance().getDefaultDynamicConfigFile());
            } else {
                // If this isn't a sip.linphone.org account, disable push notifications and enable
                // service notification, otherwise incoming calls won't work (most probably)
                if (proxyConfig != null) {
                    proxyConfig.setPushNotificationAllowed(false);
                }
                Log.w(
                        "[Assistant] Unknown domain used, push probably won't work, enable service mode");
                LinphonePreferences.instance().setServiceNotificationVisibility(true);
                LinphoneContext.instance().getNotificationManager().startForeground();
            }
        }

        if (proxyConfig == null) {
            Log.e("[Assistant] Account creator couldn't create proxy config");
            // TODO: display error message
        } else {
            if (proxyConfig.getDialPrefix() == null) {
                DialPlan dialPlan = getDialPlanForCurrentCountry();
                if (dialPlan != null) {
                    proxyConfig.setDialPrefix(dialPlan.getCountryCallingCode());
                }
            }

            LinphonePreferences.instance().firstLaunchSuccessful();
            goToLinphoneActivity();
        }
    }

    public void reloadDefaultAccountCreatorConfig() {
        Log.i("[Assistant] Reloading configuration with default");
        reloadAccountCreatorConfig(LinphonePreferences.instance().getDefaultDynamicConfigFile());
    }

    void reloadLinphoneAccountCreatorConfig() {
        Log.i("[Assistant] Reloading configuration with specifics");
        reloadAccountCreatorConfig(LinphonePreferences.instance().getLinphoneDynamicConfigFile());
    }

    private void reloadAccountCreatorConfig(String path) {
        Core core = LinphoneManager.getCore();
        if (core != null) {
            core.loadConfigFromXml(path);
            AccountCreator accountCreator = getAccountCreator();
            accountCreator.reset();
            accountCreator.setLanguage(Locale.getDefault().getLanguage());
        }
    }

    public DialPlan getDialPlanForCurrentCountry() {
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String countryIso = tm.getNetworkCountryIso();
            return getDialPlanFromCountryCode(countryIso);
        } catch (Exception e) {
            Log.e("[Assistant] " + e);
        }
        return null;
    }

    private DialPlan getDialPlanFromCountryCode(String countryCode) {
        if (countryCode == null || countryCode.isEmpty()) return null;

        for (DialPlan c : Factory.instance().getDialPlans()) {
            if (countryCode.equalsIgnoreCase(c.getIsoCountryCode())) return c;
        }
        return null;
    }

    public AccountCreator getAccountCreator() {
        return LinphoneManager.getInstance().getAccountCreator();
    }

    public void goToLinphoneActivity() {
        boolean needsEchoCalibration =
                LinphoneManager.getCore().isEchoCancellerCalibrationRequired();
        boolean echoCalibrationDone =
                LinphonePreferences.instance().isEchoCancellationCalibrationDone();
        Log.i(
                "[Assistant] Echo cancellation calibration required ? "
                        + needsEchoCalibration
                        + ", already done ? "
                        + echoCalibrationDone);

        Intent intent;
        /* if (needsEchoCalibration && !echoCalibrationDone) {
            intent = new Intent(this, EchoCancellerCalibrationAssistantActivity.class);
        } else {
            boolean openH264 = LinphonePreferences.instance().isOpenH264CodecDownloadEnabled();
            boolean codecFound =
                    LinphoneManager.getInstance().getOpenH264DownloadHelper().isCodecFound();
            boolean abiSupported =
                    Version.getCpuAbis().contains("armeabi-v7a")
                            && !Version.getCpuAbis().contains("x86");
            boolean androidVersionOk = Version.sdkStrictlyBelow(Build.VERSION_CODES.M);

            if (openH264 && abiSupported && androidVersionOk && !codecFound) {
                intent = new Intent(this, OpenH264DownloadAssistantActivity.class);
            } else {*/
        SharedPreferences preferences =
                getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.PREF_KEY_LOGGED_IN, true);
        editor.apply();
        intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NO_ANIMATION
                        | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
        // }
        //        }
        startActivity(intent);
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2) h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
