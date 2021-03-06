package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.database.constructor.DateParameter;
import com.github.youngteurus.servletdatabase.database.constructor.LongParameter;
import com.github.youngteurus.servletdatabase.database.constructor.Parameter;
import com.github.youngteurus.servletdatabase.database.constructor.StringParameter;
import com.github.youngteurus.servletdatabase.modelconnectors.AbstractModelDatabaseConnector;
import database.CreditPostgresDataBase;
import models.Sex;
import models.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDatabaseConnector extends AbstractModelDatabaseConnector<User> {
    private UserDatabaseConnector(DataBase db){
        super(db);
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    private final SexDatabaseConnector sexDatabaseConnector = SexDatabaseConnector.getInstance();
    private static UserDatabaseConnector instance;

    public static UserDatabaseConnector getInstance() {
        if (instance == null) {
            // TODO: временное решение проблемы с базами данных в UserDatabaseConnector:
            DataBase db = CreditPostgresDataBase.getInstance();
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

        return params;
    }

    @Override
    protected User constructObjectFromResultSet(ResultSet rs) {
        // Оторванность sql запроса и разбирания результа запроса для создания объекта напрягает.
        // Колонки, возвращаемые SQL запросом:
        // id, firstname, birth_date, passport_number, sex_id, surname, patronymic, tax_payer_id, driver_licence_id
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
                    .driverLicenceId(rs.getString("driver_licence_id")).build();
            return user;
        } catch (SQLException e){
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

        return getByParameters(params);
    }

    public final List<User> getByFirstnameSurnameAndPassport(String firstname, String surname, String passportNumber) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new StringParameter("firstname", firstname));
        params.add(new StringParameter("surname", surname));
        params.add(new StringParameter("passport_number", passportNumber));

        return getByParameters(params);
    }

    private Sex getSexById(int id){
        Sex returnSex = null;
        try {
            returnSex = sexDatabaseConnector.getById(id);
        } catch (SQLException | DataBaseConnectionException ignored){
        }
        return returnSex;
    }

    @Override
    protected List<Parameter> getParametersForRemove(User user) {
        throw new UnsupportedOperationException();
    }
}
