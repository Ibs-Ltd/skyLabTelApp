package tel.skylab.skylabtel.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.adapter.AdapterChatMessage;
import tel.skylab.skylabtel.adapter.AdapterRecentChats;
import tel.skylab.skylabtel.apis.request.GetMsgRequest;
import tel.skylab.skylabtel.apis.response.GetMsgResponseModel;
import tel.skylab.skylabtel.apis.service.GetMsgService;
import tel.skylab.skylabtel.apis.web.ApiCallback;
import tel.skylab.skylabtel.apis.web.ApiException;
import tel.skylab.skylabtel.models.MessageModel;
import tel.skylab.skylabtel.roomDatabase.SkylabRoomDatabase;
import tel.skylab.skylabtel.roomDatabase.entity.MessagesEntity;
import tel.skylab.skylabtel.ui.activities.DashboardActivity;
import tel.skylab.skylabtel.ui.activities.SkylabSendMessageActivity;
import tel.skylab.skylabtel.utils.Constants;

/** A simple {@link Fragment} subclass. */
public class ChatListFragment extends Fragment {

    RecyclerView recycler_recent_chat;
    View view;
    TextView noData;
    AdapterRecentChats adapterRecentChats;
    AdapterChatMessage adapterChatMessage;
    ArrayList<MessageModel> mSearchList = new ArrayList<>();
    ImageView iv_send_btn_msg;
    ProgressDialog pb;
    EditText et_Search_chat;
    LinearLayout search_layout;
    List<MessageModel> articlesData1;
    int i = 0;
    private SwipeRefreshLayout swipRefresh;
    SkylabRoomDatabase db;

    public DashboardActivity dashboard;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        swipRefresh = view.findViewById(R.id.swipRefresh);
        recycler_recent_chat = view.findViewById(R.id.recycler_recent_chat);
        iv_send_btn_msg = view.findViewById(R.id.iv_send_btn_msg);
        noData = view.findViewById(R.id.noData);
        search_layout = view.findViewById(R.id.search_layout);
        et_Search_chat = view.findViewById(R.id.et_Search_chat);

        SharedPreferences preferences =
                getActivity().getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
        String SmsStatusAllowed = preferences.getString(Constants.PREF_KEY_SMS_ALLOWED, "");

        db = SkylabRoomDatabase.getAppDatabase(getActivity());

        // Creating an AsyncTask object to retrieve
        hitGetMsgApi hit_get_msg_api = new hitGetMsgApi();
        // Starting the AsyncTask process to retrieve
        hit_get_msg_api.execute();

        /*adapterRecentChats =
                new AdapterRecentChats(
                        getActivity(),
                        recentChatsList,
                        new AdapterRecentChats.CallbackInterface() {
                            @Override
                            public void callBack() {
                                dashboard.setFragment(5, true);
                            }
                        });
        recycler_recent_chat.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_recent_chat.setAdapter(adapterRecentChats);
        adapterRecentChats.notifyDataSetChanged();*/

        et_Search_chat.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mSearchList.clear();
                        if (s.equals("")) {
                            mSearchList.addAll(articlesData1);
                        } else {
                            for (int i = 0; i < articlesData1.size(); i++) {
                                if (articlesData1.get(i).getFrom() != null
                                        && articlesData1
                                                .get(i)
                                                .getFrom()
                                                .toLowerCase()
                                                .contains(s.toString().toLowerCase())) {
                                    mSearchList.add(articlesData1.get(i));
                                }
                            }
                        }
                        //                Adapter.updateAdapter(mSearchList);
                        adapterChatMessage.notifyDataSetChanged();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

        iv_send_btn_msg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (SmsStatusAllowed.equalsIgnoreCase("0")
                                || SmsStatusAllowed.equalsIgnoreCase("")) {
                            showDailog();
                        } else {
                            startActivity(
                                    new Intent(getActivity(), SkylabSendMessageActivity.class)
                                            .putExtra("caller_number", ""));
                        }
                    }
                });

        swipRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        i = 1;
                        // Creating an AsyncTask object to retrieve
                        hitGetMsgApi hit_get_msg_api = new hitGetMsgApi();
                        // Starting the AsyncTask process to retrieve
                        hit_get_msg_api.execute();
                    }
                });

        return view;
    }

    private class hitGetMsgApi extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (i == 1) {
                swipRefresh.setRefreshing(true);
            } else {
                pb = new ProgressDialog(getActivity());
                pb.setTitle("Receiving Data");
                pb.setMessage("Please wait.....");
                pb.setCancelable(false);
                pb.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences preferences =
                    getActivity()
                            .getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
            String PBXNumber = preferences.getString(Constants.PREF_KEY_PBX_NUMBER, "");
            GetMsgRequest request = new GetMsgRequest();
            request.setTo(PBXNumber);

            new GetMsgService(getActivity())
                    .execute(
                            request,
                            new ApiCallback<GetMsgResponseModel>() {
                                @Override
                                public void onSuccess(
                                        Call<GetMsgResponseModel> call,
                                        GetMsgResponseModel response) {
                                    swipRefresh.setRefreshing(false);
                                    if (response.getCodes() == 0) {
                                        if (response.getResponseData().size() > 0) {
                                            for (int i = 0;
                                                    i < response.getResponseData().size();
                                                    i++) {
                                                MessagesEntity messagesEntity =
                                                        new MessagesEntity(
                                                                response.getResponseData()
                                                                        .get(i)
                                                                        .getFrom(),
                                                                response.getResponseData()
                                                                        .get(i)
                                                                        .getTo(),
                                                                response.getResponseData()
                                                                        .get(i)
                                                                        .getMessage(),
                                                                response.getResponseData()
                                                                        .get(i)
                                                                        .getMessaging_profile_id(),
                                                                response.getResponseData()
                                                                        .get(i)
                                                                        .getCreatedAt());
                                                db.messageDao().insert(messagesEntity);
                                            }
                                            articlesData1 = db.messageDao().getAll();
                                            mSearchList = new ArrayList<MessageModel>();
                                            mSearchList.addAll(articlesData1);
                                            noData.setVisibility(View.GONE);
                                            recycler_recent_chat.setVisibility(View.VISIBLE);
                                            search_layout.setVisibility(View.VISIBLE);
                                            recycler_recent_chat.setLayoutManager(
                                                    new LinearLayoutManager(getActivity()));
                                            adapterChatMessage =
                                                    new AdapterChatMessage(
                                                            getActivity(), mSearchList);
                                            recycler_recent_chat.setAdapter(adapterChatMessage);
                                            adapterRecentChats.notifyDataSetChanged();
                                        } else {
                                            articlesData1 = db.messageDao().getAll();
                                            if (articlesData1.size() > 0) {
                                                mSearchList = new ArrayList<MessageModel>();
                                                mSearchList.addAll(articlesData1);
                                                noData.setVisibility(View.GONE);
                                                search_layout.setVisibility(View.VISIBLE);
                                                recycler_recent_chat.setVisibility(View.VISIBLE);
                                                recycler_recent_chat.setLayoutManager(
                                                        new LinearLayoutManager(getActivity()));
                                                adapterChatMessage =
                                                        new AdapterChatMessage(
                                                                getActivity(), mSearchList);
                                                recycler_recent_chat.setAdapter(adapterChatMessage);
                                                //
                                                // adapterRecentChats.notifyDataSetChanged();
                                            } else {
                                                search_layout.setVisibility(View.GONE);
                                                noData.setVisibility(View.VISIBLE);
                                                recycler_recent_chat.setVisibility(View.GONE);
                                            }
                                        }

                                    } else {
                                        Toast.makeText(
                                                        getActivity(),
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
                                                    getActivity(),
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
            if (i == 1) {
                swipRefresh.setRefreshing(false);
            } else {
                pb.dismiss();
            }
        }
    }

    private void showDailog() {
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
