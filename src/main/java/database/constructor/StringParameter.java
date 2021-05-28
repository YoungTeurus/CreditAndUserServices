package database.constructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringParameter extends BaseParameter<String> {
    public StringParameter(String parameter, String value) {
        super(parameter, value);
    }

    @Override
    public void applyParameter(PreparedStatement ps, int applyIndex) throws SQLException {
        ps.setString(applyIndex, value);
    }
}
