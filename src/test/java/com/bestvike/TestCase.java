package com.bestvike;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Action0;
import com.bestvike.function.Action1;
import com.bestvike.function.Action2;
import com.bestvike.function.Func0;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.AssertEqualityComparer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class TestCase {
    protected static final BigDecimal MAX_DECIMAL = new BigDecimal("999999999999999999999999999999");
    protected static final BigDecimal MIN_DECIMAL = new BigDecimal("-999999999999999999999999999999");
    protected static final Date MAX_DATE = newDate(9999, 12, 31);
    protected static final Date MIN_DATE = newDate(1, 1, 1);
    protected static final String Empty = "";
    protected static final Employee[] badEmps = {
            new Employee(140, "Cedric", 40),
            new Employee(150, "Gates", null)};
    protected static final Department[] badDepts = {
            new Department("Manager", null, Collections.emptyList())};
    protected static final Employee[] emps = {
            new Employee(100, "Fred", 10),
            new Employee(110, "Bill", 30),
            new Employee(120, "Eric", 10),
            new Employee(130, "Janet", 10)};
    protected static final Department[] depts = {
            new Department("Sales", 10, Arrays.asList(emps[0], emps[2], emps[3])),
            new Department("HR", 20, Collections.emptyList()),
            new Department("Marketing", 30, Collections.singletonList(emps[1]))};

    protected static Date newDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(year, month - 1, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    protected static BigDecimal m(long value) {
        return new BigDecimal(value);
    }

    protected static BigDecimal m(String value) {
        return new BigDecimal(value);
    }

    protected static <T> T as(Object value, Class<T> clazz) {
        if (clazz == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.clazz);
        return clazz.isInstance(value) ? (T) value : null;
    }

    protected static Boolean IsEven(int num) {
        return num % 2 == 0;
    }

    protected static Boolean IsNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }

    protected static String stringJoin(Iterable<String> group) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = group.iterator();
        if (iterator.hasNext()) {
            builder.append(iterator.next());
            while (iterator.hasNext())
                builder.append("+").append(iterator.next());
        }
        return builder.toString();
    }

    protected static void assertSame(Object expected, Object actual) {
        if (actual == expected)
            return;
        fail("expected same");
    }

    protected static void assertNotSame(Object expected, Object actual) {
        if (actual != expected)
            return;
        fail("expected not same");
    }

    protected static void assertEquals(Object expected, Object actual) {
        if (equal(expected, actual))
            return;
        fail(String.format("should be %s, but %s", expected, actual));
    }

    protected static <T> void assertEquals(IEnumerable<T> expected, IEnumerable<T> actual, IEqualityComparer<T> comparer) {
        if (sequenceEqual(expected, actual, comparer))
            return;
        fail(format(null, expected, actual));
    }

    protected static void assertNotEquals(Object expected, Object actual) {
        if (equal(expected, actual))
            fail("should not be equals");
    }

    protected static <T> void assertEmpty(IEnumerable<T> enumerable) {
        if (enumerable == null)
            fail("enumerable is null");
        if (enumerable.count() == 0)
            return;
        fail("enumerable is not empty");
    }

    protected static <T> void assertNotEmpty(IEnumerable<T> enumerable) {
        if (enumerable == null)
            fail("enumerable is null");
        if (enumerable.count() != 0)
            return;
        fail("enumerable is empty");
    }

    protected static void assertIsAssignableFrom(Class<?> expectedType, Object obj) {
        if (expectedType == null)
            fail("expectedType is null");
        if (obj != null && expectedType.isAssignableFrom(obj.getClass()))
            return;
        fail("expectedType " + expectedType.getName() + ", but got " + (obj == null ? "null" : obj.getClass().getName()));
    }

    protected static <T> void assertSubset(java.util.Set<T> expectedSuperset, java.util.Set<T> actual) {
        assertNotNull(expectedSuperset);

        if (actual == null || !expectedSuperset.containsAll(actual))
            fail("expectedSuperset not containsAll actual");
    }

    protected static <T> void assertSuperset(java.util.Set<T> expectedSubset, java.util.Set<T> actual) {
        assertNotNull(expectedSubset);

        if (actual == null || !actual.containsAll(expectedSubset))
            fail("actual not containsAll expectedSubset");
    }

    protected static void assertThrows(Class<?> clazz, Action0 action) {
        if (clazz == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.clazz);
        if (action == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.action);

        try {
            action.apply();
            fail("should throw " + clazz.toString());
        } catch (Exception e) {
            if (clazz.isInstance(e))
                return;
            fail("should throw " + clazz.toString() + ", but throw " + e.getClass());
        }
    }

    protected static void assertThrows(Class<?> clazz, Func0<Object> func) {
        if (clazz == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.clazz);
        if (func == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.action);

        try {
            func.apply();
            fail("should throw " + clazz.toString());
        } catch (Exception e) {
            if (clazz.isInstance(e))
                return;
            fail("should throw " + clazz.toString() + ", but throw " + e.getClass());
        }
    }

    protected static void assertNull(Object obj) {
        if (obj == null)
            return;
        fail(String.format("expect null, but was: <%s>", obj));
    }

    protected static void assertNotNull(Object obj) {
        if (obj != null)
            return;
        fail("expect not null, but was: <null>");
    }

    protected static void assertTrue(boolean obj) {
        if (obj)
            return;

        fail("expect true, but was: false");
    }

    protected static void assertFalse(boolean obj) {
        if (!obj)
            return;

        fail("expect false, but was: true");
    }

    protected static void fail(String message) {
        if (message == null)
            throw new AssertionError();
        throw new AssertionError(message);
    }

    protected static IEnumerable<Integer> RepeatedNumberGuaranteedNotCollectionType(int num, int count) {
        return Linq.repeat(num, count);
    }

    protected static IEnumerable<Integer> NumberRangeGuaranteedNotCollectionType(int num, int count) {
        return Linq.range(num, count);
    }

    protected static IEnumerable<Integer> NullableNumberRangeGuaranteedNotCollectionType(int num, int count) {
        return Linq.range(num, count);
    }

    protected static IEnumerable<Integer> RepeatedNullableNumberGuaranteedNotCollectionType(Integer num, int count) {
        return Linq.repeat(num, count);
    }

    protected static <T> List<Func1<IEnumerable<T>, IEnumerable<T>>> IdentityTransforms() {
        List<Func1<IEnumerable<T>, IEnumerable<T>>> list = new ArrayList<>();
        list.add(e -> e);
        list.add(e -> e.toArray());
        list.add(e -> Linq.asEnumerable(e.toList()));
        list.add(e -> e.select(i -> i));
        list.add(e -> e.concat(Linq.empty()));
        list.add(e -> ForceNotCollection(e));
        list.add(e -> e.concat(ForceNotCollection(Linq.empty())));
        list.add(e -> e.where(i -> true));
        list.add(e -> ForceNotCollection(e).skip(0));
        return list;
    }

    protected static <T> IEnumerable<T> ForceNotCollection(IEnumerable<T> source) {
        return source.select(a -> a);
    }

    private static boolean equal(Object expected, Object actual) {
        if (expected == actual)
            return true;
        if (expected == null || actual == null)
            return false;
        if (expected.equals(actual))
            return true;
        if (expected instanceof IEnumerable && actual instanceof IEnumerable)
            return sequenceEqual((IEnumerable) expected, (IEnumerable) actual, null);
        if (expected instanceof Number && actual instanceof Number) {
            if (expected instanceof Byte || expected instanceof Short || expected instanceof Integer || expected instanceof Long
                    || actual instanceof Byte || actual instanceof Short || actual instanceof Integer || actual instanceof Long)
                return ((Number) expected).longValue() == ((Number) actual).longValue();
            else if (expected instanceof Float || expected instanceof Double
                    || actual instanceof Float || actual instanceof Double)
                return ((Number) expected).doubleValue() == ((Number) actual).doubleValue();
        }
        return (expected instanceof Comparable && ((Comparable) expected).compareTo(actual) == 0)
                || (actual instanceof Comparable && ((Comparable) actual).compareTo(expected) == 0);
    }

    private static <T> boolean sequenceEqual(IEnumerable<T> expected, IEnumerable<T> actual, IEqualityComparer<T> comparer) {
        if (expected == actual)
            return true;
        if (comparer == null)
            comparer = new AssertEqualityComparer<>();
        return expected != null && expected.sequenceEqual(actual, comparer);
    }

    private static <T> String format(String message, IEnumerable<T> expected, IEnumerable<T> actual) {
        String formatted = "";
        if (message != null && !message.equals(""))
            formatted = message + " ";
        String expectedString = expected == null ? "null" : expected.toList().toString();
        String actualString = actual == null ? "null" : actual.toList().toString();
        if (expectedString.equals(actualString))
            return formatted + "expected: " + formatClassAndValue(expected, expectedString) + " but was: " + formatClassAndValue(actual, actualString);
        else
            return formatted + "expected:<" + expectedString + "> but was:<" + actualString + ">";
    }

    private static String formatClassAndValue(Object value, String valueString) {
        String className = value == null ? "null" : value.getClass().getName();
        return className + "<" + valueString + ">";
    }


    public static class DelegateBasedCollection<T> implements ICollection<T> {
        protected Func0<Integer> CountWorker;
        protected Func0<Boolean> IsReadOnlyWorker;
        protected Action1<T> AddWorker;
        protected Action0 ClearWorker;
        protected Func1<T, Boolean> ContainsWorker;
        protected Func1<T, Boolean> RemoveWorker;
        protected Action2<Object[], Integer> CopyToWorker;
        protected Func0<IEnumerator<T>> GetEnumeratorWorker;

        protected DelegateBasedCollection() {
            this.CountWorker = () -> 0;
            this.IsReadOnlyWorker = () -> false;
            this.AddWorker = item -> {
            };
            this.ClearWorker = () -> {
            };
            this.ContainsWorker = item -> false;
            this.RemoveWorker = item -> false;
            this.CopyToWorker = (array, arrayIndex) -> {
            };
            this.GetEnumeratorWorker = () -> Linq.<T>empty().enumerator();
        }

        @Override
        public Collection<T> getCollection() {
            ThrowHelper.throwNotSupportedException();
            return null;
        }

        @Override
        public int _getCount() {
            return this.CountWorker.apply();
        }

        @Override
        public boolean _contains(T item) {
            return this.ContainsWorker.apply(item);
        }

        @Override
        public void _copyTo(Object[] array, int arrayIndex) {
            this.CopyToWorker.apply(array, arrayIndex);
        }

        @Override
        public T[] _toArray(Class<T> clazz) {
            ThrowHelper.throwNotSupportedException();
            return null;
        }

        @Override
        public Object[] _toArray() {
            ThrowHelper.throwNotSupportedException();
            return null;
        }

        @Override
        public List<T> _toList() {
            ThrowHelper.throwNotSupportedException();
            return null;
        }

        @Override
        public IEnumerator<T> enumerator() {
            return this.GetEnumeratorWorker.apply();
        }
    }


    public static class AnagramEqualityComparer implements IEqualityComparer<String> {
        @Override
        public boolean equals(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return true;
            if (x == null | y == null)
                return false;
            if (x.length() != y.length())
                return false;
            try (IEnumerator<Character> en = Linq.asEnumerable(x).orderBy(i -> i).enumerator()) {
                for (char c : Linq.asEnumerable(y).orderBy(i -> i)) {
                    en.moveNext();
                    if (c != en.current())
                        return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode(String obj) {
            if (obj == null)
                return 0;
            int hash = obj.length();
            for (char c : Linq.asEnumerable(obj))
                hash ^= c;
            return hash;
        }
    }
}
