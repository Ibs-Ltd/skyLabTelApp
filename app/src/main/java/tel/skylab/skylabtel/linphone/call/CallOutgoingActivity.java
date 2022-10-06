/*
 * Copyright (c) 2010-2019 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tel.skylab.skylabtel.linphone.call;

import static tel.skylab.skylabtel.utils.Constants.picturePtahSkyLab;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.Call.State;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Reason;
import org.linphone.core.tools.Log;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.linphone.LinphoneContext;
import tel.skylab.skylabtel.linphone.LinphoneManager;
import tel.skylab.skylabtel.linphone.activities.LinphoneGenericActivity;
import tel.skylab.skylabtel.linphone.contacts.ContactsManager;
import tel.skylab.skylabtel.linphone.contacts.LinphoneContact;
import tel.skylab.skylabtel.linphone.contacts.views.ContactAvatar;
import tel.skylab.skylabtel.linphone.settings.LinphonePreferences;
import tel.skylab.skylabtel.linphone.utils.LinphoneUtils;

public class CallOutgoingActivity extends LinphoneGenericActivity implements OnClickListener {
    private TextView mName, mNumber, name, timer;
    private ImageView mMicro;
    private ImageView contact_picture;
    private ImageView mSpeaker;
    private Call mCall;
    private CoreListenerStub mListener;
    private boolean mIsMicMuted, mIsSpeakerEnabled;

    int repeatCounter = 1; // incrementing for every 60 sec
    CountDownTimer tripTimeCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.call_outgoing);

        mName = findViewById(R.id.contact_name);
        name = findViewById(R.id.name);
        timer = findViewById(R.id.timer);
        mNumber = findViewById(R.id.contact_number);
        contact_picture = findViewById(R.id.contact_picture);

        mIsMicMuted = false;
        mIsSpeakerEnabled = false;

        mMicro = findViewById(R.id.micro);
        mMicro.setOnClickListener(this);
        mSpeaker = findViewById(R.id.speaker);
        mSpeaker.setOnClickListener(this);

        ImageView hangUp = findViewById(R.id.outgoing_hang_up);
        hangUp.setOnClickListener(this);

        if (picturePtahSkyLab != null) {
            //            contact_picture.setImageBitmap(picturePtahSkyLab);
            Glide.with(CallOutgoingActivity.this)
                    .asBitmap()
                    .load(picturePtahSkyLab)
                    .circleCrop()
                    .error(R.drawable.businessman)
                    .into(contact_picture);
        } else {
            Glide.with(CallOutgoingActivity.this)
                    .load(R.drawable.businessman)
                    .circleCrop()
                    .into(contact_picture);
        }

        /*CountDown timer = new CountDown(180000, 1000);
        timer.start();*/

        mListener =
                new CoreListenerStub() {
                    @Override
                    public void onCallStateChanged(
                            Core core, Call call, Call.State state, String message) {
                        if (state == State.Error) {
                            // Convert Core message for internalization
                            if (call.getErrorInfo().getReason() == Reason.Declined) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_call_declined),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            } else if (call.getErrorInfo().getReason() == Reason.NotFound) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_user_not_found),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            } else if (call.getErrorInfo().getReason() == Reason.NotAcceptable) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_incompatible_media),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            } else if (call.getErrorInfo().getReason() == Reason.Busy) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_user_busy),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            } else if (message != null) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_unknown) + " - " + message,
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else if (state == State.End) {
                            // Convert Core message for internalization
                            if (call.getErrorInfo().getReason() == Reason.Declined) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_call_declined),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else if (state == State.Connected) {
                            // This is done by the LinphoneContext listener now
                            // startActivity(new Intent(CallOutgoingActivity.this,
                            // CallActivity.class));
                        }

                        if (state == State.End || state == State.Released) {
                            finish();
                        }
                    }
                };
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAndRequestCallPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core core = LinphoneManager.getCore();
        if (core != null) {
            core.addListener(mListener);
        }

        mCall = null;

        // Only one call ringing at a time is allowed
        if (LinphoneManager.getCore() != null) {
            for (Call call : LinphoneManager.getCore().getCalls()) {
                State cstate = call.getState();
                if (State.OutgoingInit == cstate
                        || State.OutgoingProgress == cstate
                        || State.OutgoingRinging == cstate
                        || State.OutgoingEarlyMedia == cstate) {
                    mCall = call;
                    break;
                }
            }
        }
        if (mCall == null) {
            Log.e("[Call Outgoing Activity] Couldn't find outgoing call");
            finish();
            return;
        }

        Address address = mCall.getRemoteAddress();
        LinphoneContact contact = ContactsManager.getInstance().findContactFromAddress(address);
        if (contact != null) {
            ContactAvatar.displayAvatar(contact, findViewById(R.id.avatar_layout), true);
            mName.setText(contact.getFullName());
            name.setText(contact.getFullName());
        } else {
            String displayName = LinphoneUtils.getAddressDisplayName(address);
            ContactAvatar.displayAvatar(displayName, findViewById(R.id.avatar_layout), true);
            mName.setText(displayName);
            name.setText(displayName);
        }
        mNumber.setText(LinphoneUtils.getDisplayableAddress(address));

        /*new Handler()
        .postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        // Start Timer
                        long maxTimeInMilliseconds = 30000; // in your case
                        startTimer(maxTimeInMilliseconds, 1000);
                    }
                },
                1000);*/

        boolean recordAudioPermissionGranted = checkPermission(Manifest.permission.RECORD_AUDIO);
        if (!recordAudioPermissionGranted) {
            Log.w("[Call Outgoing Activity] RECORD_AUDIO permission denied, muting microphone");
            core.enableMic(false);
            mMicro.setSelected(true);
        }
    }

    @Override
    protected void onPause() {
        Core core = LinphoneManager.getCore();
        if (core != null) {
            core.removeListener(mListener);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mName = null;
        mNumber = null;
        mMicro = null;
        mSpeaker = null;
        mCall = null;
        mListener = null;

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.micro) {
            mIsMicMuted = !mIsMicMuted;
            mMicro.setSelected(mIsMicMuted);
            LinphoneManager.getCore().enableMic(!mIsMicMuted);
        }
        if (id == R.id.speaker) {
            mIsSpeakerEnabled = !mIsSpeakerEnabled;
            mSpeaker.setSelected(mIsSpeakerEnabled);
            if (mIsSpeakerEnabled) {
                LinphoneManager.getAudioManager().routeAudioToSpeaker();
            } else {
                LinphoneManager.getAudioManager().routeAudioToEarPiece();
            }
        }
        if (id == R.id.outgoing_hang_up) {
            decline();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (LinphoneContext.isReady()
                && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)) {
            mCall.terminate();

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void decline() {
        mCall.terminate();
        finish();
    }

    private void checkAndRequestCallPermissions() {
        ArrayList<String> permissionsList = new ArrayList<>();

        int recordAudio =
                getPackageManager()
                        .checkPermission(Manifest.permission.RECORD_AUDIO, getPackageName());
        Log.i(
                "[Permission] Record audio permission is "
                        + (recordAudio == PackageManager.PERMISSION_GRANTED
                                ? "granted"
                                : "denied"));
        int camera =
                getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName());
        Log.i(
                "[Permission] Camera permission is "
                        + (camera == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        int readPhoneState =
                getPackageManager()
                        .checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName());
        Log.i(
                "[Permission] Read phone state permission is "
                        + (camera == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));

        if (recordAudio != PackageManager.PERMISSION_GRANTED) {
            Log.i("[Permission] Asking for record audio");
            permissionsList.add(Manifest.permission.RECORD_AUDIO);
        }
        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            Log.i("[Permission] Asking for read phone state");
            permissionsList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (LinphonePreferences.instance().shouldInitiateVideoCall()) {
            if (camera != PackageManager.PERMISSION_GRANTED) {
                Log.i("[Permission] Asking for camera");
                permissionsList.add(Manifest.permission.CAMERA);
            }
        }

        if (permissionsList.size() > 0) {
            String[] permissions = new String[permissionsList.size()];
            permissions = permissionsList.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, 0);
        }
    }

    private boolean checkPermission(String permission) {
        int granted = getPackageManager().checkPermission(permission, getPackageName());
        Log.i(
                "[Permission] "
                        + permission
                        + " permission is "
                        + (granted == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            Log.i(
                    "[Permission] "
                            + permissions[i]
                            + " is "
                            + (grantResults[i] == PackageManager.PERMISSION_GRANTED
                                    ? "granted"
                                    : "denied"));
            if (permissions[i].equals(Manifest.permission.CAMERA)
                    && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                LinphoneUtils.reloadVideoDevices();
            } else if (permissions[i].equals(Manifest.permission.RECORD_AUDIO)
                    && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Core core = LinphoneManager.getCore();
                if (core != null) {
                    core.enableMic(true);
                    mMicro.setSelected(!core.micEnabled());
                }
            }
        }
    }

    /* public class CountDown extends CountDownTimer {

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long ms = millisUntilFinished;
            String text =
                    String.format(
                            "%02d\' %02d\"",
                            TimeUnit.MILLISECONDS.toMinutes(ms)
                                    - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)),
                            TimeUnit.MILLISECONDS.toSeconds(ms)
                                    - TimeUnit.MINUTES.toSeconds(
                                            TimeUnit.MILLISECONDS.toMinutes(ms)));
            timer.setText(text);
        }

        @Override
        public void onFinish() {
            timer.setText("00:00:00");
        }
    }

    public void startTimer(final long finish, long tick) {
        CountDownTimer t;
        t =
                new CountDownTimer(finish, tick) {

                    public void onTick(long millisUntilFinished) {
                        long remainedSecs = millisUntilFinished / 1000;
                        timer.setText(
                                ""
                                        + (remainedSecs / 60)
                                        + ":"
                                        + (remainedSecs % 60)); // manage it accordign to you
                    }

                    public void onFinish() {
                        timer.setText("00:00:00");
                        //                        Toast.makeText(CallOutgoingActivity.this,
                        // "Finish", Toast.LENGTH_SHORT)
                        //                                .show();
                        cancel();
                    }
                }.start();
    }*/

}
