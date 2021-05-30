package models;

import com.github.youngteurus.servletdatabase.models.AbstractModel;
import modelconnectors.SexDatabaseConnector;

import java.time.LocalDate;

public class User extends AbstractModel {
    private final long id;
    private final long creditServiceId;
    private final String firstname;
    private final String surname;
    private final String patronymic;
    private final LocalDate birthDate;
    private final Sex sex;
    private final String passportNumber;
    private final String taxPayerID;
    private final String driverLicenceId;

    public static class Builder {
        // Дефолтные поля
        private final String firstname;
        private long id = 0;
        private long creditServiceId = 0;
        private String surname = "";
        private String patronymic = "";
        private LocalDate birthDate = LocalDate.now();
        private Sex sex;
        private String passportNumber = "";
        private String taxPayerID = "";
        private String driverLicenceId = "";

        public Builder(String firstname) {
            this.firstname = firstname;
            try {
                this.sex = SexDatabaseConnector.getInstance().getById(1);
            } catch (Exception ignored) {

            }
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder creditServiceId(long creditServiceId){
            this.creditServiceId = creditServiceId;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder patronymic(String patronymic) {
            this.patronymic = patronymic;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder sex(Sex sex) {
            this.sex = sex;
            return this;
        }

        public Builder passportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
            return this;
        }

        public Builder taxPayerID(String taxPayerID) {
            this.taxPayerID = taxPayerID;
            return this;
        }

        public Builder driverLicenceId(String driverLicenceId) {
            this.driverLicenceId = driverLicenceId;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.creditServiceId = builder.creditServiceId;
        this.birthDate = builder.birthDate;
        this.driverLicenceId = builder.driverLicenceId;
        this.sex = builder.sex;
        this.passportNumber = builder.passportNumber;
        this.firstname = builder.firstname;
        this.surname = builder.surname;
        this.taxPayerID = builder.taxPayerID;
        this.patronymic = builder.patronymic;
    }

    public long getId() {
        return id;
    }

    public long getCreditServiceId(){
        return creditServiceId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Sex getSex() {
        return sex;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getTaxPayerID() {
        return taxPayerID;
    }

    public String getDriverLicenceId() {
        return driverLicenceId;
    }

    public String getPatronymic() {
        return patronymic;
    }

    @Override
    public String toString() {
        return "UserServiceUser{" +
                "id=" + id +
                ", creditServiceId=" + creditServiceId +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", sex=" + sex +
                ", passportNumber='" + passportNumber + '\'' +
                ", taxPayerID='" + taxPayerID + '\'' +
                ", driverLicenceId='" + driverLicenceId + '\'' +
                '}';
    }
}
