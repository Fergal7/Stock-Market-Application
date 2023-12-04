package nl.rug.aoop;

import java.lang.reflect.Method;

public class TestUtils {
    public static boolean checkIfMethodExists(String name, Method[] methods) {
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
