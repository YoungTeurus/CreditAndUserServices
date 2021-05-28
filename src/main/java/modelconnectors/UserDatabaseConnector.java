package modelconnectors;

import database.DataBaseConnectionException;
import models.Sex;
import models.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class UserDatabaseConnector extends BaseDatabaseConnector<User> {
    private final SexDatabaseConnector sexDatabaseConnector = SexDatabaseConnector.getInstance();
    private static UserDatabaseConnector instance;

    public static UserDatabaseConnector getInstance() {
        if (instance == null) {
            instance = new UserDatabaseConnector();
        }
        return instance;
    }

    @Override
    protected final ResultSet getResultSetOfAddedObjectId(User user) throws DataBaseConnectionException, SQLException {
        String sql = "INSERT INTO public.\"users\" (firstname, surname, birth_date, sex_id, passport_number, tax_payer_id, driver_licence_id)" +
                " Values (?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        Connection connection = db.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);


        preparedStatement.setString(1, user.getFirstname());
        preparedStatement.setString(2, user.getSurname());
        preparedStatement.setDate(3, Date.valueOf(user.getBirthDate()));
        preparedStatement.setLong(4, user.getSex().getId());
        preparedStatement.setString(5, user.getPassportNumber());
        preparedStatement.setString(6, user.getTaxPayerID());
        preparedStatement.setString(7, user.getDriverLicenceId());

        return db.executeStatement(preparedStatement);
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
    protected ResultSet getResultSetOfObjectOfId(long id) throws SQLException, DataBaseConnectionException {
        Connection connection = db.getConnection();

        String sql = "SELECT * FROM public.\"users\" WHERE id = ?";
        // String sql = "SELECT" +
        //                " birth_date," +
        //                " firstname," +
        //                " users.id as \"id\"," +
        //                " passport_number," +
        //                " s.name as \"sex_name\"," +
        //                " surname," +
        //                " tax_payer_id," +
        //                " driver_licence_id" +
        //                " FROM public.\"users\" JOIN public.\"sexes\" s on s.id = public.\"users\".sex_id WHERE users.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        return db.executeStatement(preparedStatement);
    }

    @Override
    protected ResultSet getResultSetOfAllObjects() throws SQLException, DataBaseConnectionException {
        Connection connection = db.getConnection();

        String sql = "SELECT * FROM public.\"users\";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return db.executeStatement(preparedStatement);
    }

    @Override
    protected User constructObjectFromResultSet(ResultSet rs) {
        // Оторванность sql запроса и разбирания результа запроса для создания объекта напрягает.
        // Колонки, возвращаемые SQL запросом:
        // id, firstname, birth_date, passport_number, sex_id, surname, tax_payer_id, driver_licence_id
        try {
            int userSexForeignKey = rs.getInt("sex_id");
            Sex usersSex = getSexById(userSexForeignKey);

            User user = new User.Builder(rs.getString("firstname"))
                    .id(rs.getInt("id"))
                    .birthDate(rs.getDate("birth_date").toLocalDate())
                    .passportNumber(rs.getString("passport_number"))
                    .sex(usersSex)
                    .surname(rs.getString("surname"))
                    .taxPayerID(rs.getString("tax_payer_id"))
                    .driverLicenceId(rs.getString("driver_licence_id")).build();
            return user;
        } catch (SQLException e){
            // TODO: решить, как обрабатывать ошибку при невозможности создать user из полученных данных.
            // Либо слать ошибку дальше по стеку вызовов, либо возвращать null.
            return null;
        }
    }

    private Sex getSexById(int id){
        Sex returnSex = null;
        try {
            returnSex = sexDatabaseConnector.get(id);
        } catch (SQLException | DataBaseConnectionException e){
            // TODO: что делать, если не смогли получить пол?
        }
        return returnSex;
    }
}
