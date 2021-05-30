package database.constructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DoubleParameter extends BaseParameter<Double> {
    public DoubleParameter(String parameter, double value){
        super(parameter, value);
    }

    @Override
    public void applyParameter(PreparedStatement ps, int applyIndex) throws SQLException {
        ps.setDouble(applyIndex, value);
    }
}
