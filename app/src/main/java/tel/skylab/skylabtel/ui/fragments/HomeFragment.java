package tel.skylab.skylabtel.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import tel.skylab.skylabtel.R;
import tel.skylab.skylabtel.ui.activities.DashboardActivity;

/** A simple {@link Fragment} subclass. */
public class HomeFragment extends Fragment {

    private View view;
    public DashboardActivity dashboard;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }
}
