package com.example.informatik.exposurecalculator;

import android.provider.BaseColumns;

/**
 * Created by Informatik on 09.03.2017.
 */

public final class FilterReaderContract {
    private FilterReaderContract(){}

    //Namen der Datenbankelemente
    public static class FilterEntry implements BaseColumns {
        public static final String TABLE_NAME  = "filter";
        public static final String CLMN_NAME    = "name";
        public static final String CLMN_STOPS = "stops";;
    }//-Contact Table

    // Query for creating table
    public static final String SQL_CREATE_FILTERS =
            "CREATE TABLE " + FilterEntry.TABLE_NAME + " (" +
                    FilterEntry._ID + " INTEGER PRIMARY KEY," +
                    FilterEntry.CLMN_NAME + " TEXT," +
                    FilterEntry.CLMN_STOPS + " INTEGER)";

    // Query for dropping table
    public static final String SQL_DELETE_FILTERS = "DROP TABLE IF EXISTS " + FilterEntry.TABLE_NAME;
}
