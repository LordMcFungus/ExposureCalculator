package com.example.informatik.exposurecalculator;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addfilterActivity extends AppCompatActivity {

    private Button addFilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfilter);

        //Fareb des Buttons ändern
        addFilterButton = (Button) findViewById(R.id.add_filter_button);
        addFilterButton.getBackground().setColorFilter(0xFF85A0C3, PorterDuff.Mode.MULTIPLY);
    }


    public void saveFilterClick(View view) {
        //Daten aus eingabefeldern auslesen
        EditText editTextFilterName = (EditText) findViewById(R.id.filter_name);
        EditText editTextFilterStops = (EditText) findViewById(R.id.filter_stops);
        String filterName = editTextFilterName.getText().toString();


        //Länge des Filternamens überprüfen, liegt er nicht zwischen 1 und 30 wird eine Errormessage im EditText angeziegt
        boolean error = false;
        if(filterName.length() == 0 || filterName.length() > 30) {
            editTextFilterName.setError("The Filtername must be between 1 and 30 characters long!");
            error = true;
        }

        //Anzahl stops des Filters einlesen, liegt er nicht zweischen 1 und 15 wird eine Errormessage im Edittext angezeigt.
        int filterStops;
        try{
            //Engabe in int parse, wirde nichts eingegeben wird dem int den wert 0 zugewiesen
            filterStops = editTextFilterStops.getText().toString().equals("") ? 0 : Integer.parseInt(editTextFilterStops.getText().toString());

            if(filterStops > 15 || filterStops < 1) {
                editTextFilterStops.setError("A filter must have between 1 and 15 Stops");
                error = true;
            }
        } catch (NumberFormatException nfe) {
            editTextFilterStops.setError("A filter must have between 1 and 15 Stops");
            return;
        }

        if(error) return;

        //Daten des filters in Datenbank speichern und zurück zur mainactivity gehen.
        Filter filter = new Filter(filterName, filterStops);
        DbManager dbMngr = new DbManager(this);
        filter.setId(dbMngr.insertFilter(filter));

        finish();
    }
}
