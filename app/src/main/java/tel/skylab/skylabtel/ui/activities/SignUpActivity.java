package tel.skylab.skylabtel.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import tel.skylab.skylabtel.R;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout ll_login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ll_login_btn = findViewById(R.id.ll_login_btn);

        ll_login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    }
                });
    }
}
