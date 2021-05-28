package database.constructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LongParameter extends BaseParameter<Long> {
    public LongParameter(String parameter, long value){
        super(parameter, value);
    }

    @Override
    public void applyParameter(PreparedStatement ps, int applyIndex) throws SQLException {
        ps.setLong(applyIndex, value);
    }
}
