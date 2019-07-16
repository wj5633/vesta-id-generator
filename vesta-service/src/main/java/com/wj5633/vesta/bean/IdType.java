package com.wj5633.vesta.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created at 2019/7/16 14:11.
 *
 * @author wangjie
 * @version 1.0.0
 */

public enum IdType {
    MAX_PEAK(0),
    MIN_GRANULARITY(1);

    private long value;

    IdType(long value) {
        this.value = value;
    }

    public long value() {
        return this.value;
    }

    public static IdType parse(long value) {
        for (IdType idType : values()) {
            if (idType.value() == value) {
                return idType;
            }
        }
        return null;
    }

    private static Map<String, IdType> nameMap = new HashMap<>();

    static {
        for (IdType idType : IdType.values()) {
            nameMap.put(idType.name(), idType);
        }
    }

    public static IdType parse(String name) {
        return nameMap.get(name);
    }
}
