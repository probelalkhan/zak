package net.simplifiedlearning.zakariaproject.models;

/**
 * Created by Belal on 9/25/2017.
 */

public class Pressure {
    int id;
    double pressure1, pressure2;
    String date;

    public Pressure(int id, double pressure1, double temp2, String date) {
        this.id = id;
        this.pressure1 = Math.round(pressure1 * 100) / 100;
        this.pressure2 = Math.round(temp2 * 100) / 100;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getPressure1() {
        return pressure1;
    }

    public double getPressure2() {
        return pressure2;
    }

    public String getDate() {
        return date;
    }

    public Double getDifference() {
        return pressure2 - pressure1;
    }
}
