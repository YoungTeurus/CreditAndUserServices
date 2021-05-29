package modelconnectors;

import database.DataBase;
import database.DataBaseConnectionException;
import database.PostgresDataBase;
import database.constructor.DateParameter;
import database.constructor.LongParameter;
import database.constructor.Parameter;
import database.constructor.StringParameter;
import models.Sex;
import models.User;
import models.UserServiceUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServiceUserDatabaseConnector extends BaseDatabaseConnector<UserServiceUser> {
    private UserServiceUserDatabaseConnector(DataBase db){
        super(db);
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    private final SexDatabaseConnector sexDatabaseConnector = SexDatabaseConnector.getInstance();
    private static UserServiceUserDatabaseConnector instance;

    public static UserServiceUserDatabaseConnector getInstance() {
        if (instance == null) {
            // TODO: временное решение проблемы с базами данных в UserServiceUserDatabaseConnector:
            DataBase db = PostgresDataBase.getUserServiceInstance();
            instance = new UserServiceUserDatabaseConnector(db);
        }
        return instance;
    }

    @Override
    protected List<Parameter> getParametersForInsert(UserServiceUser user) {
        List<Parameter> params = new ArrayList<>();

        params.add(new StringParameter("firstname", user.getFirstname()));
        params.add(new StringParameter("surname", user.getSurname()));
        params.add(new DateParameter("birth_date", Date.valueOf(user.getBirthDate())));
        params.add(new LongParameter("sex_id", user.getSex().getId()));
        params.add(new StringParameter("passport_number", user.getPassportNumber()));
        params.add(new StringParameter("tax_payer_id", user.getTaxPayerID()));
        params.add(new StringParameter("driver_licence_id", user.getDriverLicenceId()));
        params.add(new LongParameter("creditServiceId", user.getCreditServiceId()));

        return params;
    }

    @Override
    protected final ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException {
        Connection connection = db.getConnection();
        String sql = "DELETE FROM public.\"users\" WHERE id = ? RETURNING id;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);

        return db.executeStatement(preparedStatement);
    }
    @Override
    protected UserServiceUser constructObjectFromResultSet(ResultSet rs) {
        // Оторванность sql запроса и разбирания результа запроса для создания объекта напрягает.
        // Колонки, возвращаемые SQL запросом:
        // id, firstname, birth_date, passport_number, sex_id, surname, tax_payer_id, driver_licence_id, creditServiceId
        try {
            int userSexForeignKey = rs.getInt("sex_id");
            Sex usersSex = getSexById(userSexForeignKey);

            UserServiceUser user = new UserServiceUser.Builder(rs.getString("firstname"))
                    .id(rs.getInt("id"))
                    .birthDate(rs.getDate("birth_date").toLocalDate())
                    .passportNumber(rs.getString("passport_number"))
                    .sex(usersSex)
                    .surname(rs.getString("surname"))
                    .taxPayerID(rs.getString("tax_payer_id"))
                    .driverLicenceId(rs.getString("driver_licence_id"))
                    .creditServiceId(rs.getLong("creditServiceId"))
                    .build();
            return user;
        } catch (SQLException e){
            // TODO: решить, как обрабатывать ошибку при невозможности создать user из полученных данных.
            // Либо слать ошибку дальше по стеку вызовов, либо возвращать null.
            return null;
        }
    }


    public final UserServiceUser getByPassportID(String passport) throws SQLException, DataBaseConnectionException  {
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("passport_number", passport));

        List<UserServiceUser> foundUsers = getByParameters(params);

        if (foundUsers.size() > 0) {
            return foundUsers.get(0);
        }
        return null;
    }

    public final List<UserServiceUser> getByFirstname(String firstname) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("firstname", firstname));

        List<UserServiceUser> foundUsers = getByParameters(params);

        return foundUsers;
    }

    public final List<UserServiceUser> getByFirstnameSurnameAndPassport(String firstname, String surname, String passportNumber) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("firstname", firstname));
        params.add(new StringParameter("surname", surname));
        params.add(new StringParameter("passport_number", passportNumber));

        List<UserServiceUser> foundUsers = getByParameters(params);

        return foundUsers;
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
