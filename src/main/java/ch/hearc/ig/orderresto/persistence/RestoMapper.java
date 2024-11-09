package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.RestoObject;

public interface RestoMapper {
    void insert(Object o);
    void update(Object o);
    void delete(Object o);
    RestoObject findById(Long id);
}