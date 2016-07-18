package edu.gatech.seclass.gobowl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Bowling.db";

    public final class DataContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public DataContract() {}

        /* Inner class that defines the table contents */
        public abstract class CustomerEntry implements BaseColumns {
            public static final String TABLE_NAME = "CUSTOMER";
            public static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
            public static final String KEY_FIRSTNAME = "FIRSTNAME";
            public static final String KEY_LASTNAME = "LASTNAME";
            public static final String KEY_EMAIL = "EMAIL";
            public static final String KEY_IS_VIP = "IS_VIP";
        }
        
        public abstract class BowlingLaneEventEntry implements BaseColumns {
            public static final String TABLE_NAME = "RunningBowlingLanes";
            // Shops Table Columns names
            public static final String KEY_LaneID = "LaneNumber";
            public static final String KEY_StartTime = "StartTime";
            public static final String KEY_EndTime = "EndTime";
            //A Customer ID String
            public static final String KEY_PrimaryCustomerID = "PrimaryCustomerID";
            //A long Customer IDs seperated with single spaces
            public static final String KEY_NonPrimaryCustomerIDs = "NonPrimaryCustomerIDs";
            
        }

        public abstract class ScoreEntry implements BaseColumns {
            public static final String TABLE_NAME = "ScoreHistory";
            // Shops Table Columns names
            public static final String KEY_ScoreDate = "Score_Date";
            public static final String KEY_CustID = "CustID";
            public static final String KEY_Score = "Score";

        }

        public abstract class BillingEntry implements BaseColumns {
            public static final String TABLE_NAME = "Billing";
            // Shops Table Columns names
            public static final String KEY_Billing_ID = "Billing_ID";
            public static final String KEY_CustID = "CustID";
            public static final String KEY_Date = "Date";
            public static final String KEY_SpendAmount = "SpendAmount";

        }

        private static final String TEXT_TYPE = " TEXT";
        private static final String TINY_INT_TYPE = " tinyint(1)";
        private static final String INT_TYPE = " Integer";
        private static final String DOUBLE_TYPE = " Double";
        private static final String COMMA_SEP = ",";
        
        private static final String CUSTOMER_SQL_CREATE_ENTRY =
                "CREATE TABLE " + CustomerEntry.TABLE_NAME + " (" +
                        CustomerEntry.KEY_CUSTOMER_ID + TEXT_TYPE + "  PRIMARY KEY," +
                        CustomerEntry.KEY_FIRSTNAME +  TEXT_TYPE + COMMA_SEP +
                        CustomerEntry.KEY_LASTNAME +  TEXT_TYPE + COMMA_SEP +
                        CustomerEntry.KEY_IS_VIP +  TINY_INT_TYPE + COMMA_SEP +
                        CustomerEntry.KEY_EMAIL + TEXT_TYPE + " unique" + " )";

        private static final String BOWLING_LANE_EVENT_SQL_CREATE_ENTRY =
                "CREATE TABLE " + BowlingLaneEventEntry.TABLE_NAME  + "("
                + BowlingLaneEventEntry.KEY_LaneID + " INTEGER PRIMARY KEY" + COMMA_SEP
                + BowlingLaneEventEntry.KEY_StartTime + TEXT_TYPE + COMMA_SEP
                + BowlingLaneEventEntry.KEY_EndTime + TEXT_TYPE + COMMA_SEP
                + BowlingLaneEventEntry.KEY_PrimaryCustomerID + TEXT_TYPE + COMMA_SEP
                + BowlingLaneEventEntry.KEY_NonPrimaryCustomerIDs + TEXT_TYPE + ")";

        private static final String SCORE_CREATE_ENTRY =
                "CREATE TABLE " + ScoreEntry.TABLE_NAME + "(" +
                        ScoreEntry.KEY_CustID + TEXT_TYPE  + COMMA_SEP +
                        ScoreEntry.KEY_ScoreDate + TEXT_TYPE + COMMA_SEP +
                        ScoreEntry.KEY_Score + INT_TYPE + COMMA_SEP +
                        "PRIMARY KEY "+ "(" +ScoreEntry.KEY_CustID + ", "+ ScoreEntry.KEY_ScoreDate +")" +");";

        private static final String BILLING_CREATE_ENTRY =
                "CREATE TABLE " + BillingEntry.TABLE_NAME + "(" +
                        BillingEntry.KEY_Billing_ID + INT_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                        BillingEntry.KEY_CustID + TEXT_TYPE + COMMA_SEP +
                        BillingEntry.KEY_Date + TEXT_TYPE + COMMA_SEP +
                        BillingEntry.KEY_SpendAmount + DOUBLE_TYPE  + ");";



        private static final String CUSTOMER_SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + CustomerEntry.TABLE_NAME;
        private static final String BOWLING_LE_SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + BowlingLaneEventEntry.TABLE_NAME;
        private static final String SCORE_SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME;
        private static final String BILLING_SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + BillingEntry.TABLE_NAME;


    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataContract.CUSTOMER_SQL_CREATE_ENTRY);
        db.execSQL(DataContract.BOWLING_LANE_EVENT_SQL_CREATE_ENTRY);
        db.execSQL(DataContract.SCORE_CREATE_ENTRY);
        db.execSQL(DataContract.BILLING_CREATE_ENTRY);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DataContract.CUSTOMER_SQL_DELETE_ENTRIES);
        db.execSQL(DataContract.BOWLING_LE_SQL_DELETE_ENTRIES);
        db.execSQL(DataContract.SCORE_SQL_DELETE_ENTRIES);
        db.execSQL(DataContract.BILLING_SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
