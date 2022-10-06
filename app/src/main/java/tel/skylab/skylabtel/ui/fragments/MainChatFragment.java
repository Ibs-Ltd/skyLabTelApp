package tel.skylab.skylabtel.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.adapter.AdapterChatMessage;
import tel.skylab.skylabtel.models.ChatMessageModel;
import tel.skylab.skylabtel.ui.activities.DashboardActivity;

/** A simple {@link Fragment} subclass. */
public class MainChatFragment extends Fragment {

    private View view;
    private String username;
    private AdapterChatMessage mAdapter;
    private ListView mChatView;
    private List<ChatMessageModel> chatList = new ArrayList<ChatMessageModel>();
    private EditText et_message;
    private ImageView btn_send;
    private boolean mine = true;
    public DashboardActivity dashboard;

    public MainChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_chat, container, false);

        username = "faizan";

        mChatView = (ListView) view.findViewById(R.id.chat_view);
        et_message = view.findViewById(R.id.et_message);
        btn_send = view.findViewById(R.id.btn_send);

        /*mAdapter = new AdapterChatMessage(getActivity(), chatList);
        mChatView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();*/

        btn_send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_message.setText("");
                        Toast.makeText(dashboard, "Working on it.", Toast.LENGTH_SHORT).show();
                        /*if (mine){
                        ChatMessageModel chat = new ChatMessageModel(et_message.getText().toString(),true,false);
                        chatList.add(chat);
                        mine=false;
                        }else {
                            ChatMessageModel chat = new ChatMessageModel(et_message.getText().toString(),false,false);
                            chatList.add(chat);
                            mine=true;
                        }
                        mAdapter = new AdapterChatMessage(getActivity(), chatList);
                        mChatView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();*/
                    }
                });

        return view;
    }
}
