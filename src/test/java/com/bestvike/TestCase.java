package com.bestvike;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.Comparer;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.function.Action0;
import com.bestvike.function.Action1;
import com.bestvike.function.Action2;
import com.bestvike.function.Func0;
import com.bestvike.function.Func1;
import com.bestvike.function.Predicate0;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.adapter.enumerator.ArrayEnumerator;
import com.bestvike.linq.adapter.enumerator.GenericArrayEnumerator;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.enumerable.AbstractEnumerator;
import com.bestvike.linq.enumerable.Values;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.AssertEqualityComparer;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.TimeZone;
import java.util.function.Consumer;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class TestCase {
    protected static final BigDecimal MAX_DECIMAL = new BigDecimal("999999999999999999999999999999");
    protected static final BigDecimal MIN_DECIMAL = new BigDecimal("-999999999999999999999999999999");
    protected static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");
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
        calendar.setTimeZone(TIME_ZONE);
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

    protected static String join(Iterable<String> group) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = group.iterator();
        if (iterator.hasNext()) {
            builder.append(iterator.next());
            while (iterator.hasNext())
                builder.append("+").append(iterator.next());
        }
        return builder.toString();
    }

    protected static String[] split(String value, char[] separator, boolean removeEmptyEntries) {
        if (value == null)
            throw new NullPointerException("value is null");
        if (separator == null)
            throw new NullPointerException("separator is null");

        List<String> list = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        IEnumerable<Character> separators = Linq.of(separator);
        if (removeEmptyEntries) {
            for (char c : Linq.chars(value)) {
                if (separators.contains(c)) {
                    if (temp.length() > 0) {
                        list.add(temp.toString());
                        temp.setLength(0);
                    }
                } else {
                    temp.append(c);
                }
            }
            //最后一个冲刷
            if (temp.length() > 0) {
                list.add(temp.toString());
                temp.setLength(0);
            }
        } else {
            for (char c : Linq.chars(value)) {
                if (separators.contains(c)) {
                    list.add(temp.toString());
                    temp.setLength(0);
                } else {
                    temp.append(c);
                }
            }
            //最后一个冲刷
            list.add(temp.toString());
            temp.setLength(0);
        }
        return list.toArray(new String[0]);
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
        fail(String.format("should be %s, but %s", Values.toString(expected), Values.toString(actual)));
    }

    protected static void assertEquals(String expected, String actual, IEqualityComparer<String> comparer) {
        if (comparer == null)
            comparer = StringComparer.Ordinal;
        if (comparer.equals(expected, actual))
            return;
        fail(String.format("should be %s, but %s", expected, actual));
    }

    protected static <T> void assertEquals(IEnumerable<T> expected, IEnumerable<T> actual, IEqualityComparer<T> comparer) {
        if (sequenceEqual(expected, actual, comparer))
            return;
        fail(String.format("should be %s, but %s with comparer %s", Values.toString(expected), Values.toString(actual), comparer));
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

    protected static <T> void assertInRange(T actual, T low, T high) {
        Comparator<T> comparer = Comparer.Default();
        if (comparer.compare(low, actual) > 0 || comparer.compare(actual, high) > 0)
            fail(String.format("%s should in range [%s, %s]", actual, low, high));
    }

    protected static <T> void assertIsAssignableFrom(Class<T> expectedType, Object obj) {
        if (expectedType == null)
            fail("expectedType is null");
        if (obj != null && expectedType.isAssignableFrom(obj.getClass()))
            return;
        fail("expectedType " + expectedType.getName() + ", but got " + (obj == null ? "null" : obj.getClass().getName()));
    }

    protected static <T> void assertIsType(Class<T> expectedType, Object obj) {
        if (expectedType == null)
            fail("expectedType is null");
        if (obj != null && expectedType == obj.getClass())
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
        } catch (Throwable e) {
            if (e.getClass() == clazz)
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
        } catch (Throwable e) {
            if (e.getClass() == clazz)
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

    protected static <T> void assertAll(IEnumerable<T> collection, Action1<T> action) {
        assertNotNull(collection);
        assertNotNull(action);
        Stack<Tuple3<Integer, Object, Exception>> stack = new Stack<>();
        List<T> array = collection.toList();
        for (int i = 0, len = array.size(); i < len; i++) {
            try {
                action.apply(array.get(i));
            } catch (Exception item) {
                stack.push(Tuple.create(i, array.get(i), item));
            }
        }
        if (stack.size() <= 0) {
            return;
        }
        throw new AssertionError(array.size() + "  " + Arrays.toString(stack.toArray()));
    }

    protected static void fail(String message) {
        if (message == null)
            throw new AssertionError();
        throw new AssertionError(message);
    }

    protected static boolean IsEven(int num) {
        return num % 2 == 0;
    }

    protected static boolean IsNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }

    protected static IEnumerable<Integer> RepeatedNumberGuaranteedNotCollectionType(int num, int count) {
        return () -> new AbstractEnumerator<Integer>() {
            @Override
            public boolean moveNext() {
                if (this.state < 0)
                    return false;
                if (this.state < count) {
                    this.current = num;
                    this.state++;
                    return true;
                }
                this.close();
                return false;
            }
        };
    }

    protected static IEnumerable<Integer> RepeatedNullableNumberGuaranteedNotCollectionType(Integer num, int count) {
        return () -> new AbstractEnumerator<Integer>() {
            @Override
            public boolean moveNext() {
                if (this.state < 0)
                    return false;
                if (this.state < count) {
                    this.current = num;
                    this.state++;
                    return true;
                }
                this.close();
                return false;
            }
        };
    }

    protected static IEnumerable<Integer> NumberRangeGuaranteedNotCollectionType(int num, int count) {
        return () -> new AbstractEnumerator<Integer>() {
            @Override
            public boolean moveNext() {
                if (this.state < 0)
                    return false;
                if (this.state < count) {
                    this.current = num + this.state;
                    this.state++;
                    return true;
                }
                this.close();
                return false;
            }
        };
    }

    protected static IEnumerable<Integer> NullableNumberRangeGuaranteedNotCollectionType(Integer num, int count) {
        return () -> new AbstractEnumerator<Integer>() {
            @Override
            public boolean moveNext() {
                if (this.state < 0)
                    return false;
                if (this.state < count) {
                    this.current = num == null ? null : num + this.state;
                    this.state++;
                    return true;
                }
                this.close();
                return false;
            }
        };
    }

    protected static <T> List<Func1<IEnumerable<T>, IEnumerable<T>>> IdentityTransforms() {
        List<Func1<IEnumerable<T>, IEnumerable<T>>> list = new ArrayList<>();
        list.add(e -> e);
        list.add(e -> e.toArray());
        list.add(e -> Linq.of(e.toList()));
        list.add(e -> e.select(i -> i));
        list.add(e -> e.concat(Array.empty()));
        list.add(e -> ForceNotCollection(e));
        list.add(e -> e.concat(ForceNotCollection(Array.empty())));
        list.add(e -> e.where(i -> true));
        list.add(e -> ForceNotCollection(e).skip(0));
        return list;
    }

    protected static <T> IEnumerable<T> ForceNotCollection(IEnumerable<T> source) {
        return () -> new AbstractEnumerator<T>() {
            private IEnumerator<T> enumerator;

            @Override
            public boolean moveNext() {
                switch (this.state) {
                    case 0:
                        this.enumerator = source.enumerator();
                        this.state = 1;
                    case 1:
                        if (this.enumerator.moveNext()) {
                            this.current = this.enumerator.current();
                            return true;
                        }
                        this.close();
                        return false;
                    default:
                        return false;
                }
            }

            @Override
            public void close() {
                if (this.enumerator != null) {
                    this.enumerator.close();
                    this.enumerator = null;
                }
                super.close();
            }
        };
    }

    protected static <T> IEnumerable<T> FlipIsCollection(IEnumerable<T> source) {
        return source instanceof ICollection ? ForceNotCollection(source) : Linq.of(source.toList());
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
        if (expected == null)
            return false;
        try (IEnumerator<T> e1 = expected.enumerator();
             IEnumerator<T> e2 = actual.enumerator()) {
            while (e1.moveNext()) {
                if (!(e2.moveNext() && comparer.equals(e1.current(), e2.current())))
                    return false;
            }
            return !e2.moveNext();
        }
    }


    public static class TestCollection<T> implements ICollection<T> {
        public Object[] Items;
        public int CountTouched = 0;
        public int CopyToTouched = 0;
        public int ToArrayTouched = 0;
        public int ToListTouched = 0;

        public TestCollection(T[] items) {
            this.Items = items;
        }

        @Override
        public Collection<T> getCollection() {
            ThrowHelper.throwNotSupportedException();
            return null;
        }

        @Override
        public int _getCount() {
            this.CountTouched++;
            return this.Items.length;
        }

        @Override
        public boolean _contains(T item) {
            return Arrays.asList(this.Items).contains(item);
        }

        @Override
        public void _copyTo(Object[] array, int arrayIndex) {
            this.CopyToTouched++;
            System.arraycopy(this.Items, 0, array, arrayIndex, this.Items.length);
        }

        @Override
        public T[] _toArray(Class<T> clazz) {
            this.ToArrayTouched++;
            return ArrayUtils.toArray(this.Items, clazz);
        }

        @Override
        public Object[] _toArray() {
            this.ToArrayTouched++;
            return ArrayUtils.toArray(this.Items, Object.class);
        }

        @Override
        public List<T> _toList() {
            this.ToListTouched++;
            return ArrayUtils.toList(this.Items);
        }

        @Override
        public IEnumerator<T> enumerator() {
            return new ArrayEnumerator<>(this.Items);
        }
    }

    public static class TestEnumerable<T> implements IEnumerable<T> {
        private final T[] Items;

        public TestEnumerable(T[] items) {
            this.Items = items;
        }

        @Override
        public IEnumerator<T> enumerator() {
            return new GenericArrayEnumerator<>(this.Items);
        }
    }

    public static class TestReadOnlyCollection<T> implements ICollection<T> {
        public Object[] Items;
        public int CountTouched = 0;

        public TestReadOnlyCollection(T[] items) {
            this.Items = items;
        }

        @Override
        public Collection<T> getCollection() {
            ThrowHelper.throwNotSupportedException();
            return null;
        }

        @Override
        public int _getCount() {
            this.CountTouched++;
            return this.Items.length;
        }

        @Override
        public boolean _contains(T item) {
            ThrowHelper.throwNotSupportedException();
            return false;
        }

        @Override
        public void _copyTo(Object[] array, int arrayIndex) {
            ThrowHelper.throwNotSupportedException();
        }

        @Override
        public T[] _toArray(Class<T> clazz) {
            return ArrayUtils.toArray(this.Items, clazz);
        }

        @Override
        public Object[] _toArray() {
            return ArrayUtils.toArray(this.Items, Object.class);
        }

        @Override
        public List<T> _toList() {
            return ArrayUtils.toList(this.Items);
        }

        @Override
        public IEnumerator<T> enumerator() {
            return new ArrayEnumerator<>(this.Items);
        }
    }

    public static class FastInfiniteEnumerator<T> implements IEnumerable<T>, IEnumerator<T> {
        public IEnumerator<T> enumerator() {
            return this;
        }

        public boolean moveNext() {
            return true;
        }

        public T current() {
            return null;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public T next() {
            return null;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            if (action == null)
                ThrowHelper.throwArgumentNullException(ExceptionArgument.action);
            while (this.moveNext())
                action.accept(this.current());
        }

        @Override
        public void remove() {
            ThrowHelper.throwNotSupportedException();
        }

        @Override
        public void reset() {
            ThrowHelper.throwNotSupportedException();
        }

        @Override
        public void close() {
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
            try (IEnumerator<Character> en = Linq.chars(x).orderBy(i -> i).enumerator()) {
                for (char c : Linq.chars(y).orderBy(i -> i)) {
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
            for (char c : Linq.chars(obj))
                hash ^= c;
            return hash;
        }
    }

    public static class ThrowsOnMatchEnumerable<T> implements IEnumerable<T> {
        private final IEnumerable<T> data;
        private final T thrownOn;

        public ThrowsOnMatchEnumerable(IEnumerable<T> source, T thrownOn) {
            this.data = source;
            this.thrownOn = thrownOn;
        }

        public IEnumerator<T> enumerator() {
            return new AbstractEnumerator<T>() {
                private IEnumerator<T> enumerator;

                @Override
                public boolean moveNext() {
                    switch (this.state) {
                        case 0:
                            this.enumerator = ThrowsOnMatchEnumerable.this.data.enumerator();
                            this.state = 1;
                        case 1:
                            if (this.enumerator.moveNext()) {
                                T item = this.enumerator.current();
                                if (item.equals(ThrowsOnMatchEnumerable.this.thrownOn))
                                    throw new RuntimeException();
                                this.current = item;
                                return true;
                            }
                            this.close();
                            return false;
                        default:
                            return false;
                    }
                }

                @Override
                public void close() {
                    if (this.enumerator != null) {
                        this.enumerator.close();
                        this.enumerator = null;
                    }
                }
            };
        }
    }

    public static class TestEnumerator implements IEnumerable<Integer>, IEnumerator<Integer> {
        private int current = 0;
        private boolean checkedNext;
        private boolean hasNext;

        @Override
        public IEnumerator<Integer> enumerator() {
            return this;
        }

        @Override
        public boolean moveNext() {
            return this.current++ < 5;
        }

        @Override
        public Integer current() {
            return this.current;
        }

        @Override
        public boolean hasNext() {
            if (!this.checkedNext) {
                this.hasNext = this.moveNext();
                this.checkedNext = true;
            }
            return this.hasNext;
        }

        @Override
        public Integer next() {
            if (this.hasNext()) {
                this.checkedNext = false;
                return this.current();
            }
            ThrowHelper.throwNoSuchElementException();
            return null;
        }

        @Override
        public void forEachRemaining(Consumer<? super Integer> action) {
            if (action == null)
                ThrowHelper.throwArgumentNullException(ExceptionArgument.action);
            while (this.moveNext())
                action.accept(this.current());
        }

        @Override
        public void remove() {
            ThrowHelper.throwNotSupportedException();
        }

        @Override
        public void reset() {
            ThrowHelper.throwNotSupportedException();
        }

        @Override
        public void close() {
        }
    }

    public static class ThrowsOnCurrentEnumerator extends TestEnumerator {
        // A test enumerator that throws an InvalidOperationException when invoking Current after MoveNext has been called exactly once.
        @Override
        public Integer current() {
            Integer current = super.current();
            if (current == 1) {
                throw new InvalidOperationException();
            }
            return current;
        }
    }

    public static class ThrowsOnMoveNext extends TestEnumerator {
        // A test enumerator that throws an InvalidOperationException when invoking MoveNext after MoveNext has been called exactly once.
        @Override
        public boolean moveNext() {
            boolean baseReturn = super.moveNext();
            if (super.current() == 1) {
                throw new InvalidOperationException();
            }

            return baseReturn;
        }
    }

    public static class ThrowsOnGetEnumerator extends TestEnumerator {
        // A test enumerator that throws an InvalidOperationException when GetEnumerator is called for the first time.
        private int getEnumeratorCallCount;

        @Override
        public IEnumerator<Integer> enumerator() {
            if (this.getEnumeratorCallCount++ == 0) {
                throw new InvalidOperationException();
            }

            return super.enumerator();
        }
    }

    public static class StringWithIntArray extends ValueType {
        public final String name;
        public final Integer[] total;

        public StringWithIntArray(String name, Integer[] total) {
            this.name = name;
            this.total = total;
        }
    }

    public static class DelegateBasedCollection<T> implements ICollection<T> {
        protected Func0<Integer> CountWorker;
        protected Predicate0 IsReadOnlyWorker;
        protected Action1<T> AddWorker;
        protected Action0 ClearWorker;
        protected Predicate1<T> ContainsWorker;
        protected Predicate1<T> RemoveWorker;
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

    public static class DelegateIterator<TSource> implements IEnumerable<TSource>, IEnumerator<TSource> {
        private final Func0<IEnumerator<TSource>> enumerator;
        private final Predicate0 moveNext;
        private final Func0<TSource> current;
        private final Action0 reset;
        private final Action0 dispose;
        private boolean checkedNext;
        private boolean hasNext;

        public DelegateIterator(Predicate0 moveNext, Func0<TSource> current, Action0 dispose) {
            this(null, moveNext, current, null, dispose);
        }

        public DelegateIterator(Func0<IEnumerator<TSource>> enumerator, Predicate0 moveNext, Func0<TSource> current, Action0 reset, Action0 dispose) {
            this.enumerator = enumerator == null ? () -> this : enumerator;
            this.moveNext = moveNext == null ? () -> {
                ThrowHelper.throwNotSupportedException();
                return false;
            } : moveNext;
            this.current = current == null ? () -> {
                ThrowHelper.throwNotSupportedException();
                return null;
            } : current;
            this.reset = reset == null ? ThrowHelper::throwNotSupportedException : reset;
            this.dispose = dispose == null ? ThrowHelper::throwNotSupportedException : dispose;
        }

        @Override
        public IEnumerator<TSource> enumerator() {
            return this.enumerator.apply();
        }

        @Override
        public boolean moveNext() {
            return this.moveNext.apply();
        }

        @Override
        public TSource current() {
            return this.current.apply();
        }

        @Override
        public boolean hasNext() {
            if (!this.checkedNext) {
                this.hasNext = this.moveNext();
                this.checkedNext = true;
            }
            return this.hasNext;
        }

        @Override
        public TSource next() {
            if (this.hasNext()) {
                this.checkedNext = false;
                return this.current();
            }
            ThrowHelper.throwNoSuchElementException();
            return null;
        }

        @Override
        public void forEachRemaining(Consumer<? super TSource> action) {
            if (action == null)
                ThrowHelper.throwArgumentNullException(ExceptionArgument.action);
            while (this.moveNext())
                action.accept(this.current());
        }

        @Override
        public void remove() {
            ThrowHelper.throwNotSupportedException();
        }

        @Override
        public void reset() {
            this.reset.apply();
        }

        @Override
        public void close() {
            this.dispose.apply();
        }
    }

    public static class ArrayIterable<T> implements Iterable<T> {
        private final T[] array;

        @SafeVarargs
        public ArrayIterable(T... array) {
            this.array = array;
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayIterator<>(this.array);
        }
    }

    public static class ArrayIterator<T> implements Iterator<T> {
        private final T[] array;
        private int index = 0;

        ArrayIterator(T[] array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return this.index < this.array.length;
        }

        @Override
        public T next() {
            return this.array[this.index++];
        }
    }

    public static class CountIterable implements Iterable<Long> {
        private final long count;

        public CountIterable(long count) {
            this.count = count;
        }

        @Override
        public Iterator<Long> iterator() {
            return new CountIterator(this.count);
        }
    }

    public static class CountIterator implements Iterator<Long> {
        private final long count;
        private long current = 0;

        public CountIterator(long count) {
            this.count = count;
        }

        @Override
        public boolean hasNext() {
            return this.current < this.count;
        }

        @Override
        public Long next() {
            this.current++;
            return this.current;
        }
    }

    @SuppressWarnings("unused")
    public static class SkipTakeData {
        public static IEnumerable<Object[]> EnumerableData() {
            IEnumerable<Integer> sourceCounts = Linq.of(0, 1, 2, 3, 5, 8, 13, 55, 100, 250);

            IEnumerable<Integer> counts = Linq.of(1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 100, 250, 500, Integer.MAX_VALUE);
            final IEnumerable<Integer> countsF = counts.concat(counts.select(c -> -c)).append(0).append(Integer.MIN_VALUE);

            return sourceCounts.select(sourceCount -> Tuple.create(sourceCount, Linq.range(0, sourceCount)))
                    .selectMany(a -> countsF, (a, count) -> new Object[]{a.getItem2(), count});
        }

        public static IEnumerable<Object[]> EvaluationBehaviorData() {
            return Linq.range(-1, 15).select(count -> new Object[]{count});
        }
    }
}
