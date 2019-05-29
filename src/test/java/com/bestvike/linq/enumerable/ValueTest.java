package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-05-29.
 */
public class ValueTest extends TestCase {
    private NoFieldBean noFieldBean;
    private NoFieldBean noFieldBeanExpected;
    private OneFieldBean oneFieldBean;
    private OneFieldBean oneFieldBeanExpected;
    private TwoFieldBean twoFieldBean;
    private TwoFieldBean twoFieldBeanExpected;
    private MoreFieldBean moreFieldBean;
    private MoreFieldBean moreFieldBeanExpected;

    @Before
    public void init() {
        this.noFieldBean = new NoFieldBean();
        this.noFieldBeanExpected = new NoFieldBean();
        this.oneFieldBean = new OneFieldBean("Robert");
        this.oneFieldBeanExpected = new OneFieldBean("Robert");
        this.twoFieldBean = new TwoFieldBean("Robert", 45);
        this.twoFieldBeanExpected = new TwoFieldBean("Robert", 45);
        this.moreFieldBean = new MoreFieldBean();
        this.moreFieldBean.nullField = null;
        this.moreFieldBean.decimal = m("123.456");
        this.moreFieldBean.boolArr = new boolean[]{true, false, true};
        this.moreFieldBean.byteArr = new byte[]{Byte.MIN_VALUE, 2, 126, Byte.MAX_VALUE};
        this.moreFieldBean.shortArr = new short[]{Short.MIN_VALUE, 2, 3, Short.MAX_VALUE};
        this.moreFieldBean.intArr = new int[]{Integer.MIN_VALUE, 2, 3, Integer.MAX_VALUE};
        this.moreFieldBean.longArr = new long[]{Long.MIN_VALUE, 2, 3, Long.MAX_VALUE};
        this.moreFieldBean.charArr = new char[]{'\0', 35, '!', '$', 'a'};
        this.moreFieldBean.floatArr = new float[]{Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.MIN_VALUE, Float.MAX_VALUE};
        this.moreFieldBean.doubleArr = new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.MIN_VALUE, Double.MAX_VALUE};
        this.moreFieldBean.objArr = new Object[]{null, "!@#$%^&*()_+", m("123.456"), m("9999999.099999999"), new NoFieldBean()};
        this.moreFieldBean.objArr2 = new BigDecimal[]{null, m("355.99")};
        this.moreFieldBean.iCollection = (ICollection<?>) Linq.asEnumerable(null, "", Empty, "你好世界");
        this.moreFieldBean.listProvider = Linq.asEnumerable(null, "", Empty, "你好世界").select(a -> a + "_suffix");
        this.moreFieldBean.enumerable = Linq.asEnumerable(null, "", Empty, "你好世界").where(Objects::nonNull);
        this.moreFieldBean.collection = Arrays.asList(null, "", Empty, "你好世界");
        this.moreFieldBean.iterable = new ArrayIterable<>(null, "", Empty, "你好世界");
        Map<Object, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put(null, "hello");
        map.put("key", m("123.0"));
        map.put(Empty, new OneFieldBean("世界"));
        this.moreFieldBean.map = map;
        this.moreFieldBean.bean = new TwoFieldBean("Tim", 1239);
        this.moreFieldBeanExpected = new MoreFieldBean();
        this.moreFieldBeanExpected.nullField = null;
        this.moreFieldBeanExpected.decimal = m("123.456000");
        this.moreFieldBeanExpected.boolArr = new boolean[]{true, false, true};
        this.moreFieldBeanExpected.byteArr = new byte[]{Byte.MIN_VALUE, 2, 126, Byte.MAX_VALUE};
        this.moreFieldBeanExpected.shortArr = new short[]{Short.MIN_VALUE, 2, 3, Short.MAX_VALUE};
        this.moreFieldBeanExpected.intArr = new int[]{Integer.MIN_VALUE, 2, 3, Integer.MAX_VALUE};
        this.moreFieldBeanExpected.longArr = new long[]{Long.MIN_VALUE, 2, 3, Long.MAX_VALUE};
        this.moreFieldBeanExpected.charArr = new char[]{'\0', 35, '!', '$', 'a'};
        this.moreFieldBeanExpected.floatArr = new float[]{Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.MIN_VALUE, Float.MAX_VALUE};
        this.moreFieldBeanExpected.doubleArr = new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.MIN_VALUE, Double.MAX_VALUE};
        this.moreFieldBeanExpected.objArr = new Object[]{null, "!@#$%^&*()_+", m("123.45600"), m("9999999.0999999990"), new NoFieldBean()};
        this.moreFieldBeanExpected.objArr2 = new BigDecimal[]{null, m("355.99000")};
        this.moreFieldBeanExpected.iCollection = (ICollection<?>) Linq.asEnumerable(null, "", Empty, "你好世界");
        this.moreFieldBeanExpected.listProvider = Linq.asEnumerable(null, "", Empty, "你好世界").select(a -> a + "_suffix");
        this.moreFieldBeanExpected.enumerable = Linq.asEnumerable(null, "", Empty, "你好世界").where(Objects::nonNull);
        this.moreFieldBeanExpected.collection = Arrays.asList(null, "", Empty, "你好世界");
        this.moreFieldBeanExpected.iterable = new ArrayIterable<>(null, "", Empty, "你好世界");
        Map<Object, Object> map2 = new HashMap<>();
        map2.put("a", 1);
        map2.put(null, "hello");
        map2.put("key", m("123.0000000"));
        map2.put(Empty, new OneFieldBean("世界"));
        this.moreFieldBeanExpected.map = map2;
        this.moreFieldBeanExpected.bean = new TwoFieldBean("Tim", 1239);
    }

    @Test
    public void testEquals() {
        assertTrue(Value.equals(null, null));
        assertFalse(Value.equals(null, this.noFieldBean));
        assertFalse(Value.equals(this.noFieldBean, null));

        assertNotSame(this.noFieldBeanExpected, this.noFieldBean);
        assertTrue(Value.equals(this.noFieldBeanExpected, this.noFieldBean));

        assertNotSame(this.oneFieldBeanExpected, this.oneFieldBean);
        assertTrue(Value.equals(this.oneFieldBeanExpected, this.oneFieldBean));

        assertNotSame(this.twoFieldBeanExpected, this.twoFieldBean);
        assertTrue(Value.equals(this.twoFieldBeanExpected, this.twoFieldBean));

        assertNotSame(this.moreFieldBeanExpected, this.moreFieldBean);
        assertTrue(Value.equals(this.moreFieldBeanExpected, this.moreFieldBean));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Value.hashCode(null));
        assertEquals(1, Value.hashCode(new int[0]));

        assertEquals(Value.hashCode(this.noFieldBeanExpected), Value.hashCode(this.noFieldBean));
        assertEquals(Value.hashCode(this.oneFieldBeanExpected), Value.hashCode(this.oneFieldBean));
        assertEquals(Value.hashCode(this.twoFieldBeanExpected), Value.hashCode(this.twoFieldBean));
        assertEquals(Value.hashCode(this.moreFieldBeanExpected), Value.hashCode(this.moreFieldBean));
    }

    @Test
    public void testToString() {
        assertEquals(Value.toString(this.noFieldBeanExpected), Value.toString(this.noFieldBean));
        assertEquals("NoFieldBean{}", Value.toString(this.noFieldBean));

        assertEquals(Value.toString(this.oneFieldBeanExpected), Value.toString(this.oneFieldBean));
        assertEquals("OneFieldBean{Name='Robert'}", Value.toString(this.oneFieldBean));

        assertEquals(Value.toString(this.twoFieldBeanExpected), Value.toString(this.twoFieldBean));
        assertEquals("TwoFieldBean{Name='Robert', Score=45}", Value.toString(this.twoFieldBean));

        assertEquals(Value.toString(this.moreFieldBeanExpected), Value.toString(this.moreFieldBean));
        assertEquals("MoreFieldBean{nullField=null, decimal=123.456000, boolArr=[true, false, true], byteArr=[-128, 2, 126, 127], shortArr=[-32768, 2, 3, 32767], intArr=[-2147483648, 2, 3, 2147483647], longArr=[-9223372036854775808, 2, 3, 9223372036854775807], charArr=[\u0000, #, !, $, a], floatArr=[-Infinity, Infinity, 1.4E-45, 3.4028235E38], doubleArr=[-Infinity, Infinity, 4.9E-324, 1.7976931348623157E308], objArr=[null, '!@#$%^&*()_+', 123.456000, 9999999.100000, NoFieldBean{}], objArr2=[null, 355.990000], iCollection=[null, '', '', '你好世界'], listProvider=['null_suffix', '_suffix', '_suffix', '你好世界_suffix'], enumerable=['', '', '你好世界'], collection=[null, '', '', '你好世界'], iterable=[null, '', '', '你好世界'], map=HashMap{null='hello', ''=OneFieldBean{Name='世界'}, 'a'=1, 'key'=123.000000}, bean=TwoFieldBean{Name='Tim', Score=1239}}", Value.toString(this.moreFieldBean));
    }


    private static class NoFieldBean {
    }

    private static class OneFieldBean {
        final String Name;

        private OneFieldBean(String name) {
            this.Name = name;
        }
    }

    private static class TwoFieldBean {
        final String Name;
        final int Score;

        private TwoFieldBean(String name, int score) {
            this.Name = name;
            this.Score = score;
        }
    }

    private static class MoreFieldBean {
        private NoFieldBean nullField;
        private BigDecimal decimal;
        private boolean[] boolArr;
        private byte[] byteArr;
        private short[] shortArr;
        private int[] intArr;
        private long[] longArr;
        private char[] charArr;
        private float[] floatArr;
        private double[] doubleArr;
        private Object[] objArr;
        private BigDecimal[] objArr2;
        private ICollection<?> iCollection;
        private IEnumerable<?> listProvider;
        private IEnumerable<?> enumerable;
        private Collection<?> collection;
        private Iterable<?> iterable;
        private Map<?, ?> map;
        private TwoFieldBean bean;
    }

    private static class ArrayIterable<T> implements Iterable<T> {
        private final T[] array;

        ArrayIterable(T... array) {
            this.array = array;
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayIterator<>(this.array);
        }
    }

    private static final class ArrayIterator<T> implements Iterator<T> {
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
}
