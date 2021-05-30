package constructor;

import database.DataBase;
import database.DataBaseConnectionException;
import database.PostgresDataBase;
import database.constructor.*;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatementConstructorTest {
    @Test
    void constructSelectSQLQuery() {
        List<Parameter> params = new ArrayList<>();

        params.add(new StringParameter("param_1", null));
        params.add(new StringParameter("param_2", null));
        params.add(new StringParameter("param_3", null));

        String tableName = "users";

        String sql = StatementConstructor.constructSelectSQLQuery(tableName, params);

        System.out.println(sql);
        assertEquals("SELECT * FROM public.\"users\" WHERE param_1 = ? AND param_2 = ? AND param_3 = ?", sql);
    }

    @Test
    void constructInsertSQLQuery(){
        List<Parameter> params = new ArrayList<>();

        params.add(new StringParameter("param_1", null));
        params.add(new StringParameter("param_2", null));
        params.add(new StringParameter("param_3", null));

        String tableName = "users";

        String sql = StatementConstructor.constructInsertSQLQuery(tableName, params);

        System.out.println(sql);
        assertEquals("INSERT INTO public.\"users\" (\"param_1\",\"param_2\",\"param_3\") Values (?,?,?) RETURNING id", sql);
    }

    @Test
    void constructSelectStatementFromParametersList() throws SQLException, DataBaseConnectionException {
        DataBase db = PostgresDataBase.getInstance();
        Connection connection = db.getConnection();

        List<Parameter> params = new ArrayList<>();

        params.add(new StringParameter("param_1", "value_1"));
        params.add(new LongParameter("param_2", 200000));
        params.add(new DateParameter("param_3", Date.valueOf(LocalDate.of(2000, 12, 12))));

        String tableName = "users";

        PreparedStatement ps = StatementConstructor.constructSelectStatementFromParametersList(connection, tableName, params);

        System.out.println(ps.toString());
        assertEquals("SELECT * FROM public.\"users\" WHERE param_1 = 'value_1' AND param_2 = 200000 AND param_3 = '2000-12-12 +03'", ps.toString());
    }

    @Test
    void constructInsertStatementFromParametersList() throws DataBaseConnectionException, SQLException {
        DataBase db = PostgresDataBase.getInstance();
        Connection connection = db.getConnection();

        List<Parameter> params = new ArrayList<>();

        params.add(new StringParameter("param_1", "value_1"));
        params.add(new LongParameter("param_2", 200000));
        params.add(new DateParameter("param_3", Date.valueOf(LocalDate.of(2000, 12, 12))));

        String tableName = "users";

        PreparedStatement ps = StatementConstructor.constructInsertStatementFromParametersList(connection, tableName, params);

        System.out.println(ps.toString());
        assertEquals("INSERT INTO public.\"users\" (\"param_1\",\"param_2\",\"param_3\") Values ('value_1',200000,'2000-12-12 +03') RETURNING id", ps.toString());
    }
}