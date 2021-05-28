package database.constructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StatementConstructor {
    public static String constructSelectSQLQuery(String tableName, List<BaseParameter> parameters){
        StringBuilder sql = new StringBuilder();
        // Составляем sql запрос по типу:
        // SELECT FROM public."TABLE_NAME" WHERE param_1 = ?, param_2 = ?
        sql.append("SELECT FROM public.").append("\"").append(tableName).append("\"");
        if (parameters.size() > 0 ){
            int parametersCount = parameters.size();
            int currentParameter = 0;

            sql.append(" WHERE");
            for (BaseParameter param : parameters) {
                String SQLParameter = param.getParameter();
                sql.append(" ").append(SQLParameter).append(" = ?");
                if (currentParameter != parametersCount - 1){
                    sql.append(",");
                }
                currentParameter++;
            }
        }
        return sql.toString();
    }

    public static PreparedStatement prepareStatement(Connection connection, String sql, List<BaseParameter> parameters) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(sql);

        int paramIndex = 1;
        for(BaseParameter param : parameters){
            param.applyParameter(ps, paramIndex++);
        }

        return ps;
    }
}
