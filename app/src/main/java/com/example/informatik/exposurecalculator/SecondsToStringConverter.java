package com.example.informatik.exposurecalculator;

/**
 * Created by Informatik on 10.03.2017.
 */

public class SecondsToStringConverter {

    //Sekunden in format dd hh mm ss umwandeln
    public String convertSecondsToString(int shutter) {
        String finalString;
        int tmp;
        int seconds;
        int minutes;
        int hours;
        int days;

        if(shutter < 60){finalString = shutter + "s";}
        else {
            seconds = shutter % 60;
            minutes = (shutter - seconds)/60;
            if(minutes < 60) {finalString = minutes + "m " + seconds + "s";}
            else{
                tmp = minutes;
                minutes = minutes % 60;
                hours = (tmp - minutes)/60;
                if(hours < 24){finalString = hours +"h " + minutes + "m " + seconds + "s";}
                else{
                    tmp = hours;
                    hours = hours % 24;
                    days = (tmp - hours)/24;
                    finalString = days + "d " +  hours +"h " + minutes + "m " + seconds + "s";
                }
            }
        }
        return finalString;
    }
}
