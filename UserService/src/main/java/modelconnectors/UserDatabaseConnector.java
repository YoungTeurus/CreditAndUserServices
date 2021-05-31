package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.database.constructor.DateParameter;
import com.github.youngteurus.servletdatabase.database.constructor.LongParameter;
import com.github.youngteurus.servletdatabase.database.constructor.Parameter;
import com.github.youngteurus.servletdatabase.database.constructor.StringParameter;
import com.github.youngteurus.servletdatabase.modelconnectors.AbstractModelDatabaseConnector;
import database.UserPostgresDataBase;
import models.Sex;
import models.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseConnector extends AbstractModelDatabaseConnector<User> {
    // private final ParentsDatabaseConnector parentsDatabaseConnector = ParentsDatabaseConnector.getInstance();
    private final SexDatabaseConnector sexDatabaseConnector = SexDatabaseConnector.getInstance();
    private static UserDatabaseConnector instance;

    private UserDatabaseConnector(DataBase db){
        super(db);
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    public static UserDatabaseConnector getInstance() {
        if (instance == null) {
            DataBase db = UserPostgresDataBase.getInstance();
            instance = new UserDatabaseConnector(db);
        }
        return instance;
    }

    @Override
    protected List<Parameter> getParametersForInsert(User user) {
        List<Parameter> params = new ArrayList<>();

        params.add(new StringParameter("firstname", user.getFirstname()));
        params.add(new StringParameter("surname", user.getSurname()));
        params.add(new StringParameter("patronymic", user.getPatronymic()));
        params.add(new DateParameter("birth_date", Date.valueOf(user.getBirthDate())));
        params.add(new LongParameter("sex_id", user.getSex().getId()));
        params.add(new StringParameter("passport_number", user.getPassportNumber()));
        params.add(new StringParameter("tax_payer_id", user.getTaxPayerID()));
        params.add(new StringParameter("driver_licence_id", user.getDriverLicenceId()));
        params.add(new LongParameter("creditServiceId", user.getCreditServiceId()));

        return params;
    }

    @Override
    protected List<Parameter> getParametersForRemove(User user) {
        // TODO: реализовать метод для удаления User-ов из таблицы
        throw new NotImplementedException();
    }

    @Override
    protected User constructObjectFromResultSet(ResultSet rs) {
        // Оторванность sql запроса и разбирания результа запроса для создания объекта напрягает.
        // Колонки, возвращаемые SQL запросом:
        // id, firstname, birth_date, passport_number, sex_id, surname, tax_payer_id, driver_licence_id, creditServiceId
        try {
            int userSexForeignKey = rs.getInt("sex_id");
            Sex usersSex = getSexById(userSexForeignKey);

            User user = new User.Builder(rs.getString("firstname"))
                    .id(rs.getInt("id"))
                    .birthDate(rs.getDate("birth_date").toLocalDate())
                    .passportNumber(rs.getString("passport_number"))
                    .sex(usersSex)
                    .surname(rs.getString("surname"))
                    .patronymic(rs.getString("patronymic"))
                    .taxPayerID(rs.getString("tax_payer_id"))
                    .driverLicenceId(rs.getString("driver_licence_id"))
                    .creditServiceId(rs.getLong("creditServiceId"))
                    .build();
            return user;
        } catch (SQLException e){
            // TODO: решить, как обрабатывать ошибку при невозможности создать user из полученных данных.
            // Либо слать ошибку дальше по стеку вызовов, либо возвращать null.
            e.printStackTrace();
            return null;
        }
    }


    public final User getByPassportID(String passport) throws SQLException, DataBaseConnectionException  {
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("passport_number", passport));

        List<User> foundUsers = getByParameters(params);

        if (foundUsers.size() > 0) {
            return foundUsers.get(0);
        }
        return null;
    }

    public final List<User> getByFirstname(String firstname) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("firstname", firstname));

        List<User> foundUsers = getByParameters(params);

        return foundUsers;
    }

    public final List<User> getByFullNameAndPassport(String firstname, String surname, String patronymic, String passportNumber) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("firstname", firstname));
        params.add(new StringParameter("surname", surname));
        params.add(new StringParameter("patronymic", patronymic));
        params.add(new StringParameter("passport_number", passportNumber));

        List<User> foundUsers = getByParameters(params);

        return foundUsers;
    }

    public final List<User> getByFullNameAndDriverId(String firstname, String surname, String patronymic, String driverID) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("firstname", firstname));
        params.add(new StringParameter("surname", surname));
        params.add(new StringParameter("patronymic", patronymic));
        params.add(new StringParameter("driver_licence_id", driverID));

        List<User> foundUsers = getByParameters(params);

        return foundUsers;
    }

    public final List<User> getByFullNameAndTaxID(String firstname, String surname, String patronymic, String taxID) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("firstname", firstname));
        params.add(new StringParameter("surname", surname));
        params.add(new StringParameter("patronymic", patronymic));
        params.add(new StringParameter("tax_payer_id", taxID));

        List<User> foundUsers = getByParameters(params);

        return foundUsers;
    }

    private List<User> getParentsById(long id){
        // List<User> parents = parentsDatabaseConnector.getParentsById(id);

        return null;
    }

    private Sex getSexById(int id){
        Sex returnSex = null;
        try {
            returnSex = sexDatabaseConnector.getById(id);
        } catch (SQLException | DataBaseConnectionException e){
            // TODO: что делать, если не смогли получить пол?
            // return null?
        }
        return returnSex;
    }
}
