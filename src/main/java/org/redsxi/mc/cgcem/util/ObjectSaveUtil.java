package org.redsxi.mc.cgcem.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ObjectSaveUtil {
    private static Map<Long, Object> objectMap = new HashMap<>();
    private static final Random r = new Random(System.currentTimeMillis());

    public static long storeObject(Object o) {
        long index = r.nextLong();
        if(objectMap.containsKey(index)) throw new IllegalStateException("Existing key " + index);
        objectMap.put(index, o);
        return index;
    }

    public static Object getObject(long index) {
        if(!objectMap.containsKey(index)) throw new IllegalStateException("No key " + index);
        Object r = objectMap.get(index);
        if(!objectMap.remove(index, r)) throw new RuntimeException("Deleted entry in map,but it still exists");
        return objectMap.get(index);
    }
}
