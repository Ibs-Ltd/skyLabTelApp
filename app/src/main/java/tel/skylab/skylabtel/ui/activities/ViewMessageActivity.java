package tel.skylab.skylabtel.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import tel.skylab.skylabtel.R;

public class ViewMessageActivity extends AppCompatActivity {

    ImageView back_btn;
    TextView et_from_number, tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        et_from_number = findViewById(R.id.et_from_number);
        tv_message = findViewById(R.id.tv_message);
        back_btn = findViewById(R.id.back_btn);

        if (getIntent() != null) {
            String Num_From = getIntent().getStringExtra("EXTRA_FROM");
            String Msg = getIntent().getStringExtra("EXTRA_MSG");

            et_from_number.setText(Num_From);
            tv_message.setText(Msg);
        }

        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }
}
