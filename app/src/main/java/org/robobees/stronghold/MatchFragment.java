package org.robobees.stronghold;


import android.app.Fragment;

public abstract class MatchFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    protected OnFragmentInteractionListener mListener;
    protected int sectionNumber;
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void saveData(MatchStatsSH matchData, String source);
    }
}