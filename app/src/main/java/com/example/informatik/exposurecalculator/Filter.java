package com.example.informatik.exposurecalculator;

/**
 * Created by Informatik on 09.03.2017.
 */

// DTO mit Daten eines Filters
public class Filter {
    private Long Id;
    private String Name;
    private int Stops;

    //Constructor for new Filter
    public Filter(String name, int stops) {
        this.Name = name;
        this.Stops = stops;
    }

    //Constructor for Filter from Database
    public Filter(Long id, String name, int stops) {
        this.Name = name;
        this.Stops = stops;
        this.Id = id;
    }


    public long getId() {
        return Id;
    }
    public void setId(long id) {
        this.Id = id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        this.Name = name;
    }
    public int getStops() {
        return Stops;
    }
    public void setStops(int stops) {
        this.Stops = stops;
    }


}
