package com.example.informatik.exposurecalculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Informatik on 09.03.2017.
 */

public class FilterReaderDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FilterReader.db";
    private static final int DATABASE_VERSION = 1;

    // Standard constructor for the helper
    public FilterReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }//-constructor

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FilterReaderContract.SQL_CREATE_FILTERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FilterReaderContract.SQL_DELETE_FILTERS);
        onCreate(db);
    }

    // For downgrading the database to the old version
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
