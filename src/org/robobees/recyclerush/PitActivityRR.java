package org.robobees.recyclerush;

import java.util.ArrayList;
import java.util.List;

import org.frc836.database.DB;
import org.frc836.database.DBSyncService;
import org.frc836.database.PitStats;
import org.frc836.database.DBSyncService.LocalBinder;
import org.growingstems.scouting.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jon on 1/27/2015.
 */
public class PitActivityRR extends Activity {

	private String HELPMESSAGE;

	private DB submitter;
	private PitStatsRR stats;

	private EditText teamT;
	private Spinner configS;
	private Spinner drivetrainS;
	private Spinner wheeltypeS;

	private EditText commentsT;
	private Button submitB;
	private TextView teamInfoT;
	private EditText heightT;

	private LocalBinder binder;
	//private ServiceWatcher watcher = new ServiceWatcher();

	private Handler timer = new Handler();
	private static final int DELAY = 500;

	private static final int CANCEL_DIALOG = 0;

	//private List<TeamNumTask> tasks = new ArrayList<TeamNumTask>(3);


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pits);

		Intent sync = new Intent(this, DBSyncService.class);
		//bindService(sync, watcher, Context.BIND_AUTO_CREATE);

		HELPMESSAGE = "Enter the requested information about each team. \n\n"
				+ "When a team number is entered, the last tiem that data was" +
				"collected about this team will be shown.\n"
				+ "If the date shown is during the current event, data does " +
				"not need to be collected.";

		submitter = new DB(getBaseContext(), binder);
		stats = new PitStatsRR();
		teamT = (EditText) findViewById(R.id.pits_teamT);
		configS = (Spinner) findViewById(R.id.pits_configS);
		drivetrainS = (Spinner) findViewById(R.id.pits_drivetrainS);
		wheeltypeS = (Spinner) findViewById(R.id.pits_wheeltypeS);

		commentsT = (EditText) findViewById(R.id.pits_commentsT);
		submitB = (Button) findViewById(R.id.pits_submitB);
		teamInfoT = (TextView) findViewById(R.id.pits_teamInfo);

		//teamT.addTextChangedListener(new teamTextListener());
		//submitB.setOnClickListener(new SubmitListener());
	}

	public void onResume() {
		//super.onRusume();

		List<String> config = submitter.getConfigList();
	}

}
