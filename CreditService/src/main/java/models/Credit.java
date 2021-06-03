package models;

import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.models.AbstractModel;
import modelconnectors.BranchDatabaseConnector;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class Credit extends AbstractModel {
    private final long id;
    private final long userId;
    private final Branch branch;
    private final BigDecimal totalSum;
    private final LocalDate startPaymentDate;
    private final LocalDate endPaymentDate;

    public static class Builder {
        private long id = 0;
        private long userId = 0;
        private Branch branch;
        private BigDecimal totalSum = new BigDecimal("0.0");
        private LocalDate startPaymentDate = LocalDate.now();
        private LocalDate endPaymentDate = LocalDate.now();

        public Builder(){
            try{
                branch = BranchDatabaseConnector.getInstance().getById(1);
            } catch (SQLException | DataBaseConnectionException ignored){}
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder branch(Branch branch){
            this.branch = branch;
            return this;
        }

        public Builder totalSum(BigDecimal totalSum) {
            this.totalSum = totalSum;
            return this;
        }

        public Builder startPaymentDate(LocalDate startPaymentDate) {
            this.startPaymentDate = startPaymentDate;
            return this;
        }

        public Builder endPaymentDate(LocalDate endPaymentDate) {
            this.endPaymentDate = endPaymentDate;
            return this;
        }

        public Credit build() {
            return new Credit(this);
        }
    }

    private Credit(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.branch = builder.branch;
        this.totalSum = builder.totalSum;
        this.startPaymentDate = builder.startPaymentDate;
        this.endPaymentDate = builder.endPaymentDate;
    }

    public long getUserId() {
        return userId;
    }

    public Branch getBranch(){
        return branch;
    }

    public BigDecimal getTotalSum() {
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
                ", branch=" + branch +
                ", totalSum=" + totalSum +
                ", startPaymentDate=" + startPaymentDate +
                ", endPaymentDate=" + endPaymentDate +
                '}';
    }
}
