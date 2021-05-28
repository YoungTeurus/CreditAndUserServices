package modelconnectors;

import database.DataBase;
import database.DataBaseConnectionException;
import database.PostgresDataBase;
import database.constructor.BaseParameter;
import database.constructor.StatementConstructor;
import database.constructor.StringParameter;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLConstructorTest {
    @Test
    void constructSQLStringFromParametersWithoutCompletion(){
        List<BaseParameter> params = new ArrayList<>();

        params.add(new StringParameter("param_1", null));
        params.add(new StringParameter("param_2", null));
        params.add(new StringParameter("param_3", null));

        String tableName = "users";

        String sql = StatementConstructor.constructSelectSQLQuery(tableName, params);

        System.out.println(sql);
        assertEquals("SELECT FROM public.\""+tableName+"\" WHERE param_1 = ?, param_2 = ?, param_3 = ?", sql);
    }

    @Test
    void constructSQLStringFromParametersWithCompletion() throws DataBaseConnectionException, SQLException {
        DataBase db = PostgresDataBase.getInstance();
        Connection connection = db.getConnection();

        List<BaseParameter> params = new ArrayList<>();

        params.add(new StringParameter("param_1", "value_1"));
        params.add(new StringParameter("param_2", "value_2"));
        params.add(new StringParameter("param_3", "value_3"));

        String tableName = "users";

        String sql = StatementConstructor.constructSelectSQLQuery(tableName, params);
        PreparedStatement ps = StatementConstructor.prepareStatement(connection, sql, params);

        System.out.println(ps.toString());
        assertEquals("SELECT FROM public.\""+tableName+"\" WHERE param_1 = 'value_1', param_2 = 'value_2', param_3 = 'value_3'", ps.toString());
    }
}