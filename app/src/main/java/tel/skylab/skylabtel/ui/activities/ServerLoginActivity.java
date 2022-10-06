package tel.skylab.skylabtel.ui.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import org.linphone.core.AccountCreator;
import org.linphone.core.Core;
import org.linphone.core.TransportType;
import org.linphone.core.tools.Log;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.linphone.LinphoneManager;
import tel.skylab.skylabtel.linphone.assistant.AssistantActivity;

public class ServerLoginActivity extends AssistantActivity implements TextWatcher {

    private TextView mLogin;
    private EditText mUsername, mPassword, mDomain, mDisplayName;
    private RadioGroup mTransport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_login);

        mLogin = findViewById(R.id.assistant_login);
        mLogin.setEnabled(false);
        mLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        configureAccount();
                    }
                });

        mUsername = findViewById(R.id.assistant_username);
        mUsername.addTextChangedListener(this);
        mDisplayName = findViewById(R.id.assistant_display_name);
        mDisplayName.addTextChangedListener(this);
        mPassword = findViewById(R.id.assistant_password);
        mPassword.addTextChangedListener(this);
        mDomain = findViewById(R.id.assistant_domain);
        mDomain.addTextChangedListener(this);
        mTransport = findViewById(R.id.assistant_transports);
    }

    private void configureAccount() {
        Core core = LinphoneManager.getCore();
        if (core != null) {
            Log.i("[Generic Connection Assistant] Reloading configuration with default");
            reloadDefaultAccountCreatorConfig();
        }

        AccountCreator accountCreator = getAccountCreator();
        accountCreator.setUsername(mUsername.getText().toString());
        accountCreator.setDomain(mDomain.getText().toString());
        accountCreator.setPassword(mPassword.getText().toString());
        accountCreator.setDisplayName(mDisplayName.getText().toString());

        switch (mTransport.getCheckedRadioButtonId()) {
            case R.id.transport_udp:
                accountCreator.setTransport(TransportType.Udp);
                break;
            case R.id.transport_tcp:
                accountCreator.setTransport(TransportType.Tcp);
                break;
            case R.id.transport_tls:
                accountCreator.setTransport(TransportType.Tls);
                break;
        }

        createProxyConfigAndLeaveAssistant(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mLogin.setEnabled(
                !mUsername.getText().toString().isEmpty()
                        && !mDomain.getText().toString().isEmpty());
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
