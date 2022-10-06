package tel.skylab.skylabtel.ui.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.apis.request.SendMsgRequest;
import tel.skylab.skylabtel.apis.response.SendMsgResponseModel;
import tel.skylab.skylabtel.apis.service.SendMsgService;
import tel.skylab.skylabtel.apis.web.ApiCallback;
import tel.skylab.skylabtel.apis.web.ApiException;
import tel.skylab.skylabtel.utils.Constants;

public class SkylabSendMessageActivity extends AppCompatActivity {

    ImageView back_btn;
    EditText et_to_number, et_message;
    RadioButton slct_business_name, slct_virtual_number;
    TextView tv_send_btn;
    String select_btn_status = null, msg, ToNumber;
    RadioGroup radioGroup;
    ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skylab_send_message);

        back_btn = findViewById(R.id.back_btn);
        et_to_number = findViewById(R.id.et_to_number);
        et_message = findViewById(R.id.et_message);
        slct_business_name = findViewById(R.id.slct_business_name);
        slct_virtual_number = findViewById(R.id.slct_virtual_number);
        tv_send_btn = findViewById(R.id.tv_send_btn);
        radioGroup = findViewById(R.id.radioGroup);

        if (getIntent() != null) {
            String number = getIntent().getExtras().getString("caller_number", "");
            et_to_number.setText(number);
        }

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        SharedPreferences preferences =
                                getSharedPreferences(
                                        Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
                        if (checkedId == R.id.slct_business_name) {
                            select_btn_status =
                                    preferences.getString(Constants.PREF_KEY_BUSINESS_NAME, "NA");
                            /*Toast.makeText(
                                    SkylabSendMessageActivity.this,
                                    "" + select_btn_status,
                                    Toast.LENGTH_SHORT)
                            .show();*/
                            showDailog();
                        } else if (checkedId == R.id.slct_virtual_number) {
                            select_btn_status =
                                    preferences.getString(Constants.PREF_KEY_PBX_NUMBER, "NA");
                            /*Toast.makeText(
                                    SkylabSendMessageActivity.this,
                                    "" + select_btn_status,
                                    Toast.LENGTH_SHORT)
                            .show();*/
                        }
                    }
                });

        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        tv_send_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_to_number.getText().toString().trim().equalsIgnoreCase("")) {
                            et_to_number.setError("Please enter number");
                        } else if (et_message.getText().toString().trim().equalsIgnoreCase("")) {
                            et_message.setError("Please type message here");
                        } else if (select_btn_status == null) {
                            Toast.makeText(
                                            SkylabSendMessageActivity.this,
                                            "Please select any one BusinessName Or Virtual Number",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            msg = et_message.getText().toString();
                            ToNumber = et_to_number.getText().toString();

                            // Creating an AsyncTask object to retrieve
                            hitSendApi hit_send_api = new hitSendApi();
                            // Starting the AsyncTask process to retrieve
                            hit_send_api.execute();
                        }
                    }
                });
    }

    private class hitSendApi extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb = new ProgressDialog(SkylabSendMessageActivity.this);
            pb.setTitle("Sending Messages");
            pb.setMessage("Please wait.....");
            pb.setCancelable(false);
            pb.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences preferences =
                    getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
            SendMsgRequest request = new SendMsgRequest();
            request.setMessaging_profile_id(
                    preferences.getString(Constants.PREF_KEY_MESSAGING_PROFILE_ID, ""));
            request.setText(msg);
            request.setTo(ToNumber);
            request.setFrom(select_btn_status);

            new SendMsgService(getApplicationContext())
                    .execute(
                            request,
                            new ApiCallback<SendMsgResponseModel>() {
                                @Override
                                public void onSuccess(
                                        Call<SendMsgResponseModel> call,
                                        SendMsgResponseModel response) {

                                    if (response.getCodes() == 0) {

                                        Toast.makeText(
                                                        SkylabSendMessageActivity.this,
                                                        response.getMessage(),
                                                        Toast.LENGTH_SHORT)
                                                .show();

                                    } else {
                                        Toast.makeText(
                                                        SkylabSendMessageActivity.this,
                                                        response.getMessage(),
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }

                                @Override
                                public void onComplete() {}

                                @Override
                                public void onFailure(ApiException e) {

                                    Toast.makeText(
                                                    SkylabSendMessageActivity.this,
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

    private void showDailog() {
        final Dialog dialog = new Dialog(SkylabSendMessageActivity.this);
        dialog.setContentView(R.layout.message_aleart_dialog);
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView cancel_btn = dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
