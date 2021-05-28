package database.constructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseParameter<T> implements Parameter {
    private String parameter;
    protected T value;

    public BaseParameter(String parameter, T value){
        this.parameter = parameter;
        this.value = value;
    }

    public String getParameter() {
        return parameter;
    }

    public abstract void applyParameter(PreparedStatement ps, int applyIndex) throws SQLException;

    @Override
    public String toString() {
        return "BaseParameter{" +
                "parameter='" + parameter + '\'' +
                ", value=" + value +
                '}';
    }
}
