package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.RestoObject;
import ch.hearc.ig.orderresto.service.DbUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDataMapper {

    protected static Map<Long, RestoObject> cache = new HashMap<>();


    //Create
    protected abstract String insertStatement();
    protected abstract void doInsert(RestoObject restoObject, PreparedStatement stmt) throws SQLException;

    public Long insert(RestoObject restoObject) {
        PreparedStatement insertStatement = null;
        try {
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
        }
        return null;
    }





    //Read
    abstract protected String findStatement();

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



    protected RestoObject load(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        RestoObject result = doLoad(id, rs);
        cache.put(id, result);
        return result;
    }

    abstract protected RestoObject doLoad(Long id, ResultSet rs) throws SQLException;

    //Update
    public void update(RestoObject restoObject) {
        // TODO
    }

    //Delete
    public void delete(RestoObject restoObject) {
        // TODO
    }



}


