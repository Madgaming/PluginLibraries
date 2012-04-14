package net.zetaeta.libraries.util;

import java.lang.reflect.Field;

public class ReflectionUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getField(Object container, String fieldName) throws Exception {
        Class<?> containerClass = container.getClass();
        Field f = containerClass.getField(fieldName);
        T t = (T) f.get(container);
        return t;
    }
}
