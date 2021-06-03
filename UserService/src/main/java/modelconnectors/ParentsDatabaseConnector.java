package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.database.constructor.*;
import com.github.youngteurus.servletdatabase.modelconnectors.BaseDatabaseConnector;
import database.UserPostgresDataBase;
import models.User;
import models.out.ParentAndChild;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParentsDatabaseConnector extends BaseDatabaseConnector<ParentAndChild> {
    private static ParentsDatabaseConnector instance;
    private ParentsDatabaseConnector(DataBase db){
        super(db);
    }
    public static ParentsDatabaseConnector getInstance() {
        if (instance == null) {
            DataBase db = UserPostgresDataBase.getInstance();
            instance = new ParentsDatabaseConnector(db);
        }
        return instance;
    }
    @Override
    protected String getTableName() {
        return "parents";
    }

    @Override
    protected ParentAndChild constructObjectFromResultSet(ResultSet resultSet) {
        // Колонки, возвращаемые SQL запросом:
        // parentId, childId
        try{
            ParentAndChild parentAndChild = new ParentAndChild(
                    resultSet.getInt("parentId"),
                    resultSet.getInt("childId")
            );

            return parentAndChild;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected List<Parameter> getParametersForInsert(ParentAndChild parentAndChild) {
        List<Parameter> params = new ArrayList<>();

        params.add(new IntegerParameter("parentId", parentAndChild.getParentId()));
        params.add(new IntegerParameter("childId", parentAndChild.getChildId()));

        return params;
    }

    @Override
    protected List<Parameter> getParametersForRemove(ParentAndChild parentAndChild) {
        return getParametersForInsert(parentAndChild);
    }

    public List<Integer> getChildrenIDsOfUser(int id) throws SQLException, DataBaseConnectionException {
        List<Integer> childIDs = new ArrayList<>();

        List<Parameter> params = new ArrayList<>();

        params.add(new IntegerParameter("parentId", id));

        List<ParentAndChild> parentAndChildList = getByParameters(params);

        for(ParentAndChild parentAndChild : parentAndChildList){
            int idOfChild = parentAndChild.getChildId();
            childIDs.add(idOfChild);
        }

        return childIDs;
    }

    public List<Integer> getParentsIDsOfUser(int id) throws SQLException, DataBaseConnectionException {
        List<Integer> parentsIDs = new ArrayList<>();

        List<Parameter> params = new ArrayList<>();

        params.add(new IntegerParameter("childId", id));

        List<ParentAndChild> parentAndChildList = getByParameters(params);

        for(ParentAndChild parentAndChild : parentAndChildList){
            int idOfParent = parentAndChild.getParentId();
            parentsIDs.add(idOfParent);
        }

        return parentsIDs;
    }
}
