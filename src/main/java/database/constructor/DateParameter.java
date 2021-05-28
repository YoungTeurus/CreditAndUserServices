package database.constructor;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DateParameter extends BaseParameter<Date> {
    public DateParameter(String parameter, Date value){
        super(parameter, value);
    }

    @Override
    public void applyParameter(PreparedStatement ps, int applyIndex) throws SQLException {
        ps.setDate(applyIndex, value);
    }
}
