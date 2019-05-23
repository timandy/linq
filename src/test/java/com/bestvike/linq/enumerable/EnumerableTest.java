package com.bestvike.linq.enumerable;

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
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
            ThrowHelper.throwArgumentNullException(ExceptionArgument.clazz);
        return clazz.isInstance(value) ? (T) value : null;
    }

    static Boolean IsEven(int num) {
        return num % 2 == 0;
    }

    static Boolean IsNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }

    static String stringJoin(Iterable<String> group) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = group.iterator();
        if (iterator.hasNext()) {
            builder.append(iterator.next());
            while (iterator.hasNext())
                builder.append("+").append(iterator.next());
        }
        return builder.toString();
    }

    static <T> void assertEquals(IEnumerable<T> expected, IEnumerable<T> actual) {
        assertEquals(expected, actual, null);
    }

    static <T> void assertEquals(IEnumerable<T> expected, IEnumerable<T> actual, IEqualityComparer<T> comparer) {
        if (expected == actual)
            return;
        if (comparer == null)
            comparer = new AssertEqualityComparer<>();
        if (expected != null && expected.sequenceEqual(actual, comparer))
            return;
        fail(format(null, expected, actual));
    }

    static <T> void assertEmpty(IEnumerable<T> enumerable) {
        if (enumerable == null)
            fail("enumerable is null");
        if (enumerable.count() == 0)
            return;
        fail("enumerable is not empty");
    }

    static <T> void assertNotEmpty(IEnumerable<T> enumerable) {
        if (enumerable == null)
            fail("enumerable is null");
        if (enumerable.count() != 0)
            return;
        fail("enumerable is empty");
    }

    static void assertIsAssignableFrom(Class<?> expectedType, Object obj) {
        if (expectedType == null)
            fail("expectedType is null");
        if (obj != null && expectedType.isAssignableFrom(obj.getClass()))
            return;
        fail("expectedType " + expectedType.getName() + ", but got " + (obj == null ? "null" : obj.getClass().getName()));
    }

    public static <T> void assertSubset(java.util.Set<T> expectedSuperset, java.util.Set<T> actual) {
        Assert.assertNotNull("expectedSuperset ", expectedSuperset);

        if (actual == null || !expectedSuperset.containsAll(actual))
            fail("expectedSuperset not containsAll actual");
    }

    public static <T> void assertSuperset(java.util.Set<T> expectedSubset, java.util.Set<T> actual) {
        Assert.assertNotNull("expectedSubset ", expectedSubset);

        if (actual == null || !actual.containsAll(expectedSubset))
            fail("actual not containsAll expectedSubset");
    }

    static void assertThrows(Class<?> clazz, Action0 action) {
        if (clazz == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.clazz);
        if (action == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.action);

        try {
            action.apply();
            Assert.fail("should throw " + clazz.toString());
        } catch (Exception e) {
            if (clazz.isInstance(e))
                return;
            Assert.fail("should throw " + clazz.toString() + ", but throw " + e.getClass());
        }
    }

    static void assertThrows(Class<?> clazz, Func0<Object> func) {
        if (clazz == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.clazz);
        if (func == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.action);

        try {
            func.apply();
            Assert.fail("should throw " + clazz.toString());
        } catch (Exception e) {
            if (clazz.isInstance(e))
                return;
            Assert.fail("should throw " + clazz.toString() + ", but throw " + e.getClass());
        }
    }

    static IEnumerable<Integer> RepeatedNumberGuaranteedNotCollectionType(int num, int count) {
        return Linq.repeat(num, count);
    }

    static IEnumerable<Integer> NumberRangeGuaranteedNotCollectionType(int num, int count) {
        return Linq.range(num, count);
    }

    static IEnumerable<Integer> NullableNumberRangeGuaranteedNotCollectionType(int num, int count) {
        return Linq.range(num, count);
    }

    static IEnumerable<Integer> RepeatedNullableNumberGuaranteedNotCollectionType(Integer num, int count) {
        return Linq.repeat(num, count);
    }

    static <T> List<Func1<IEnumerable<T>, IEnumerable<T>>> IdentityTransforms() {
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

    static <T> IEnumerable<T> ForceNotCollection(IEnumerable<T> source) {
        return source.select(a -> a);
    }

    private static void fail(String message) {
        if (message == null)
            throw new AssertionError();
        throw new AssertionError(message);
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


    protected static class DelegateBasedCollection<T> implements ICollection<T> {
        Func0<Integer> CountWorker;
        Func0<Boolean> IsReadOnlyWorker;
        Action1<T> AddWorker;
        Action0 ClearWorker;
        Func1<T, Boolean> ContainsWorker;
        Func1<T, Boolean> RemoveWorker;
        Action2<Object[], Integer> CopyToWorker;
        Func0<IEnumerator<T>> GetEnumeratorWorker;

        DelegateBasedCollection() {
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


    protected static class AnagramEqualityComparer implements IEqualityComparer<String> {
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
