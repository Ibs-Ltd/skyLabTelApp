package tel.skylab.skylabtel.adapter;

import static android.content.Context.MODE_PRIVATE;
import static tel.skylab.skylabtel.utils.Constants.picturePtahSkyLab;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.linphone.LinphoneManager;
import tel.skylab.skylabtel.models.ContactModel;
import tel.skylab.skylabtel.ui.activities.SkylabSendMessageActivity;
import tel.skylab.skylabtel.utils.Constants;

public class AdapterContactList extends RecyclerView.Adapter<AdapterContactList.ViewHolder> {
    private Context mContext;
    private ArrayList<ContactModel> mData = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    ViewModelStoreOwner viewModelStoreOwner;

    public AdapterContactList(Context mContext, ArrayList<ContactModel> mData) {
        this.mContext = mContext;
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ContactModel Info = mData.get(holder.getAdapterPosition());

        holder.tv_name.setText(Info.getName());

        holder.tv_details.setText(Info.getNumber());
        Bitmap bmImg = BitmapFactory.decodeFile(Info.getImage());
        if (bmImg == null) {
            holder.iv_photo.setImageResource(R.drawable.businessman);
        } else {
            holder.iv_photo.setImageBitmap(bmImg);
        }

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        picturePtahSkyLab = BitmapFactory.decodeFile(Info.getImage());
                        showDailog(Info.getName(), Info.getNumber());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_details, user_invite;
        private ImageView iv_photo, user_img;
        private RelativeLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_details = itemView.findViewById(R.id.tv_details);
            iv_photo = itemView.findViewById(R.id.iv_photo);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public void updateAdapter(ArrayList<ContactModel> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    private void showDailog(String name, String number) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.profile_dialog);
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView caller_name = dialog.findViewById(R.id.caller_name);
        TextView caller_number = dialog.findViewById(R.id.caller_number);
        LinearLayout call_button = dialog.findViewById(R.id.call_button);
        LinearLayout message_button = dialog.findViewById(R.id.message_button);
        caller_name.setText(name);
        caller_number.setText(number);
        call_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LinphoneManager.getCallManager().newOutgoingCall(number, name);
                    }
                });

        message_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences preferences =
                                mContext.getSharedPreferences(
                                        Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
                        String SmsStatusAllowed =
                                preferences.getString(Constants.PREF_KEY_SMS_ALLOWED, "");
                        if (SmsStatusAllowed.equalsIgnoreCase("0")
                                || SmsStatusAllowed.equalsIgnoreCase("")) {
                            dialog.dismiss();
                            showDailog1();
                        } else {
                            dialog.dismiss();
                            mContext.startActivity(
                                    new Intent(mContext, SkylabSendMessageActivity.class)
                                            .putExtra("caller_number", number));
                        }
                    }
                });
        dialog.show();
    }

    private void showDailog1() {
        final Dialog dialog = new Dialog(mContext);
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
