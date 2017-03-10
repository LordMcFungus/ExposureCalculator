package com.example.informatik.exposurecalculator;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TIMER_MESSAGE = "timeractivity.time";
    private float finalTime;
    private int shutter;
    private int currentExposure;
    private int stops;
    private ArrayList<Filter> filterList;
    private String[] filtersArray;
    private String finalTimeString;
    private Spinner exposureSpinner;
    private Spinner filterSpinner;
    private int expouserSelection;
    private int filterSelection;
    private boolean resume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Ändert die Fabren der Buttons
        Button button = (Button) findViewById(R.id.filter_add_button);
        button.getBackground().setColorFilter(0xFF85A0C3, PorterDuff.Mode.MULTIPLY);
        button = (Button) findViewById(R.id.timer_button);
        button.getBackground().setColorFilter(0xFF85A0C3, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpSpinners();

        //Auswahl der Spinners auf den verherigen Wert setzetn
        if(resume) {
            exposureSpinner.setSelection(expouserSelection);
            filterSpinner.setSelection(filterSelection);
        }


    }
    //Arrays den Spinners zuweisen
    private void setUpSpinners() {
        //Liste mit allen Filtern der Db auslesen und in Array speichern
        filterSpinner = (Spinner) findViewById(R.id.filter_spinner);
        DbManager dbMngr = new DbManager(this);

        filterList = dbMngr.getAllFilters();
        filtersArray = new String[filterList.size() + 1];
        filtersArray[0] = "Select a Filter";
        for (int i = 0; i < filterList.size(); i++) {
            filtersArray[i+1] = filterList.get(i).getName() + ": " + filterList.get(i).getStops() +" Stops";
        }
        //Array dem Filter Spinner zuweisen
        ArrayAdapter<String> adapterone = new ArrayAdapter<>(this,
                R.layout.spinner_item, filtersArray);
        adapterone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        filterSpinner.setAdapter(adapterone);

        //shutter_array vom Strigns.xml dem shutterspinner zuweisen
        exposureSpinner = (Spinner) findViewById(R.id.exposure_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shutter_array, R.layout.spinner_item);
     // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     // Apply the adapter to the spinner
        exposureSpinner.setAdapter(adapter);

        setUpSpinnerEvents();

    }

    //OnSelected Events den Spinners zuweisen, bei andern der selektion wird der Final shutter neu berechnet
    private void setUpSpinnerEvents() {
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                calculateExposure();
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        exposureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                calculateExposure();
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Final Shutter berechnen
    private void calculateExposure() {
        String currentExposure = exposureSpinner.getSelectedItem().toString();
        String appliedFilter = filterSpinner.getSelectedItem().toString();
        Filter filter = null;

        //Falls noch keine Filter in der Db vorhanden sind wird der standartwert ausgelesen und anzahl stops auf 0 gesetzt
        if(appliedFilter.equals("Select a Filter")) {
            stops = 0;
        }
        else{
            //Ausgewählten Filter suchen
            for(int i = 0;i < filterList.size() ; i++) {
                if((filterList.get(i).getName() + ": " + filterList.get(i).getStops() +" Stops").equals(appliedFilter)) {
                    filter = filterList.get(i);
                    break;
                }
            }

            if(filter == null) {
                return;
            }


            // 2er potenz des Filters finden
            stops = filter.getStops();
        }

        int multiplicator = 1;

        for(int i = 0; i < stops; i++) {
            multiplicator *= 2;
        }



        float base ;
        float dividor;


        // Falls es möglich ist, dass der entgültige shutterspeen keliner als eins ist, wir er anders formatioert (1/xx)
        if(currentExposure.length() >=3 ) {
            //String 1/xx aufteilen und float-value des bruches berechnen.
            String[] parts = currentExposure.split("/");
            base = Integer.parseInt(parts[0]);
            dividor = Integer.parseInt(parts[1]);

            float startShutter = base/dividor;

            finalTime = startShutter * multiplicator;

            //ist die Belichtungszeit kleiner als 1, Divisor berechenen und auf 10er runden, falls keliner als 12, auf 2er runden
            if(finalTime < 1){
                int diviser = 1/finalTime < 12 ? Math.round((1/finalTime)/2) * 2 : Math.round((1/finalTime)/10) * 10;
                finalTimeString = "1/"+diviser + " s";
            }
            else {
                shutter = Math.round(finalTime);
                finalTimeString = new SecondsToStringConverter().convertSecondsToString(shutter);
            }
        }
        else {
            int exposure = Integer.parseInt(currentExposure);
            shutter = Math.round(exposure * multiplicator);
            finalTimeString = new SecondsToStringConverter().convertSecondsToString(shutter);
        }


        // Finale belichtungszeit anzeigen und falls grösser als 4 den timerbutton enablen
        TextView finalTextView = (TextView) findViewById(R.id.final_shutter);
        finalTextView.setText(finalTimeString);
        Button timerButton = (Button) findViewById(R.id.timer_button);
        if(shutter > 4){  timerButton.setEnabled(true);}else {timerButton.setEnabled(false);}
        if(shutter > 4){  timerButton.setEnabled(true);}else {timerButton.setEnabled(false);}



    }

    // AddFilter Activity öffnen
    public void addFilterClick(View view) {
        Intent intent = new Intent(this, addfilterActivity.class);
        startActivity(intent);
    }

    //Timer Activity öffnen
    public void setTimerClick(View view) {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra(TIMER_MESSAGE, shutter);
        startActivity(intent);
    }

    // beim pausieren dieser Activity werden die auswahlen bei den Spinners gespeichert, um sie beim wiederöffen zu laden.
    @Override
    protected void onPause() {
        super.onPause();
        expouserSelection = exposureSpinner.getSelectedItemPosition();
        filterSelection = filterSpinner.getSelectedItemPosition();
        resume = true;

    }
}
