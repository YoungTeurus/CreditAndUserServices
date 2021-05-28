package models;

import java.time.LocalDate;

// TODO: добавить Builder-а
public class Credit extends AbstactModel{
    private final long id;
    private final long userId;
    private final long branchId;
    private final double totalSum;
    private final LocalDate startPaymentDate;
    private final LocalDate endPaymentDate;

    public Credit(long id, long userId, long branchId, double totalSum, LocalDate startPaymentDate, LocalDate endPaymentDate) {
        this.id = id;
        this.userId = userId;
        this.branchId = branchId;
        this.totalSum = totalSum;
        this.startPaymentDate = startPaymentDate;
        this.endPaymentDate = endPaymentDate;
    }

    public long getUserId() {
        return userId;
    }

    public long getBranchId() {
        return branchId;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public LocalDate getStartPaymentDate() {
        return startPaymentDate;
    }

    public LocalDate getEndPaymentDate() {
        return endPaymentDate;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", userId=" + userId +
                ", branchId=" + branchId +
                ", totalSum=" + totalSum +
                ", startPaymentDate=" + startPaymentDate +
                ", endPaymentDate=" + endPaymentDate +
                '}';
    }
}
