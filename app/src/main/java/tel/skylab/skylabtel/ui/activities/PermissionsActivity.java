package tel.skylab.skylabtel.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import tel.skylab.skylabtel.R;

public class PermissionsActivity extends AppCompatActivity {

    ImageView microphone_on,
            microphone_off,
            call_logs_off,
            call_logs_on,
            phone_on,
            phone_off,
            contact_off,
            contact_on;
    TextView tv_cancel_btn, tv_continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        microphone_on = findViewById(R.id.microphone_on);
        microphone_off = findViewById(R.id.microphone_off);
        call_logs_off = findViewById(R.id.call_logs_off);
        call_logs_on = findViewById(R.id.call_logs_on);
        phone_on = findViewById(R.id.phone_on);
        phone_off = findViewById(R.id.phone_off);
        contact_off = findViewById(R.id.contact_off);
        contact_on = findViewById(R.id.contact_on);
        tv_cancel_btn = findViewById(R.id.tv_cancel_btn);
        tv_continue_btn = findViewById(R.id.tv_continue_btn);

        tv_cancel_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishAffinity();
                    }
                });

        tv_continue_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(PermissionsActivity.this, DashboardActivity.class)
                                        .addFlags(
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                        | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
    }
}
