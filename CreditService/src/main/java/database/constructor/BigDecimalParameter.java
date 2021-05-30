package database.constructor;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BigDecimalParameter extends BaseParameter<BigDecimal>{
    public BigDecimalParameter(String parameter, BigDecimal value){
        super(parameter, value);
    }

    @Override
    public void applyParameter(PreparedStatement ps, int applyIndex) throws SQLException {
        // TODO: протестировать работу вставки BigDecimal в таблицу.
        ps.setBigDecimal(applyIndex, value);
    }
}
