package tel.skylab.skylabtel.ui.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.linphone.settings.SettingsActivity;
import tel.skylab.skylabtel.utils.Constants;

public class SkylabSettingActivity extends AppCompatActivity {

    ImageView iv_on_switch, iv_off_switch;
    LinearLayout ll_adv_settings, ll_logout, ll_privacy_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skylab_setting);

        iv_on_switch = findViewById(R.id.iv_on_switch);
        iv_off_switch = findViewById(R.id.iv_off_switch);
        ll_adv_settings = findViewById(R.id.ll_adv_settings);
        ll_privacy_btn = findViewById(R.id.ll_privacy_btn);
        ll_logout = findViewById(R.id.ll_logout);

        ((ImageView) findViewById(R.id.back_btn))
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });

        iv_off_switch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_off_switch.setVisibility(View.GONE);
                        iv_on_switch.setVisibility(View.VISIBLE);
                    }
                });

        iv_on_switch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iv_on_switch.setVisibility(View.GONE);
                        iv_off_switch.setVisibility(View.VISIBLE);
                    }
                });

        ll_adv_settings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(SkylabSettingActivity.this, SettingsActivity.class));
                    }
                });

        ll_privacy_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*startActivity(
                        new Intent(
                                SkylabSettingActivity.this,
                                PrivacyPolicyWebActivity.class));*/
                        String url = "https://skylab.tel/terms.php";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

        ll_logout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ClearApplicationData().execute();
                        startActivity(
                                new Intent(SkylabSettingActivity.this, SignInActivity.class)
                                        .addFlags(
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                        | Intent.FLAG_ACTIVITY_NEW_TASK));
                        //                        clearAppData();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class ClearApplicationData extends AsyncTask<Void, String, String> {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog =
                    new ProgressDialog(
                            SkylabSettingActivity.this); // Change MainActivity as per your activity
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
            Toast.makeText(SkylabSettingActivity.this, "Logout Successfully.", Toast.LENGTH_SHORT)
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

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager) getSystemService(ACTIVITY_SERVICE))
                        .clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
