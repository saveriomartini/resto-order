package ch.hearc.ig.orderresto.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IdentityMap<T>{
    private Map<Long, T> map = new HashMap<>();

    public T get (Long id) {
        return map.get(id);
    }

    public void put (Long id, T object) {
        map.put(id, object);
    }

    public boolean containsKey(Long id) {
        return map.containsKey(id);
    }

    public Collection<T> values() {
        return map.values();
    }
}
