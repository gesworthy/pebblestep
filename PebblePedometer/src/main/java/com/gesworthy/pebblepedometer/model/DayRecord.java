package com.gesworthy.pebblepedometer.model;

/**
 * Created by Gary on 2/8/14.
 */
public class DayRecord {

    private int id;
    private long date;
    private int steps;

    public DayRecord(final int id, final long date, final int steps) {
        this.id = id;
        this.date = date;
        this.steps = steps;
    }

    public DayRecord(final long date, final int steps) {
        this.date = date;
        this.steps = steps;
    }

    public long getDate() {
        return date;
    }

    public int getSteps() {
        return steps;
    }

    public int getId() {
        return id;
    }
}
