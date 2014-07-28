package testhelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;

public class TestHelper {
    private TestHelper() {

    }

    public static int countTestCases(final Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        int count = 0;
        for (final Method m : methods) {
            final Annotation[] annotations = m.getDeclaredAnnotations();
            for (final Annotation a : annotations) {
                if (a.annotationType() == Test.class) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    public static void gcAndSleep(final int millis) {
        System.gc();
        try {
            Thread.sleep(millis);
        }
        catch (final InterruptedException e) {
        }
    }
}
