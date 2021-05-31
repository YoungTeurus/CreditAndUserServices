package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBase;
import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import com.github.youngteurus.servletdatabase.database.constructor.Parameter;
import com.github.youngteurus.servletdatabase.modelconnectors.BaseDatabaseConnector;
import database.UserPostgresDataBase;
import models.User;
import models.out.UserAndRelatives;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// public class ParentsDatabaseConnector extends BaseDatabaseConnector<Integer> {
//     private static ParentsDatabaseConnector instance;
//
//     private ParentsDatabaseConnector(DataBase db){
//         super(db);
//     }
//
//     public static ParentsDatabaseConnector getInstance() {
//         if (instance == null) {
//             DataBase db = UserPostgresDataBase.getInstance();
//             instance = new ParentsDatabaseConnector(db);
//         }
//         return instance;
//     }
//
//     @Override
//     protected String getTableName() {
//         return "parents";
//     }
//
//     @Override
//     protected Integer constructObjectFromResultSet(ResultSet resultSet) {
//         return null;
//     }
//
//     @Override
//     protected List<Parameter> getParametersForInsert(Integer userAndRelatives) {
//         return null;
//     }
//
//     public List<User> getParentsById(long id){
//         List<User> parents = getByParameters();
//
//         return parents;
//     }
//
//     @Override
//     protected ResultSet getResultSetOfRemovedObjectId(long l) throws SQLException, DataBaseConnectionException {
//         throw new NotImplementedException();
//     }
// }
//