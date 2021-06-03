package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.database.constructor.BigDecimalParameter;
import com.github.youngteurus.servletdatabase.database.constructor.DateParameter;
import com.github.youngteurus.servletdatabase.database.constructor.LongParameter;
import com.github.youngteurus.servletdatabase.database.constructor.Parameter;
import com.github.youngteurus.servletdatabase.modelconnectors.AbstractModelDatabaseConnector;
import database.CreditPostgresDataBase;
import models.Branch;
import models.Credit;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditDatabaseConnector extends AbstractModelDatabaseConnector<Credit> {
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
            DataBase db = CreditPostgresDataBase.getInstance();
            instance = new CreditDatabaseConnector(db);
        }
        return instance;
    }

    @Override
    protected Credit constructObjectFromResultSet(ResultSet rs) {
        // Колонки, возвращаемые SQL запросом:
        // id, userId, totalSum, startPaying, endPaying, branchId
        Credit returnCredit = null;
        try {
            long branchId = rs.getLong("branchId");
            Branch branch = BranchDatabaseConnector.getInstance().getById(branchId);

            returnCredit = new Credit.Builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getLong("userId"))
                    .totalSum(rs.getBigDecimal("totalSum"))
                    .startPaymentDate(rs.getDate("startPaying").toLocalDate())
                    .endPaymentDate(rs.getDate("endPaying").toLocalDate())
                    .branch(branch)
                    .build();
        } catch (SQLException | DataBaseConnectionException e) {
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

        return params;
    }

    public List<Credit> getByUserId(int userId) throws SQLException, DataBaseConnectionException {
        List<Parameter> params = new ArrayList<>();
        params.add(new LongParameter("userId", userId));

        return getByParameters(params);
    }

    @Override
    protected List<Parameter> getParametersForRemove(Credit credit) {
        throw new UnsupportedOperationException();
    }
}
