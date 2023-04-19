package org.frc836.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoutingDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 20232;
    public static final String DATABASE_NAME = "FRCscouting.db";

    private static ScoutingDBHelper helper;
    public static final Object lock = new Object();

    public ScoutingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < FRCScoutingContract.SQL_CREATE_ENTRIES.length; i++)
            db.execSQL(FRCScoutingContract.SQL_CREATE_ENTRIES[i]);
        DBActivity.dbUpdated();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion == 20231 && newVersion == 20232) {
            final String query =
                "INSERT INTO event_lu\n" +
                    "SELECT 168 AS id, 'FIRST Championship - Archimedes Division' AS event_name, 'ARPKY' AS event_code, '2023-04-19 00:00:00' AS date_start, '2023-04-19 20:16:23' AS timestamp, 0 AS invalid\n" +
                    "UNION SELECT 169, 'FIRST Championship - Curie Division', 'CPRA', '2023-04-19 00:00:00', '2023-04-19 20:19:31', 0\n" +
                    "UNION SELECT 170, 'FIRST Championship - Daly Division', 'DCMP', '2023-04-19 00:00:00', '2023-04-19 20:19:31', 0\n" +
                    "UNION SELECT 171, 'FIRST Championship - Galileo Division', 'GCMP', '2023-04-19 00:00:00', '2023-04-19 20:19:31', 0\n" +
                    "UNION SELECT 172, 'FIRST Championship - Hopper Division', 'HCMP', '2023-04-19 00:00:00', '2023-04-19 20:19:31', 0\n" +
                    "UNION SELECT 173, 'FIRST Championship - Johnson Division', 'JCMP', '2023-04-19 00:00:00', '2023-04-19 20:19:31', 0\n" +
                    "UNION SELECT 174, 'FIRST in Michigan State Championship presented by DTE Foundation - DTE ENERGY FOUNDATION Division', 'MICMP1', '2023-04-06 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 175, 'FIRST in Michigan State Championship presented by DTE Foundation - FORD Division', 'MICMP2', '2023-04-06 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 176, 'FIRST in Michigan State Championship presented by DTE Foundation - APTIV Division', 'MICMP3', '2023-04-06 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 177, 'FIRST in Michigan State Championship presented by DTE Foundation - CONSUMERS ENERGY Division', 'MICMP4', '2023-04-06 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 178, 'FIRST Championship - Milstein Division', 'MPCIA', '2023-04-19 00:00:00', '2023-04-19 20:18:04', 0\n" +
                    "UNION SELECT 179, 'New England FIRST District Championship - MEIR Division', 'NECMP1', '2023-04-05 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 180, 'New England FIRST District Championship - WILSON Division', 'NECMP2', '2023-04-05 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 181, 'FIRST Championship - Newton Division', 'NPFCMP', '2023-04-19 00:00:00', '2023-04-19 20:18:04', 0\n" +
                    "UNION SELECT 182, 'FIRST Ontario Provincial Championship - TECHNOLOGY Division', 'ONCMP1', '2023-04-05 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 183, 'FIRST Ontario Provincial Championship - SCIENCE Division', 'ONCMP2', '2023-04-05 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 184, 'Halic Regional', 'TUHC', '2023-03-31 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 185, 'FIRST In Texas District Championship presented by Phillips 66 - APOLLO Division', 'TXCMP1', '2023-04-05 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 186, 'FIRST In Texas District Championship presented by Phillips 66 - MERCURY Division', 'TXCMP2', '2023-04-05 00:00:00', '2023-04-19 20:03:12', 0\n" +
                    "UNION SELECT 187, 'Week 0', 'WEEK0', '2023-02-18 00:00:00', '2023-04-19 20:03:12', 0;";

            db.execSQL(query);
            DBActivity.dbUpdated();

        }
        else {
            for (int i = 0; i < FRCScoutingContract.SQL_DELETE_ENTRIES.length; i++)
                db.execSQL(FRCScoutingContract.SQL_DELETE_ENTRIES[i]);
            onCreate(db);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 20232 && newVersion == 20231) {
            //do nothing
        }
        else {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public static ScoutingDBHelper getInstance(Context context) {
        if (helper == null) {
            helper = new ScoutingDBHelper(context);
        }
        return helper;
    }

    public static ScoutingDBHelper getInstance() {
        return helper;
    }

    public interface DBInstantiatedCallback {
        void dbInstantiated();
    }

}
