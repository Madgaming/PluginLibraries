package net.zetaeta.bukkit.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getField(Object container, String fieldName) throws Exception {
        Class<?> containerClass = container.getClass();
        Field f = containerClass.getField(fieldName);
        T t = (T) f.get(container);
        return t;
    }
    
    public static Method getMethod(Object container, String methodName) {
        Class<?> containerClass = container.getClass();
        for (Method poss : containerClass.getDeclaredMethods()) {
            if (poss.getName().equals(methodName)) {
                return poss;
            }
        }
        return null;
    }
}
