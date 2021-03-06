package models;

import com.github.youngteurus.servletdatabase.models.AbstractModel;

import java.time.LocalDate;

public class Payment extends AbstractModel {
    private final long id;
    private final long creditId;
    private final double sum;
    private final LocalDate date;

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

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", creditId=" + creditId +
                ", sum=" + sum +
                ", date=" + date +
                '}';
    }
}
