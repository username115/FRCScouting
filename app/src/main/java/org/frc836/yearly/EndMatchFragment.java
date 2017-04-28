/*
 * Copyright 2016 Daniel Logan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.frc836.yearly;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.MatchFragment;
import org.growingstems.scouting.R;

import java.util.ArrayList;
import java.util.List;


public class EndMatchFragment extends MatchFragment {

    private boolean displayed = false;

    private Spinner commonNotes;

    private Spinner pastNotes;

    private EditText teamNotes;

    public EndMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreMatch.
     */
    public static EndMatchFragment newInstance() {
        EndMatchFragment fragment = new EndMatchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        displayed = true;
        commonNotes = (Spinner) view.findViewById(R.id.commonNotes);
        teamNotes = (EditText)getView().findViewById(R.id.notes);
        pastNotes = (Spinner)getView().findViewById(R.id.previousNotes);
        commonNotes.setOnItemSelectedListener(new NotesSelectedListener());
        pastNotes.setOnItemSelectedListener(new NotesSelectedListener());
        ((EditText)getView().findViewById(R.id.alignTimeT)).addTextChangedListener(
                new TimeTextChangedListener((CheckBox)getView().findViewById(R.id.alignTimeUnknownC)));
        ((EditText)getView().findViewById(R.id.climbTimeT)).addTextChangedListener(
                new TimeTextChangedListener((CheckBox)getView().findViewById(R.id.climbTimeUnknownC)));
    }

    public void onResume() {
        super.onResume();

        Activity a = getActivity();

        if (a instanceof MatchActivity) {
            MatchActivity match = (MatchActivity) a;
            List<String> options = match.getNotesOptions();
            List<String> teamOptions = match.getTeamNotes();

            if (options == null)
                options = new ArrayList<String>(1);

            if (teamOptions == null)
                teamOptions = new ArrayList<String>(1);

            options.add(0, commonNotes.getItemAtPosition(0).toString());
            teamOptions.add(0, pastNotes.getItemAtPosition(0).toString());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(match,
                    android.R.layout.simple_spinner_item, options);

            ArrayAdapter<String> adapterTeam = new ArrayAdapter<String>(match,
                    android.R.layout.simple_spinner_item, teamOptions);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            commonNotes.setAdapter(adapter);
            pastNotes.setAdapter(adapterTeam);
        }
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void saveData(MatchStatsStruct data) {
        if (getView() == null || data == null || !displayed)
            return;
        data.climb_attempt = ((CheckBox)getView().findViewById(R.id.climb_end_attempt)).isChecked();
        data.climb_rope = ((CheckBox)getView().findViewById(R.id.climb_end)).isChecked();
        data.align_time = (((CheckBox)getView().findViewById(R.id.alignTimeUnknownC)).isChecked() ||
                ((EditText)getView().findViewById(R.id.alignTimeT)).getText().toString().length() < 1) ? -1 :
                Integer.valueOf(((EditText)getView().findViewById(R.id.alignTimeT)).getText().toString());
        data.climb_time = (((CheckBox)getView().findViewById(R.id.climbTimeUnknownC)).isChecked() ||
                ((EditText)getView().findViewById(R.id.climbTimeT)).getText().toString().length() < 1) ? -1 :
                Integer.valueOf(((EditText)getView().findViewById(R.id.climbTimeT)).getText().toString());
        data.notes = ((EditText)getView().findViewById(R.id.notes)).getText().toString();
        data.tip_over = ((CheckBox)getView().findViewById(R.id.botTip)).isChecked();
        data.foul = ((CheckBox)getView().findViewById(R.id.foul)).isChecked();
        data.yellow_card = ((CheckBox)getView().findViewById(R.id.yellow_card)).isChecked();
        data.red_card = ((CheckBox)getView().findViewById(R.id.red_card)).isChecked();
    }

    @Override
    public void loadData(MatchStatsStruct data) {
        if (getView() == null || data == null || !displayed)
            return;
        ((CheckBox)getView().findViewById(R.id.climb_end_attempt)).setChecked(data.climb_attempt);
        ((CheckBox)getView().findViewById(R.id.climb_end)).setChecked(data.climb_rope);
        ((CheckBox)getView().findViewById(R.id.alignTimeUnknownC)).setChecked(data.align_time <= 0);
        ((CheckBox)getView().findViewById(R.id.climbTimeUnknownC)).setChecked(data.climb_time <= 0);
        ((EditText)getView().findViewById(R.id.alignTimeT)).setText(data.align_time <= 0 ? "" : String.valueOf(data.align_time));
        ((EditText)getView().findViewById(R.id.climbTimeT)).setText(data.climb_time <= 0 ? "" : String.valueOf(data.climb_time));
        ((EditText)getView().findViewById(R.id.notes)).setText(data.notes);
        ((CheckBox)getView().findViewById(R.id.botTip)).setChecked(data.tip_over);
        ((CheckBox)getView().findViewById(R.id.foul)).setChecked(data.foul);
        ((CheckBox)getView().findViewById(R.id.red_card)).setChecked(data.red_card);
        ((CheckBox)getView().findViewById(R.id.yellow_card)).setChecked(data.yellow_card);
    }

    public class NotesSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View v, int position,
                                   long id) {
            if (position == 0 || !(parent instanceof Spinner))
                return;
            Spinner par = (Spinner) parent;
            String note = teamNotes.getText().toString();
            if (!note.contains(par.getItemAtPosition(position).toString())) {
                if (!note.trim().equals(""))
                    note = note + "; ";
                note = note + par.getItemAtPosition(position);
                teamNotes.setText(note);
            }
            par.setSelection(0);

        }

        public void onNothingSelected(AdapterView<?> parent) {
        }

    }

    class TimeTextChangedListener implements TextWatcher {

        private CheckBox m_unknown;

        public TimeTextChangedListener(CheckBox unknownC) {
            m_unknown = unknownC;
        }

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * are about to be replaced by new text with length <code>after</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param s
         * @param start
         * @param count
         * @param after
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Auto-generated method stub
        }

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * have just replaced old text that had length <code>before</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Auto-generated method stub
        }

        /**
         * This method is called to notify you that, somewhere within
         * <code>s</code>, the text has been changed.
         * It is legitimate to make further changes to <code>s</code> from
         * this callback, but be careful not to get yourself into an infinite
         * loop, because any changes you make will cause this method to be
         * called again recursively.
         * (You are not told where the change took place because other
         * afterTextChanged() methods may already have made other changes
         * and invalidated the offsets.  But if you need to know here,
         * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
         * to mark your place and then look up from here where the span
         * ended up.
         *
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0)
                m_unknown.setChecked(false);
            else
                m_unknown.setChecked(true);
        }
    }

}
