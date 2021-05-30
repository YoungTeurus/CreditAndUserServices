package models;

import com.github.youngteurus.servletdatabase.models.AbstractModel;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment extends AbstractModel {
    private long id;
    private long creditId;
    private BigDecimal sum;
    private LocalDate date;

    public Payment(long id, long creditId, BigDecimal sum, LocalDate date) {
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

    public BigDecimal getSum() {
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
