package tel.skylab.skylabtel.ui.activities;

import static tel.skylab.skylabtel.utils.Constants.skyLabProxyHost;
import static tel.skylab.skylabtel.utils.Constants.skyLabProxyPort;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Route;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.linphone.core.MediaEncryption;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.apis.api.API_Interface;
import tel.skylab.skylabtel.apis.api.ServerResponse;
import tel.skylab.skylabtel.apis.request.GetProfileRequest;
import tel.skylab.skylabtel.apis.response.GetProfileResponseModel;
import tel.skylab.skylabtel.apis.service.GetProfileService;
import tel.skylab.skylabtel.apis.web.ApiCallback;
import tel.skylab.skylabtel.apis.web.ApiException;
import tel.skylab.skylabtel.apis.web.UnsafeOkHttpClient;
import tel.skylab.skylabtel.linphone.BackgroundService;
import tel.skylab.skylabtel.linphone.LinphoneManager;
import tel.skylab.skylabtel.linphone.chat.ChatActivity;
import tel.skylab.skylabtel.linphone.contacts.ContactsActivity;
import tel.skylab.skylabtel.linphone.fragments.StatusBarFragment;
import tel.skylab.skylabtel.linphone.history.HistoryActivity;
import tel.skylab.skylabtel.linphone.service.LinphoneService;
import tel.skylab.skylabtel.linphone.service.ServiceWaitThread;
import tel.skylab.skylabtel.linphone.service.ServiceWaitThreadListener;
import tel.skylab.skylabtel.linphone.settings.LinphonePreferences;
import tel.skylab.skylabtel.models.FragmentModels;
import tel.skylab.skylabtel.ui.fragments.ChatListFragment;
import tel.skylab.skylabtel.ui.fragments.ContactListFragment;
import tel.skylab.skylabtel.ui.fragments.HomeFragment;
import tel.skylab.skylabtel.ui.fragments.MainChatFragment;
import tel.skylab.skylabtel.ui.fragments.RecentCallsFragment;
import tel.skylab.skylabtel.ui.fragments.SkyDialerFragment;
import tel.skylab.skylabtel.utils.Constants;

public class DashboardActivity extends AppCompatActivity implements ServiceWaitThreadListener {

    LinearLayout setting_btn, profile_btn;
    ImageView iv_keypad_btn, iv_recent_contact_btn, iv_chat_btn, iv_contact_btn;
    ImageView iv_select_keypad_btn,
            iv_select_recent_contact_btn,
            iv_select_chat_btn,
            iv_select_contact_btn,
            iv_home_btn;
    FragmentManager manager;
    LinearLayout statusBarLayout;
    private LinphonePreferences mPrefs;
    ArrayList<FragmentModels> frgList = new ArrayList<>();
    HomeFragment homeFragment; // 0
    RecentCallsFragment recentCallsFragment; // 1
    ChatListFragment chatListFragment; // 2
    ContactListFragment contactListFragment; // 3
    SkyDialerFragment skyDialerFragment; // 4
    MainChatFragment mainChatFragment; // 5

    private StatusBarFragment mStatusBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setting_btn = findViewById(R.id.setting_btn);
        profile_btn = findViewById(R.id.profile_btn);
        iv_keypad_btn = findViewById(R.id.iv_keypad_btn);
        iv_recent_contact_btn = findViewById(R.id.iv_recent_contact_btn);
        iv_chat_btn = findViewById(R.id.iv_chat_btn);
        iv_contact_btn = findViewById(R.id.iv_contact_btn);
        iv_select_keypad_btn = findViewById(R.id.iv_select_keypad_btn);
        iv_select_recent_contact_btn = findViewById(R.id.iv_select_recent_contact_btn);
        iv_select_chat_btn = findViewById(R.id.iv_select_chat_btn);
        iv_select_contact_btn = findViewById(R.id.iv_select_contact_btn);
        statusBarLayout = findViewById(R.id.statusBarLayout);
        iv_home_btn = findViewById(R.id.iv_home_btn);

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }

        startService(new Intent(this, BackgroundService.class));

        mStatusBarFragment =
                (StatusBarFragment) getFragmentManager().findFragmentById(R.id.status_fragment);

        mPrefs = LinphonePreferences.instance();
        mPrefs.setMediaEncryption(MediaEncryption.SRTP);

        manager = getSupportFragmentManager();
        setFragment(0, true);

        // Creating an AsyncTask object to retrieve
        hitLoginApi hit_login_api = new hitLoginApi();
        // Starting the AsyncTask process to retrieve
        hit_login_api.execute();

        /* // Creating an AsyncTask object to retrieve
        setProxyClass setProxy = new setProxyClass();

        // Starting the AsyncTask process to retrieve
        setProxy.execute();*/

        setting_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(DashboardActivity.this, SkylabSettingActivity.class));
                    }
                });

        profile_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                    }
                });

        statusBarLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStart();
                    }
                });

        iv_home_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_keypad_btn.setVisibility(View.VISIBLE);
                        iv_select_keypad_btn.setVisibility(View.GONE);
                        iv_recent_contact_btn.setVisibility(View.VISIBLE);
                        iv_select_recent_contact_btn.setVisibility(View.GONE);
                        iv_chat_btn.setVisibility(View.VISIBLE);
                        iv_select_chat_btn.setVisibility(View.GONE);
                        iv_contact_btn.setVisibility(View.VISIBLE);
                        iv_select_contact_btn.setVisibility(View.GONE);
                        setFragment(0, true);
                        /*startActivity(
                        new Intent(DashboardActivity.this, LinphoneLauncherActivity.class));*/

                        /*// Creating an AsyncTask object to retrieve
                        setProxyClass setProxy= new setProxyClass();

                        // Starting the AsyncTask process to retrieve
                        setProxy.execute();*/
                    }
                });

        iv_keypad_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_keypad_btn.setVisibility(View.GONE);
                        iv_select_keypad_btn.setVisibility(View.VISIBLE);
                        iv_recent_contact_btn.setVisibility(View.VISIBLE);
                        iv_select_recent_contact_btn.setVisibility(View.GONE);
                        iv_chat_btn.setVisibility(View.VISIBLE);
                        iv_select_chat_btn.setVisibility(View.GONE);
                        iv_contact_btn.setVisibility(View.VISIBLE);
                        iv_select_contact_btn.setVisibility(View.GONE);
                        setFragment(4, true);
                    }
                });

        iv_recent_contact_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_keypad_btn.setVisibility(View.VISIBLE);
                        iv_select_keypad_btn.setVisibility(View.GONE);
                        iv_recent_contact_btn.setVisibility(View.GONE);
                        iv_select_recent_contact_btn.setVisibility(View.VISIBLE);
                        iv_chat_btn.setVisibility(View.VISIBLE);
                        iv_select_chat_btn.setVisibility(View.GONE);
                        iv_contact_btn.setVisibility(View.VISIBLE);
                        iv_select_contact_btn.setVisibility(View.GONE);
                        setFragment(1, true);
                    }
                });

        iv_chat_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_keypad_btn.setVisibility(View.VISIBLE);
                        iv_select_keypad_btn.setVisibility(View.GONE);
                        iv_recent_contact_btn.setVisibility(View.VISIBLE);
                        iv_select_recent_contact_btn.setVisibility(View.GONE);
                        iv_chat_btn.setVisibility(View.GONE);
                        iv_select_chat_btn.setVisibility(View.VISIBLE);
                        iv_contact_btn.setVisibility(View.VISIBLE);
                        iv_select_contact_btn.setVisibility(View.GONE);
                        setFragment(2, true);
                    }
                });

        iv_contact_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_keypad_btn.setVisibility(View.VISIBLE);
                        iv_select_keypad_btn.setVisibility(View.GONE);
                        iv_recent_contact_btn.setVisibility(View.VISIBLE);
                        iv_select_recent_contact_btn.setVisibility(View.GONE);
                        iv_chat_btn.setVisibility(View.VISIBLE);
                        iv_select_chat_btn.setVisibility(View.GONE);
                        iv_contact_btn.setVisibility(View.GONE);
                        iv_select_contact_btn.setVisibility(View.VISIBLE);
                        setFragment(3, true);
                    }
                });
    }

    public void setFragment(int i, boolean isAdd) {

        if (i == 0) {

            if (homeFragment == null) {
                homeFragment = new HomeFragment();
                homeFragment.dashboard = this;
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.main_frame, homeFragment, "HomeFragment").commit();
            }
            if (homeFragment != null) {
                if (isAdd) {
                    for (int k = 0; k < frgList.size(); k++) {
                        if (frgList.get(k).getFragmentNumber() == 0) {
                            frgList.remove(k);
                            break;
                        }
                    }
                    frgList.add(new FragmentModels(0, homeFragment));
                }
                manager.beginTransaction().show(homeFragment).commit();
            }
            if (recentCallsFragment != null)
                manager.beginTransaction().hide(recentCallsFragment).commit();
            if (chatListFragment != null)
                manager.beginTransaction().hide(chatListFragment).commit();
            if (contactListFragment != null)
                manager.beginTransaction().hide(contactListFragment).commit();
            if (skyDialerFragment != null)
                manager.beginTransaction().hide(skyDialerFragment).commit();
            if (mainChatFragment != null)
                manager.beginTransaction().hide(mainChatFragment).commit();
            /*if (myCart != null)
                manager.beginTransaction().hide(myCart).commit();
            if (myAddresses != null)
                manager.beginTransaction().hide(myAddresses).commit();
            if (myWishlist != null)
                manager.beginTransaction().hide(myWishlist).commit();
            if (couponOffers != null)
                manager.beginTransaction().hide(couponOffers).commit();
            if (customerSupport != null)
                manager.beginTransaction().hide(customerSupport).commit();
            if (aboutGracer != null)
                manager.beginTransaction().hide(aboutGracer).commit();
            if (productView != null)
                manager.beginTransaction().hide(productView).commit();
            if (profile != null)
                manager.beginTransaction().hide(profile).commit();
            if (notifications != null)
                manager.beginTransaction().hide(notifications).commit();
            if (addNewAddress != null)
                manager.beginTransaction().hide(addNewAddress).commit();
            if (addMoney != null)
                manager.beginTransaction().hide(addMoney).commit();
            if (faqFragment != null)
                manager.beginTransaction().hide(faqFragment).commit();
            if (orderPlaced != null)
                manager.beginTransaction().hide(orderPlaced).commit();
            if (productList != null)
                manager.beginTransaction().hide(productList).commit();
            if (orderDetails != null)
                manager.beginTransaction().hide(orderDetails).commit();*/
        } else if (i == 1) {
            if (recentCallsFragment == null) {
                recentCallsFragment = new RecentCallsFragment();
                recentCallsFragment.dashboard = this;
                FragmentTransaction transaction = manager.beginTransaction();
                transaction
                        .add(R.id.main_frame, recentCallsFragment, "RecentCallsFragment")
                        .commit();
            }
            if (recentCallsFragment != null) {
                if (isAdd) {
                    for (int k = 0; k < frgList.size(); k++) {
                        if (frgList.get(k).getFragmentNumber() == 1) {
                            frgList.remove(k);
                            break;
                        }
                    }
                    frgList.add(new FragmentModels(1, recentCallsFragment));
                }
                manager.beginTransaction().show(recentCallsFragment).commit();
            }
            if (homeFragment != null) manager.beginTransaction().hide(homeFragment).commit();
            if (contactListFragment != null)
                manager.beginTransaction().hide(contactListFragment).commit();
            if (chatListFragment != null)
                manager.beginTransaction().hide(chatListFragment).commit();
            if (skyDialerFragment != null)
                manager.beginTransaction().hide(skyDialerFragment).commit();
            if (mainChatFragment != null)
                manager.beginTransaction().hide(mainChatFragment).commit();
        } else if (i == 2) {
            if (chatListFragment == null) {
                chatListFragment = new ChatListFragment();
                chatListFragment.dashboard = this;
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.main_frame, chatListFragment, "ChatListFragment").commit();
            }
            if (chatListFragment != null) {
                if (isAdd) {
                    for (int k = 0; k < frgList.size(); k++) {
                        if (frgList.get(k).getFragmentNumber() == 2) {
                            frgList.remove(k);
                            break;
                        }
                    }
                    frgList.add(new FragmentModels(2, chatListFragment));
                }
                manager.beginTransaction().show(chatListFragment).commit();
            }
            if (homeFragment != null) manager.beginTransaction().hide(homeFragment).commit();
            if (contactListFragment != null)
                manager.beginTransaction().hide(contactListFragment).commit();
            if (recentCallsFragment != null)
                manager.beginTransaction().hide(recentCallsFragment).commit();
            if (skyDialerFragment != null)
                manager.beginTransaction().hide(skyDialerFragment).commit();
            if (mainChatFragment != null)
                manager.beginTransaction().hide(mainChatFragment).commit();
        } else if (i == 3) {
            if (contactListFragment == null) {
                contactListFragment = new ContactListFragment();
                contactListFragment.dashboard = this;
                FragmentTransaction transaction = manager.beginTransaction();
                transaction
                        .add(R.id.main_frame, contactListFragment, "ContactListFragment")
                        .commit();
            }
            if (contactListFragment != null) {
                if (isAdd) {
                    for (int k = 0; k < frgList.size(); k++) {
                        if (frgList.get(k).getFragmentNumber() == 3) {
                            frgList.remove(k);
                            break;
                        }
                    }
                    frgList.add(new FragmentModels(3, contactListFragment));
                }
                manager.beginTransaction().show(contactListFragment).commit();
            }
            if (homeFragment != null) manager.beginTransaction().hide(homeFragment).commit();
            if (chatListFragment != null)
                manager.beginTransaction().hide(chatListFragment).commit();
            if (recentCallsFragment != null)
                manager.beginTransaction().hide(recentCallsFragment).commit();
            if (skyDialerFragment != null)
                manager.beginTransaction().hide(skyDialerFragment).commit();
            if (mainChatFragment != null)
                manager.beginTransaction().hide(mainChatFragment).commit();
        } else if (i == 4) {
            if (skyDialerFragment == null) {
                skyDialerFragment = new SkyDialerFragment();
                skyDialerFragment.dashboard = this;
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.main_frame, skyDialerFragment, "SkyDialerFragment").commit();
            }
            if (skyDialerFragment != null) {
                if (isAdd) {
                    for (int k = 0; k < frgList.size(); k++) {
                        if (frgList.get(k).getFragmentNumber() == 4) {
                            frgList.remove(k);
                            break;
                        }
                    }
                    frgList.add(new FragmentModels(4, skyDialerFragment));
                }
                manager.beginTransaction().show(skyDialerFragment).commit();
            }
            if (homeFragment != null) manager.beginTransaction().hide(homeFragment).commit();
            if (chatListFragment != null)
                manager.beginTransaction().hide(chatListFragment).commit();
            if (recentCallsFragment != null)
                manager.beginTransaction().hide(recentCallsFragment).commit();
            if (contactListFragment != null)
                manager.beginTransaction().hide(contactListFragment).commit();
            if (mainChatFragment != null)
                manager.beginTransaction().hide(mainChatFragment).commit();
        } else if (i == 5) {
            if (mainChatFragment == null) {
                mainChatFragment = new MainChatFragment();
                mainChatFragment.dashboard = this;
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.main_frame, mainChatFragment, "MainChatFragment").commit();
            }
            if (mainChatFragment != null) {
                if (isAdd) {
                    for (int k = 0; k < frgList.size(); k++) {
                        if (frgList.get(k).getFragmentNumber() == 5) {
                            frgList.remove(k);
                            break;
                        }
                    }
                    frgList.add(new FragmentModels(5, mainChatFragment));
                }
                manager.beginTransaction().show(mainChatFragment).commit();
            }
            if (homeFragment != null) manager.beginTransaction().hide(homeFragment).commit();
            if (chatListFragment != null)
                manager.beginTransaction().hide(chatListFragment).commit();
            if (recentCallsFragment != null)
                manager.beginTransaction().hide(recentCallsFragment).commit();
            if (contactListFragment != null)
                manager.beginTransaction().hide(contactListFragment).commit();
            if (skyDialerFragment != null)
                manager.beginTransaction().hide(skyDialerFragment).commit();
        }
    }

    private class setProxyClass extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            OkHttpClient client =
                    new OkHttpClient.Builder()
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .proxy(
                                    new Proxy(
                                            Proxy.Type.HTTP,
                                            new InetSocketAddress(
                                                    skyLabProxyHost, skyLabProxyPort)))
                            .proxyAuthenticator(proxyAuthenticator)
                            .build();

            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

            Retrofit retrofit =
                    new Retrofit.Builder()
                            .baseUrl("https://api.skylab.tel/api/")
                            .client(okHttpClient)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

            API_Interface api = retrofit.create(API_Interface.class);
            Call<ServerResponse> call = api.getCotegory();
            call.enqueue(
                    new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(
                                Call<ServerResponse> call, Response<ServerResponse> response) {
                            try {
                                JSONObject jsonObj =
                                        new JSONObject(new Gson().toJson(response.body()));
                                String message = jsonObj.getString("message");
                                int codes = jsonObj.getInt("code");
                                boolean status = jsonObj.getBoolean("error");

                                if (!status) {
                                    //                        List<ServerResponse.Data> categoryList
                                    // = response.body().data;
                                    Toast.makeText(
                                                    DashboardActivity.this,
                                                    "If Condition: \n" + message + "\n" + codes,
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                } else {
                                    Toast.makeText(
                                                    DashboardActivity.this,
                                                    "Else Condition: \n" + message + "\n" + codes,
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            } catch (Exception ee) {

                                Toast.makeText(DashboardActivity.this, "" + ee, Toast.LENGTH_SHORT)
                                        .show();
                                ee.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            Toast.makeText(DashboardActivity.this, "" + t, Toast.LENGTH_SHORT)
                                    .show();
                            t.printStackTrace();
                        }
                    });

            return null;
        }
    }

    Authenticator proxyAuthenticator =
            new Authenticator() {
                @Override
                public Request authenticate(@Nullable Route route, okhttp3.Response response)
                        throws IOException {
                    SharedPreferences preferences =
                            DashboardActivity.this.getSharedPreferences(
                                    Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
                    String user = preferences.getString(Constants.skyLabUsername, "");
                    String pass = preferences.getString(Constants.skyLabPassword, "");
                    String credential = Credentials.basic(user, pass);
                    return response.request()
                            .newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            };

    @Override
    protected void onStart() {
        super.onStart();

        if (LinphoneService.isReady()) {
            LinphoneManager.getInstance().changeStatusToOnline();
            startActivity(
                    new Intent(DashboardActivity.this, DashboardActivity.class)
                            .addFlags(
                                    Intent.FLAG_ACTIVITY_NO_ANIMATION
                                            | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            //            onServiceReady();
        } else {
            try {
                startService(new Intent().setClass(DashboardActivity.this, LinphoneService.class));
                new ServiceWaitThread(this).start();
            } catch (IllegalStateException ise) {
                Log.e("SkyLab", "Exception raised while starting service: " + ise);
            }
        }
    }

    @Override
    public void onServiceReady() {
        final Class<? extends Activity> classToStart;

        boolean useFirstLoginActivity =
                getResources().getBoolean(R.bool.display_account_assistant_at_first_start);
        if (useFirstLoginActivity && LinphonePreferences.instance().isFirstLaunch()) {
            classToStart = DashboardActivity.class;
        } else {
            if (getIntent().getExtras() != null) {
                String activity = getIntent().getExtras().getString("Activity", null);
                if (ChatActivity.NAME.equals(activity)) {
                    classToStart = DashboardActivity.class;
                } else if (HistoryActivity.NAME.equals(activity)) {
                    classToStart = DashboardActivity.class;
                } else if (ContactsActivity.NAME.equals(activity)) {
                    classToStart = DashboardActivity.class;
                } else {
                    classToStart = DashboardActivity.class;
                }
            } else {
                classToStart = DashboardActivity.class;
            }
        }

        Intent intent = new Intent();
        intent.setClass(DashboardActivity.this, classToStart);
        if (getIntent() != null && getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
        }
        intent.setAction(getIntent().getAction());
        intent.setType(getIntent().getType());
        intent.setData(getIntent().getData());
        startActivity(intent);

        LinphoneManager.getInstance().changeStatusToOnline();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }

    private boolean checkIfAlreadyhavePermission() {
        int result =
                ContextCompat.checkSelfPermission(
                        DashboardActivity.this, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(
                DashboardActivity.this,
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

    private class hitLoginApi extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences preferences =
                    DashboardActivity.this.getSharedPreferences(
                            Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
            String Username = preferences.getString(Constants.PREF_KEY_Username, "");
            String Password = preferences.getString(Constants.PREF_KEY_Password, "");

            if (!Username.equalsIgnoreCase("")) {
                GetProfileRequest request = new GetProfileRequest();
                request.setEmail(Username);
                request.setPassword(Password);

                new GetProfileService(getApplicationContext())
                        .execute(
                                request,
                                new ApiCallback<GetProfileResponseModel>() {
                                    @Override
                                    public void onSuccess(
                                            Call<GetProfileResponseModel> call,
                                            GetProfileResponseModel response) {

                                        if (response.getCodes() == 0) {

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

                                            if (response.getResponseData()
                                                            .getSubscription_active()
                                                            .equalsIgnoreCase("0")
                                                    || response.getResponseData()
                                                            .getSubscription_active()
                                                            .equalsIgnoreCase("")) {
                                                new ClearApplicationData().execute();
                                                startActivity(
                                                        new Intent(
                                                                        DashboardActivity.this,
                                                                        SignInActivity.class)
                                                                .addFlags(
                                                                        Intent
                                                                                        .FLAG_ACTIVITY_CLEAR_TASK
                                                                                | Intent
                                                                                        .FLAG_ACTIVITY_NEW_TASK));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onComplete() {}

                                    @Override
                                    public void onFailure(ApiException e) {
                                        e.printStackTrace();
                                    }
                                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class ClearApplicationData extends AsyncTask<Void, String, String> {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog =
                    new ProgressDialog(
                            DashboardActivity.this); // Change MainActivity as per your activity
            mDialog.setMessage("Logout...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        protected String doInBackground(Void... urls) {
            SharedPreferences.Editor editor =
                    getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                            .edit();
            editor.clear();
            editor.apply();
            File cacheDir = getApplicationContext().getCacheDir();
            File appDir = new File(cacheDir.getParent());
            deleteRecursive(appDir);
            return "";
        }

        protected void onPostExecute(String result) {
            mDialog.dismiss();
            Toast.makeText(DashboardActivity.this, "Logout Successfully.", Toast.LENGTH_SHORT)
                    .show();
            finish(); // Activity finish
        }
    }

    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
