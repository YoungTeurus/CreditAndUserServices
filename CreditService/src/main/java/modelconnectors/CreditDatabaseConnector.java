package modelconnectors;

import database.DataBase;
import database.DataBaseConnectionException;
import database.PostgresDataBase;
import database.constructor.BigDecimalParameter;
import database.constructor.DateParameter;
import database.constructor.LongParameter;
import database.constructor.Parameter;
import models.Credit;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditDatabaseConnector extends BaseDatabaseConnector<Credit> {
    private CreditDatabaseConnector(DataBase db){
        super(db);
    }

    @Override
    protected String getTableName() {
        return "credits";
    }

    private static CreditDatabaseConnector instance;

    public static CreditDatabaseConnector getInstance() {
        if (instance == null) {
            // TODO: временное решение проблемы с базами данных в CreditDatabaseConnector:
            DataBase db = PostgresDataBase.getCreditServiceInstance();
            instance = new CreditDatabaseConnector(db);
        }
        return instance;
    }

    @Override
    protected Credit constructObjectFromResultSet(ResultSet rs) {
        // Колонки, возвращаемые SQL запросом:
        // id, userId, totalSum, startPaying, endPaying
        Credit returnCredit = null;
        try {
            returnCredit = new Credit.Builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getLong("userId"))
                    .totalSum(rs.getBigDecimal("totalSum"))
                    .startPaymentDate(rs.getDate("startPaying").toLocalDate())
                    .endPaymentDate(rs.getDate("endPaying").toLocalDate())
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnCredit;
    }

    @Override
    protected List<Parameter> getParametersForInsert(Credit credit) {
        List<Parameter> params = new ArrayList<>();

        params.add(new LongParameter("userId", credit.getUserId()));
        params.add(new BigDecimalParameter("totalSum", credit.getTotalSum()));
        params.add(new DateParameter("startPaymentDate", Date.valueOf(credit.getStartPaymentDate())));
        params.add(new DateParameter("endPaymentDate", Date.valueOf(credit.getEndPaymentDate())));
        params.add(new LongParameter("branchId", credit.getBranchId()));

        return params;
    }

    public List<Credit> getByUserId(int userId) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new LongParameter("userId", userId));

        List<Credit> foundCredits = getByParameters(params);

        return foundCredits;
    }

    @Override
    protected ResultSet getResultSetOfRemovedObjectId(long id) throws SQLException, DataBaseConnectionException {
        // Удалять записи о кредитах нельзя.
        throw new UnsupportedOperationException();
    }
}
