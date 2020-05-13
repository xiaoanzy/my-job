package com.jhxaa.util;


import java.util.Collection;
import java.util.Map;

public class EmptyUtil {


    public static boolean isEmptyString(String str) {
        if (str == null || "".equals(str) || str.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmptyString(String str) {
        return !isEmptyString(str);
    }

    public static boolean isListEmpty(Collection collection) {
        return collection == null || collection.isEmpty() || collection.size() == 0;
        // return false;
    }

    public static boolean isEmptyInteger(Integer ints) {
        if (null == ints || 0 == ints || ints.intValue() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmptyInteger(Integer ints) {
        return !isEmptyInteger(ints);
    }

    //判断是否为空
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        if (obj instanceof Collection) return ((Collection) obj).isEmpty();
        if (obj instanceof Map) return ((Map) obj).isEmpty();
        if (obj instanceof Integer) return (Integer) obj == 0;
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
    //判断是否不为空

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    private boolean validPropertyEmpty(Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (EmptyUtil.isEmpty(args[i])) {
                return true;
            }
        }
        return false;
    }
}