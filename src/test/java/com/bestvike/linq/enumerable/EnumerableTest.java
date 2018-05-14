package com.bestvike.linq.enumerable;

import com.bestvike.function.Action0;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.Errors;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class EnumerableTest {
    static final double DELTA = 0d;
    static final String Empty = "";
    static final Employee[] badEmps = {
            new Employee(140, "Cedric", 40),
            new Employee(150, "Gates", null)};
    static final Department[] badDepts = {
            new Department("Manager", null, Collections.emptyList())};
    static final Employee[] emps = {
            new Employee(100, "Fred", 10),
            new Employee(110, "Bill", 30),
            new Employee(120, "Eric", 10),
            new Employee(130, "Janet", 10)};
    static final Department[] depts = {
            new Department("Sales", 10, Arrays.asList(emps[0], emps[2], emps[3])),
            new Department("HR", 20, Collections.emptyList()),
            new Department("Marketing", 30, Collections.singletonList(emps[1]))};

    static <T> T as(Object value, Class<T> clazz) {
        if (clazz == null)
            throw Errors.argumentNull("clazz");
        return clazz.isInstance(value) ? (T) value : null;
    }

    static Boolean IsEven(int num) {
        return num % 2 == 0;
    }

    static Boolean IsNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }

    static String stringJoin(Iterable<String> group) {
        final StringBuilder builder = new StringBuilder();
        final Iterator<String> iterator = group.iterator();
        if (iterator.hasNext()) {
            builder.append(iterator.next());
            while (iterator.hasNext()) {
                builder.append("+").append(iterator.next());
            }
        }
        return builder.toString();
    }

    static <T> void assertEquals(IEnumerable<T> expected, IEnumerable<T> actual) {
        if (expected == actual)
            return;
        if (expected != null && expected.sequenceEqual(actual))
            return;
        fail(format(null, expected, actual));
    }

    static void assertThrows(Class<?> clazz, Action0 action) {
        if (clazz == null)
            throw Errors.argumentNull("clazz");
        if (action == null)
            throw Errors.argumentNull("action");

        try {
            action.apply();
            Assert.fail("should throw " + clazz.toString());
        } catch (Exception e) {
            if (clazz.isInstance(e))
                return;
            Assert.fail("should throw " + clazz.toString() + ", but throw " + e.getClass());
        }
    }

    static IEnumerable<Integer> NumberRangeGuaranteedNotCollectionType(int num, int count) {
        return Linq.range(num, count);
    }

    private static void fail(String message) {
        if (message == null) {
            throw new AssertionError();
        }
        throw new AssertionError(message);
    }

    private static <T> String format(String message, IEnumerable<T> expected, IEnumerable<T> actual) {
        String formatted = "";
        if (message != null && !message.equals("")) {
            formatted = message + " ";
        }
        String expectedString = expected == null ? "null" : expected.toList().toString();
        String actualString = actual == null ? "null" : actual.toList().toString();
        if (expectedString.equals(actualString)) {
            return formatted + "expected: "
                    + formatClassAndValue(expected, expectedString)
                    + " but was: " + formatClassAndValue(actual, actualString);
        } else {
            return formatted + "expected:<" + expectedString + "> but was:<"
                    + actualString + ">";
        }
    }

    private static String formatClassAndValue(Object value, String valueString) {
        String className = value == null ? "null" : value.getClass().getName();
        return className + "<" + valueString + ">";
    }
}
