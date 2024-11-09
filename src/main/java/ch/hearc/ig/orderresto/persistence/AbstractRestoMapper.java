package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.RestoObject;

import java.util.HashMap;
import java.util.Map;

public class AbstractRestoMapper implements RestoMapper {
    protected static Map<Long, RestoObject> cache = new HashMap<>();


    public void insert(Object o) {
        // TODO
    }

    public void update(Object o) {
        // TODO
    }

    public void delete(Object o) {
        // TODO
    }

    public RestoObject findById(Long id) {
        // TODO
        return null;
    }
}
