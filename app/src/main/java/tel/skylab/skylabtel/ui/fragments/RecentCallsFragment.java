package tel.skylab.skylabtel.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.adapter.AdapterRecentCalls;
import tel.skylab.skylabtel.roomDatabase.SkylabRoomDatabase;
import tel.skylab.skylabtel.roomDatabase.models.RecentCallModel;
import tel.skylab.skylabtel.ui.activities.DashboardActivity;

/** A simple {@link Fragment} subclass. */
public class RecentCallsFragment extends Fragment {
    TextView no_call_history;
    public DashboardActivity dashboard;
    RecyclerView mHistoryList;
    SkylabRoomDatabase db;
    View view;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent_calls, container, false);

        mHistoryList = view.findViewById(R.id.mHistoryList);
        no_call_history = view.findViewById(R.id.no_call_history);

        db = SkylabRoomDatabase.getAppDatabase(getActivity());
        List<RecentCallModel> articlesData1 = db.recentCallsDao().getAll();

        if (articlesData1.size() > 0) {
            mHistoryList.setVisibility(View.VISIBLE);
            no_call_history.setVisibility(View.GONE);
            mHistoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
            final AdapterRecentCalls adapter = new AdapterRecentCalls(getActivity(), articlesData1);
            mHistoryList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            mHistoryList.setVisibility(View.GONE);
            no_call_history.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        db = SkylabRoomDatabase.getAppDatabase(getActivity());
        List<RecentCallModel> articlesData1 = db.recentCallsDao().getAll();

        if (articlesData1.size() > 0) {
            mHistoryList.setVisibility(View.VISIBLE);
            no_call_history.setVisibility(View.GONE);
            mHistoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
            final AdapterRecentCalls adapter = new AdapterRecentCalls(getActivity(), articlesData1);
            mHistoryList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            mHistoryList.setVisibility(View.GONE);
            no_call_history.setVisibility(View.VISIBLE);
        }
    }
}
