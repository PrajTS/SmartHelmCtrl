package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import data.HelmContract.HelmetEntry;

public class HelmDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shelter.db";

    private static final int DATABASE_VERSION = 1;

    public HelmDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_HELM_TABLE = "CREATE TABLE " + HelmetEntry.TABLE_NAME + "("
                + HelmetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HelmetEntry.COLUMN_HELM_NAME + " TEXT NOT NULL, "
                + HelmetEntry.COLUMN_MAC_ADDRESS + " TEXT NOT NULL, "
                + HelmetEntry.COLUMN_DEFAULT + " BOOLEAN DEFAULT FALSE); ";

        db.execSQL(SQL_CREATE_HELM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onDefaultChange(SQLiteDatabase db,int default_val, String mac_addr)
    {
        String SQL_DEFAULT_HELM = "UPDATE " + HelmetEntry.TABLE_NAME
                + " SET " + HelmetEntry.COLUMN_DEFAULT  + " = " + default_val
            + " WHERE " + HelmetEntry.COLUMN_MAC_ADDRESS + " = " + mac_addr ;

        db.execSQL(SQL_DEFAULT_HELM);
    }

    public void onRemoveHelm(SQLiteDatabase db, String mac_addr)
    {
        String SQL_DELETE_HELM = "DELETE FROM " + HelmetEntry.TABLE_NAME
                + " WHERE " + HelmetEntry.COLUMN_MAC_ADDRESS + " = " + mac_addr ;

        db.execSQL(SQL_DELETE_HELM);
    }

}
