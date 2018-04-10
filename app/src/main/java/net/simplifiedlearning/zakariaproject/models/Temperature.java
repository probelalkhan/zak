package net.simplifiedlearning.zakariaproject.models;

/**
 * Created by Belal on 9/25/2017.
 */

public class Temperature {
    int id;
    double temp1, temp2;
    String date;

    public Temperature(int id, double temp1, double temp2, String date) {
        this.id = id;
        this.temp1 = Math.round(temp1 * 100) / 100;
        this.temp2 = Math.round(temp2 * 100) / 100;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getTemp1() {
        return temp1;
    }

    public double getTemp2() {
        return temp2;
    }

    public String getDate() {
        return date;
    }

    public Double getDifference() {
        return temp2 - temp1;
    }
}
