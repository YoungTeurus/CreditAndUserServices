package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.constructor.Parameter;
import com.github.youngteurus.servletdatabase.database.constructor.StringParameter;
import com.github.youngteurus.servletdatabase.modelconnectors.AbstractModelDatabaseConnector;
import database.CreditPostgresDataBase;
import models.Branch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BranchDatabaseConnector extends AbstractModelDatabaseConnector<Branch> {
    private static BranchDatabaseConnector instance;

    private BranchDatabaseConnector(DataBase db) {
        super(db);
    }

    public static BranchDatabaseConnector getInstance(){
        if (instance == null) {
            DataBase db = CreditPostgresDataBase.getInstance();
            instance = new BranchDatabaseConnector(db);
        }
        return instance;
    }

    @Override
    protected String getTableName() {
        return "branches";
    }

    @Override
    protected Branch constructObjectFromResultSet(ResultSet resultSet) {
        // Колонки, возвращаемые SQL запросом:
        // id, name
        Branch branch = null;

        try{
            branch = new Branch(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return branch;
    }

    @Override
    protected List<Parameter> getParametersForInsert(Branch branch) {
        List<Parameter> parameters = new ArrayList<>();

        parameters.add(new StringParameter("name", branch.getName()));

        return parameters;
    }

    @Override
    protected List<Parameter> getParametersForRemove(Branch branch) {
        throw new UnsupportedOperationException();
    }
}
