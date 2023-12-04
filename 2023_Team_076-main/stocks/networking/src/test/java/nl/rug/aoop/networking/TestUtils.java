package nl.rug.aoop.networking;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestUtils {
    public static void setPrivateField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static boolean checkIfMethodExists(String name, Method[] methods) {
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}