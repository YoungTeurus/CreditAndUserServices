package database.constructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntegerParameter extends BaseParameter<Integer> {
    public IntegerParameter(String parameter, int value){
        super(parameter, value);
    }

    @Override
    public void applyParameter(PreparedStatement ps, int applyIndex) throws SQLException {
        ps.setInt(applyIndex, value);
    }
}