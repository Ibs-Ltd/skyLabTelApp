package tel.skylab.skylabtel.ui.fragments;

import static android.content.Context.MODE_PRIVATE;
import static tel.skylab.skylabtel.utils.Constants.picturePtahSkyLab;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.linphone.LinphoneManager;
import tel.skylab.skylabtel.ui.activities.DashboardActivity;
import tel.skylab.skylabtel.ui.activities.SkylabSendMessageActivity;
import tel.skylab.skylabtel.utils.Constants;

/** A simple {@link Fragment} subclass. */
public class SkyDialerFragment extends Fragment {

    private View view;
    private EditText etNumber;
    private ImageView tvErase, iv_add_person, iv_call_btn, iv_message;
    private TextView tvOne,
            tvTwo,
            tvThree,
            tvFour,
            tvFive,
            tvSix,
            tvSeven,
            tvEight,
            tvNine,
            tvStar,
            tvZero,
            tvHash;
    public DashboardActivity dashboard;
    private boolean mIsTransfer;

    public SkyDialerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sky_dialer, container, false);

        etNumber = view.findViewById(R.id.etNumber);
        etNumber.setRawInputType(InputType.TYPE_NULL);
        etNumber.setFocusable(true);
        tvErase = view.findViewById(R.id.tvErase);
        iv_add_person = view.findViewById(R.id.iv_add_person);
        iv_call_btn = view.findViewById(R.id.iv_call_btn);
        iv_message = view.findViewById(R.id.iv_message);
        tvOne = view.findViewById(R.id.tvOne);
        tvTwo = view.findViewById(R.id.tvTwo);
        tvThree = view.findViewById(R.id.tvThree);
        tvFour = view.findViewById(R.id.tvFour);
        tvFive = view.findViewById(R.id.tvFive);
        tvSix = view.findViewById(R.id.tvSix);
        tvSeven = view.findViewById(R.id.tvSeven);
        tvEight = view.findViewById(R.id.tvEight);
        tvNine = view.findViewById(R.id.tvNine);
        tvStar = view.findViewById(R.id.tvStar);
        tvZero = view.findViewById(R.id.tvZero);
        tvHash = view.findViewById(R.id.tvHash);

        tvErase.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*int start = Math.min(etNumber.getSelectionStart(), etNumber.getText().length());
                        int end = Math.min(etNumber.getSelectionEnd(), etNumber.getText().length());
                        etNumber.getText().replace(Math.min(start, end), Math.max(start, end), "");*/
                        int end = etNumber.getText().length();
                        if (end > 0) {
                            etNumber.getText().replace(end - 1, end, "");
                        }
                    }
                });

        tvErase.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        etNumber.setText("");
                        return false;
                    }
                });

        iv_call_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                        showDailog(getActivity());
                        String mAddress = etNumber.getText().toString();
                        if (etNumber.getText().length() > 0) {
                            picturePtahSkyLab = null;
                            LinphoneManager.getCallManager().newTextOutgoingCall(mAddress);
                        } else {
                            Toast.makeText(dashboard, "Please enter a number", Toast.LENGTH_SHORT)
                                    .show();
                            /*if (LinphonePreferences.instance().isBisFeatureEnabled()) {
                                Core core = LinphoneManager.getCore();
                                CallLog[] logs = core.getCallLogs();
                                CallLog log = null;
                                for (CallLog l : logs) {
                                    if (l.getDir() == Call.Dir.Outgoing) {
                                        log = l;
                                        break;
                                    }
                                }
                                if (log == null) {
                                    return;
                                }

                                ProxyConfig lpc = core.getDefaultProxyConfig();
                                if (lpc != null
                                        && log.getToAddress().getDomain().equals(lpc.getDomain())) {
                                    etNumber.setText(log.getToAddress().getUsername());
                                } else {
                                    etNumber.setText(log.getToAddress().asStringUriOnly());
                                }
                                etNumber.setSelection(etNumber.getText().toString().length());
                                //
                                // mAddress.setDisplayedName(log.getToAddress().getDisplayName());
                            }*/
                        }
                    }
                });

        iv_add_person.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etNumber.getText().length() > 0) {
                            Intent contactIntent =
                                    new Intent(ContactsContract.Intents.Insert.ACTION);
                            contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                            contactIntent
                                    .putExtra(ContactsContract.Intents.Insert.NAME, "")
                                    .putExtra(
                                            ContactsContract.Intents.Insert.PHONE,
                                            etNumber.getText().toString().trim());
                            startActivityForResult(contactIntent, 1);
                        } else {
                            Toast.makeText(dashboard, "Please enter a number", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

        iv_message.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etNumber.getText().length() > 0) {
                            SharedPreferences preferences =
                                    getActivity()
                                            .getSharedPreferences(
                                                    Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
                            String SmsStatusAllowed =
                                    preferences.getString(Constants.PREF_KEY_SMS_ALLOWED, "");
                            if (SmsStatusAllowed.equalsIgnoreCase("0")
                                    || SmsStatusAllowed.equalsIgnoreCase("")) {
                                showDailog1();
                            } else {
                                getActivity()
                                        .startActivity(
                                                new Intent(
                                                                getActivity(),
                                                                SkylabSendMessageActivity.class)
                                                        .putExtra(
                                                                "caller_number",
                                                                etNumber.getText()
                                                                        .toString()
                                                                        .trim()));
                            }
                        } else {
                            Toast.makeText(dashboard, "Please enter a number", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

        tvOne.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("1");
                    }
                });

        tvTwo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("2");
                    }
                });

        tvThree.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("3");
                    }
                });

        tvFour.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("4");
                    }
                });

        tvFive.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("5");
                    }
                });

        tvSix.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("6");
                    }
                });

        tvSeven.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("7");
                    }
                });

        tvEight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("8");
                    }
                });

        tvNine.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("9");
                    }
                });

        tvStar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("*");
                    }
                });

        tvZero.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("0");
                    }
                });

        tvZero.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        insertTextToDialer("+");
                        return true;
                    }
                });

        tvHash.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        insertTextToDialer("#");
                    }
                });

        /* iv_call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        return view;
    }

    private void insertTextToDialer(String text) {
        int start = Math.max(etNumber.getSelectionStart(), 0);
        int end = Math.max(etNumber.getSelectionEnd(), 0);
        etNumber.getText().replace(Math.min(start, end), Math.max(start, end), text);
    }

    public void showDailog(Context context) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calling_screen);
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView close_btn = dialog.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void showDailog1() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.message_aleart_dialog);
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView cancel_btn = dialog.findViewById(R.id.cancel_btn);
        TextView message_body = dialog.findViewById(R.id.message_body);
        message_body.setText("Your subscription is only for calls not for messages.");
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
