package org.growingstems.scouting;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.TouchInterceptor;

import org.frc836.database.DBActivity;

import java.util.ArrayList;
import java.util.List;

public class PickActivity extends DBActivity implements View.OnCreateContextMenuListener {

    TouchInterceptor mPickList;
    ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);

        mPickList = (TouchInterceptor) findViewById(R.id.PickList);
        ensureList();

        mPickList.setOnCreateContextMenuListener(this);
        mPickList.setDropListener(mDropListener);
        mPickList.setRemoveListener(mRemoveListener);
        mPickList.setDivider(null);
        mPickList.setSelector(R.drawable.list_selector_background);

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
        List<String> teams = db.getPickList(Prefs.getEvent(this, "CHS District - Greater DC Event"));
        if (teams == null) {
            teams = new ArrayList<String>(1);
            teams.add("No teams in pick list");
        }

            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teams));

    }


    private TouchInterceptor.DropListener mDropListener =
            new TouchInterceptor.DropListener() {
                public void drop(int from, int to) {
                    //TODO
                }
            };

    private TouchInterceptor.RemoveListener mRemoveListener =
            new TouchInterceptor.RemoveListener() {
                public void remove(int which) {
                    //TODO
                }
            };

    public void setListAdapter(ListAdapter adapter) {
        synchronized (this) {
            ensureList();
            mAdapter = adapter;
            mPickList.setAdapter(adapter);
        }
    }

    private void ensureList() {
        if (mPickList != null) {
            return;
        }
        //setContentView(com.android.internal.R.layout.list_content_simple); //do I need this?

    }

    public ListView getListView() {
        ensureList();
        return mPickList;
    }

}
