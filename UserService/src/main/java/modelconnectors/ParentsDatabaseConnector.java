package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.database.constructor.LongParameter;
import com.github.youngteurus.servletdatabase.database.constructor.Parameter;
import com.github.youngteurus.servletdatabase.modelconnectors.BaseDatabaseConnector;
import database.UserPostgresDataBase;
import models.out.ParentAndChild;

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

        params.add(new LongParameter("parentId", parentAndChild.getParentId()));
        params.add(new LongParameter("childId", parentAndChild.getChildId()));

        return params;
    }

    @Override
    protected List<Parameter> getParametersForRemove(ParentAndChild parentAndChild) {
        return getParametersForInsert(parentAndChild);
    }

    public List<Long> getChildrenIDsOfUser(long id) throws SQLException, DataBaseConnectionException {
        List<Long> childIDs = new ArrayList<>();

        List<Parameter> params = new ArrayList<>();

        params.add(new LongParameter("parentId", id));

        List<ParentAndChild> parentAndChildList = getByParameters(params);

        for(ParentAndChild parentAndChild : parentAndChildList){
            long idOfChild = parentAndChild.getChildId();
            childIDs.add(idOfChild);
        }

        return childIDs;
    }

    public List<Long> getParentsIDsOfUser(long id) throws SQLException, DataBaseConnectionException {
        List<Long> parentsIDs = new ArrayList<>();

        List<Parameter> params = new ArrayList<>();

        params.add(new LongParameter("childId", id));

        List<ParentAndChild> parentAndChildList = getByParameters(params);

        for(ParentAndChild parentAndChild : parentAndChildList){
            long idOfParent = parentAndChild.getParentId();
            parentsIDs.add(idOfParent);
        }

        return parentsIDs;
    }
}
