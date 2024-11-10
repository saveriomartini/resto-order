package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.RestoObject;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractDataMapper {

    protected static Map<Long, RestoObject> cache = new HashMap<>();


    //Create
    protected abstract String insertStatement();

    protected abstract void doInsert(RestoObject restoObject, PreparedStatement stmt) throws SQLException;

    public Long insert(RestoObject restoObject) {
        PreparedStatement insertStatement = null;
        try {
            // Prepare the statement using the insert SQL statement
            insertStatement = DbUtils.getConnection().prepareStatement(insertStatement(), PreparedStatement.RETURN_GENERATED_KEYS);

            // Populate the statement with the object's data
            doInsert(restoObject, insertStatement);

            // Execute the statement
            insertStatement.executeUpdate();

            // Retrieve generated keys
            ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long id = generatedKeys.getLong(1);
                restoObject.setId(id);
                cache.put(id, restoObject);
                return id;
            } else {
                throw new SQLException("Creating object failed, no ID obtained.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.cleanUp(insertStatement);
        }
        return null;
    }


    protected RestoObject load(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        RestoObject result = doLoad(id, rs);
        cache.put(id, result);
        return result;
    }


    protected abstract String findByIdStatement(Long id);

    protected abstract RestoObject doLoad(Long id, ResultSet rs) throws SQLException;


    //Read
    protected abstract String findStatement();

    protected abstract String findAllStatement();

    protected RestoObject abstractFind(Long id) {
        RestoObject result = cache.get(id);
        if (result != null) {
            return result;
        }
        PreparedStatement findStatement = null;
        try {
            findStatement = DbUtils.getConnection().prepareStatement(findStatement());
            findStatement.setLong(1, id);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                result = load(rs);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.cleanUp(findStatement);
        }
        return null;
    }

    protected Set<RestoObject> abstractFindAll() {
        Set<RestoObject> result = new HashSet<>();
        PreparedStatement findAllStatement = null;
        try {
            findAllStatement = DbUtils.getConnection().prepareStatement(findAllStatement());
            ResultSet rs = findAllStatement.executeQuery();
            while (rs.next()) {
                result.add(load(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected Set<RestoObject> abstractFindAllById(Long id) {
        Set<RestoObject> result = new HashSet<>();
        PreparedStatement findAllStatement = null;
        try {
            findAllStatement = DbUtils.getConnection().prepareStatement(findByIdStatement(id));
            findAllStatement.setLong(1, id);
            ResultSet rs = findAllStatement.executeQuery();
            while (rs.next()) {
                result.add(load(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.cleanUp(findAllStatement);
        }
        return result;
    }

    //Update
    protected abstract String updateStatement();

    protected abstract void doUpdate(RestoObject restoObject, PreparedStatement stmt) throws SQLException;

    public void update(RestoObject restoObject) {
        PreparedStatement updateStatement = null;
        try {
            doUpdate(restoObject, updateStatement);
            updateStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Delete
    protected abstract String deleteStatement();

    protected abstract void doDelete(RestoObject restoObject, PreparedStatement stmt) throws SQLException;

    public void delete(RestoObject restoObject) {
        PreparedStatement deleteStatement = null;
        try {
            doDelete(restoObject, deleteStatement);
            deleteStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
