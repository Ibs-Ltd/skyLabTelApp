package tel.skylab.skylabtel.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import tel.skylab.skylabtel.R;

public class VerificationActivity extends AppCompatActivity {

    EditText et1, et2, et3, et4;
    TextView tv_continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        tv_continue_btn = findViewById(R.id.tv_continue_btn);

        et1.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 1) {
                            et2.requestFocus();
                        } else if (s.length() == 0) {
                            et1.clearFocus();
                        }
                    }
                });

        et2.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 1) {
                            et3.requestFocus();
                        } else if (s.length() == 0) {
                            et1.requestFocus();
                        }
                    }
                });

        et3.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 1) {
                            et4.requestFocus();
                        } else if (s.length() == 0) {
                            et2.requestFocus();
                        }
                    }
                });

        et4.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 1) {
                            et4.clearFocus();
                        } else if (s.length() == 0) {
                            et3.requestFocus();
                        }
                    }
                });

        tv_continue_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                                new Intent(VerificationActivity.this, PermissionsActivity.class)
                                        .addFlags(
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                        | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });
    }
}
