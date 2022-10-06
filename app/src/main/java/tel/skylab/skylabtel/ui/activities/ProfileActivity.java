package tel.skylab.skylabtel.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.utils.Constants;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout change_password_btn;
    ImageView back_btn;
    TextView businessName, tv_unique_number, tv_email, tv_businessName;
    String BusinessName;
    CardView cv_renew_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        change_password_btn = findViewById(R.id.change_password_btn);
        back_btn = findViewById(R.id.back_btn);
        businessName = findViewById(R.id.businessName);
        tv_unique_number = findViewById(R.id.tv_unique_number);
        tv_email = findViewById(R.id.tv_email);
        tv_businessName = findViewById(R.id.tv_businessName);
        cv_renew_btn = findViewById(R.id.cv_renew_btn);

        SharedPreferences preferences =
                getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
        businessName.setText(preferences.getString(Constants.PREF_KEY_BUSINESS_NAME, "NA"));
        tv_unique_number.setText(preferences.getString(Constants.PREF_KEY_PBX_NUMBER, "NA"));
        tv_email.setText(preferences.getString(Constants.PREF_KEY_CUSTOMER_EMAIL, "NA"));
        tv_businessName.setText(preferences.getString(Constants.PREF_KEY_BUSINESS_NAME, "NA"));

        BusinessName = preferences.getString(Constants.PREF_KEY_BUSINESS_NAME, "NA");
        if (BusinessName.equalsIgnoreCase("")) {
            businessName.setText("NA");
            tv_businessName.setText("NA");
        }

        change_password_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(ProfileActivity.this, ChangePasswordActivity.class));
                    }
                });

        cv_renew_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://skylab.tel";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

        ((ImageView) findViewById(R.id.back_btn))
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
