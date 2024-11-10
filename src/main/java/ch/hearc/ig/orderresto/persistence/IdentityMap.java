package ch.hearc.ig.orderresto.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IdentityMap<T> {
    Map<Long, T> map = new HashMap<>();

    public T get (Long id) {
        return map.get(id);
    }

    public void put (Long id, T object) {
        map.put(id, object);
    }

    public boolean contains (Long id) {
        return map.containsKey(id);
    }

    public Collection<T> values() {
        return map.values();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, T> entry : map.entrySet()) {
            sb.append("ID: ").append(entry.getKey()).append(", Value: ").append(entry.getValue().toString()).append("\n");
        }
        return sb.toString();
    }
}