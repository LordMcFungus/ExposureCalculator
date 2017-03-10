package com.example.informatik.exposurecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Informatik on 09.03.2017.
 */

public class DbManager {
    // Instance variable
    private FilterReaderDbHelper dbHelper = null;
    private SQLiteDatabase db = null;

    public DbManager(Context context){
        // Creates a DB-Helper for out interactions with the database
        this.dbHelper = new FilterReaderDbHelper(context);
    }//-Constructor


    public long insertFilter(Filter filter){
        // Sets the data repository in write mode
        this.db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column NAMES are the KEYS
        ContentValues values = new ContentValues();
        values.put(FilterReaderContract.FilterEntry.CLMN_NAME, filter.getName());
        values.put(FilterReaderContract.FilterEntry.CLMN_STOPS, filter.getStops());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FilterReaderContract.FilterEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId; //returning new id
    }

    // Gibt eine ArrayList mit allen filter in der DB zurück
    public ArrayList<Filter> getAllFilters(){
        ArrayList<Filter> contacts = new ArrayList<Filter>();
        // Sets the data repository in read mode
        this.db = dbHelper.getReadableDatabase();

        String[] projection = {
                FilterReaderContract.FilterEntry._ID,
                FilterReaderContract.FilterEntry.CLMN_NAME,
                FilterReaderContract.FilterEntry.CLMN_STOPS,
        };

        String sortOrder = FilterReaderContract.FilterEntry._ID + " ASC";

        //Query um alle filter in der Datenbank auszulesen
        Cursor cursor = db.query(
                FilterReaderContract.FilterEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // Iterate through result rows
        while(cursor.moveToNext()) {
            contacts.add(new Filter(
                    cursor.getLong(cursor.getColumnIndexOrThrow(FilterReaderContract.FilterEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FilterReaderContract.FilterEntry.CLMN_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(FilterReaderContract.FilterEntry.CLMN_STOPS))
            ));
        }//-while
        cursor.close();
        db.close();
        return contacts;
    }//-function

    public void deleteAllFilters(){
        // Issue SQL statement.
        this.db = dbHelper.getReadableDatabase();
        db.delete(FilterReaderContract.FilterEntry.TABLE_NAME, null, null);
    }//-function
}
