package modelconnectors;

import com.github.youngteurus.servletdatabase.database.DataBaseConnectionException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParentsDatabaseConnectorTest {

    private final ParentsDatabaseConnector pdc = ParentsDatabaseConnector.getInstance();

    @Test
    void getChildrenIDsOfUser() throws SQLException, DataBaseConnectionException {
        List<Integer> idOfChildren = pdc.getChildrenIDsOfUser(1);

        assertNotEquals(0 , idOfChildren.size());
        System.out.println(idOfChildren);
    }

    @Test
    void getParentsIDsOfUser() throws SQLException, DataBaseConnectionException {
        List<Integer> idOfParents = pdc.getParentsIDsOfUser(2);

        assertNotEquals(0 , idOfParents.size());
        System.out.println(idOfParents);
    }

    @Test
    void compareChildrenAndParentsTest() throws SQLException, DataBaseConnectionException {
        int testParentId = 1;

        List<Integer> idOfChildren = pdc.getChildrenIDsOfUser(testParentId);
        System.out.println("Children of user {id=" + testParentId + "}:\n" + idOfChildren);

        if (!idOfChildren.isEmpty()){
            int firstChildId = idOfChildren.get(0);
            List<Integer> idOfParents = pdc.getParentsIDsOfUser(firstChildId);
            System.out.println("Parents of child {id=" + firstChildId + "}:\n" + idOfParents + "\nShould contain '" + testParentId + "'!");
            assertTrue(idOfParents.contains(testParentId));
        }
    }
}