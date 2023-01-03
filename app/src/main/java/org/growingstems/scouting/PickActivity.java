package org.growingstems.scouting;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.android.TouchInterceptor;

import org.frc836.database.DBActivity;
import org.frc836.database.FRCScoutingContract;
import org.frc836.database.ScoutingDBHelper;

import java.util.Collections;
import java.util.List;

public class PickActivity extends DBActivity implements View.OnCreateContextMenuListener {

    TouchInterceptor mPickList;
    SimpleCursorAdapter mAdapter;
    private final ActivityResultLauncher<Intent> resultForPrefs = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);

        mPickList = findViewById(R.id.PickList);
        ensureList();

        mPickList.setOnCreateContextMenuListener(this);
        mPickList.setDropListener(mDropListener);
        mPickList.setRemoveListener(mRemoveListener);
        mPickList.setDivider(null);
        mPickList.setSelector(R.drawable.list_selector_background);

        String[] fromColumns = {FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_TEAM_ID, FRCScoutingContract.PICKLIST_Entry.COLUMN_NAME_PICKED};
        int[] toViews = {R.id.line1, R.id.line2};
        setListAdapter(new SimpleCursorAdapter(this,
            R.layout.pick_item, null,
            fromColumns, toViews, 0));
        mPickList.setOnItemLongClickListener(new PickLongClick());

    }

    public void onResume() {
        super.onResume();
        RefreshData();
    }

    public void onDestroy() {
        super.onDestroy();
        mPickList.setDropListener(null);
        mPickList.setRemoveListener(null);
    }

    private void RefreshData() {

        SQLiteDatabase data = ScoutingDBHelper.getInstance()
            .getReadableDatabase();
        Cursor teams = db.getPickListCursor(Prefs.getEvent(this, "CHS District Central Virginia Event"), data);
        mAdapter.swapCursor(teams);
        ScoutingDBHelper.getInstance().close();
    }


    private final TouchInterceptor.DropListener mDropListener =
        (from, to) -> {
            boolean fromFirst = from < to;
            String eventName = Prefs.getEvent(PickActivity.this, "CHS District Central Virginia Event");
            List<String> teams = db.getPickList(eventName);
            Collections.rotate(teams.subList(fromFirst ? from : to, (fromFirst ? to : from) + 1), to - from);
            db.updateSort(teams, eventName);
            RefreshData();
        };

    private final TouchInterceptor.RemoveListener mRemoveListener =
        which -> {
            String eventName = Prefs.getEvent(PickActivity.this, "CHS District Central Virginia Event");
            List<String> teams = db.getPickList(eventName);
            db.removeTeamFromPickList(Integer.parseInt(teams.get(which)), eventName);
            RefreshData();
        };

    public void setListAdapter(SimpleCursorAdapter adapter) {
        synchronized (this) {
            ensureList();
            mAdapter = adapter;
            mPickList.setAdapter(adapter);
        }
    }

    private void ensureList() {
        if (mPickList != null) {
            //TODO
        }
        //setContentView(com.android.internal.R.layout.list_content_simple); //do I need this?

    }

    public ListView getListView() {
        ensureList();
        return mPickList;
    }

    @NonNull
    @Override
    public String getHelpMessage() {
        return "";
    }

    @NonNull
    @Override
    public ActivityResultLauncher<Intent> getResultForPrefs() {
        return resultForPrefs;
    }

    private class PickLongClick implements AdapterView.OnItemLongClickListener {

        /**
         * Callback method to be invoked when an item in this view has been
         * clicked and held.
         * <p/>
         * Implementers can call getItemAtPosition(position) if they need to access
         * the data associated with the selected item.
         *
         * @param parent   The AbsListView where the click happened
         * @param view     The view within the AbsListView that was clicked
         * @param position The position of the view in the list
         * @param id       The row id of the item that was clicked
         * @return true if the callback consumed the long click, false otherwise
         */
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            PopupMenu popup = new PopupMenu(PickActivity.this, view);

            popup.getMenu().add(OTHERPICKED);
            popup.getMenu().add(REMOVETEXT);
            popup.setOnMenuItemClickListener(new PickMenuItemListener(position));
            popup.show();
            return true;
        }
    }

    private static final String OTHERPICKED = "Team Picked by other Alliance";
    private static final String REMOVETEXT = "Remove";

    private class PickMenuItemListener implements PopupMenu.OnMenuItemClickListener {

        int mPosition;

        public PickMenuItemListener(int position) {
            mPosition = position;
        }

        /**
         * This method will be invoked when a menu item is clicked if the item itself did
         * not already handle the event.
         *
         * @param item {@link MenuItem} that was clicked
         * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
         */
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            String eventName = Prefs.getEvent(PickActivity.this, "CHS District Central Virginia Event");
            List<String> teams = db.getPickList(eventName);
            if (item.getTitle().toString().equalsIgnoreCase(REMOVETEXT)) {
                db.removeTeamFromPickList(Integer.parseInt(teams.get(mPosition)), eventName);
                RefreshData();
            } else if (item.getTitle().toString().equalsIgnoreCase(OTHERPICKED)) {
                db.teamPickToggle(Integer.parseInt(teams.get(mPosition)), eventName);
                RefreshData();
            }
            return true;
        }
    }


}
