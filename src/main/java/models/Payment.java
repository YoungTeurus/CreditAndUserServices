package models;

import java.time.LocalDate;

// TODO: добавить Builder-а
public class Payment extends AbstactModel{
    private long id;
    private long creditId;
    private double sum;
    private LocalDate date;

    public Payment(long id, long creditId, double sum, LocalDate date) {
        this.id = id;
        this.creditId = creditId;
        this.sum = sum;
        this.date = date;
    }

    @Override
    public long getId() {
        return id;
    }

    public long getCreditId() {
        return creditId;
    }

    public double getSum() {
        return sum;
    }

    public LocalDate getDate() {
        return date;
    }
}
