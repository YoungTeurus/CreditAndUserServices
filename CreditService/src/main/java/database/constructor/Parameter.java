package database.constructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Parameter {
    String getParameter();
    void applyParameter(PreparedStatement ps, int applyIndex) throws SQLException;
}
