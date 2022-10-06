package tel.skylab.skylabtel.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.setFullScreen();

        new Handler()
                .postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences preferences =
                                        getSharedPreferences(
                                                Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
                                boolean loggedIn =
                                        preferences.getBoolean(Constants.PREF_KEY_LOGGED_IN, false);
                                if (loggedIn) {
                                    startActivity(
                                            new Intent(SplashActivity.this, DashboardActivity.class)
                                                    .addFlags(
                                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                    | Intent
                                                                            .FLAG_ACTIVITY_NEW_TASK));
                                } else {
                                    startActivity(
                                            new Intent(SplashActivity.this, SignInActivity.class)
                                                    .addFlags(
                                                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                    | Intent
                                                                            .FLAG_ACTIVITY_NEW_TASK));
                                }
                            }
                        },
                        3000);
    }

    protected void setFullScreen() {
        try {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
