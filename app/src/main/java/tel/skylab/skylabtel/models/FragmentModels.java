package tel.skylab.skylabtel.models;

import androidx.fragment.app.Fragment;

public class FragmentModels {

    private int FragmentNumber;
    private Fragment fragment;

    public FragmentModels(int fragmentNumber, Fragment fragment) {
        FragmentNumber = fragmentNumber;
        this.fragment = fragment;
    }

    public int getFragmentNumber() {
        return FragmentNumber;
    }

    public void setFragmentNumber(int fragmentNumber) {
        FragmentNumber = fragmentNumber;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
