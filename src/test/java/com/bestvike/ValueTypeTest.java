package com.bestvike;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-05-28.
 */
class ValueTypeTest extends TestCase {
    private final NoFieldBean noFieldBean;
    private final NoFieldBean noFieldBeanExpected;
    private final OneFieldBean oneFieldBean;
    private final OneFieldBean oneFieldBeanExpected;
    private final TwoFieldBean twoFieldBean;
    private final TwoFieldBean twoFieldBeanExpected;
    private final MoreFieldBean nullMoreFieldBean;
    private final MoreFieldBean nullMoreFieldBeanExpected;
    private final MoreFieldBean emptyMoreFieldBean;
    private final MoreFieldBean emptyMoreFieldBeanExpected;
    private final MoreFieldBean moreFieldBean;
    private final MoreFieldBean moreFieldBeanExpected;

    {
        this.noFieldBean = new NoFieldBean();
        this.noFieldBeanExpected = new NoFieldBean();
        this.oneFieldBean = new OneFieldBean("Robert");
        this.oneFieldBeanExpected = new OneFieldBean("Robert");
        this.twoFieldBean = new TwoFieldBean("Robert", 45);
        this.twoFieldBeanExpected = new TwoFieldBean("Robert", 45);
        this.nullMoreFieldBean = new MoreFieldBean();
        this.nullMoreFieldBeanExpected = new MoreFieldBean();
        this.emptyMoreFieldBean = new MoreFieldBean();
        this.emptyMoreFieldBean.nullField = null;
        this.emptyMoreFieldBean.decimal = m("0");
        this.emptyMoreFieldBean.boolArr = new boolean[]{};
        this.emptyMoreFieldBean.byteArr = new byte[]{};
        this.emptyMoreFieldBean.shortArr = new short[]{};
        this.emptyMoreFieldBean.intArr = new int[]{};
        this.emptyMoreFieldBean.longArr = new long[]{};
        this.emptyMoreFieldBean.charArr = new char[]{};
        this.emptyMoreFieldBean.floatArr = new float[]{};
        this.emptyMoreFieldBean.doubleArr = new double[]{};
        this.emptyMoreFieldBean.objArr = new Object[]{};
        this.emptyMoreFieldBean.objArr2 = new BigDecimal[]{};
        this.emptyMoreFieldBean.iCollection = (ICollection<?>) Linq.of();
        this.emptyMoreFieldBean.listProvider = Linq.of().select(a -> a + "_suffix");
        this.emptyMoreFieldBean.enumerable = Linq.of().where(Objects::nonNull);
        this.emptyMoreFieldBean.collection = Collections.emptyList();
        this.emptyMoreFieldBean.iterable = new ArrayIterable<>();
        this.emptyMoreFieldBean.map = new LinkedHashMap<>();
        this.emptyMoreFieldBean.bean = new TwoFieldBean("Tim", 1239);
        this.emptyMoreFieldBeanExpected = new MoreFieldBean();
        this.emptyMoreFieldBeanExpected.nullField = null;
        this.emptyMoreFieldBeanExpected.decimal = m("0.0000");
        this.emptyMoreFieldBeanExpected.boolArr = new boolean[]{};
        this.emptyMoreFieldBeanExpected.byteArr = new byte[]{};
        this.emptyMoreFieldBeanExpected.shortArr = new short[]{};
        this.emptyMoreFieldBeanExpected.intArr = new int[]{};
        this.emptyMoreFieldBeanExpected.longArr = new long[]{};
        this.emptyMoreFieldBeanExpected.charArr = new char[]{};
        this.emptyMoreFieldBeanExpected.floatArr = new float[]{};
        this.emptyMoreFieldBeanExpected.doubleArr = new double[]{};
        this.emptyMoreFieldBeanExpected.objArr = new Object[]{};
        this.emptyMoreFieldBeanExpected.objArr2 = new BigDecimal[]{};
        this.emptyMoreFieldBeanExpected.iCollection = (ICollection<?>) Linq.of();
        this.emptyMoreFieldBeanExpected.listProvider = Linq.of().select(a -> a + "_suffix");
        this.emptyMoreFieldBeanExpected.enumerable = Linq.of().where(Objects::nonNull);
        this.emptyMoreFieldBeanExpected.collection = Collections.emptyList();
        this.emptyMoreFieldBeanExpected.iterable = new ArrayIterable<>();
        this.emptyMoreFieldBeanExpected.map = new LinkedHashMap<>();
        this.emptyMoreFieldBeanExpected.bean = new TwoFieldBean("Tim", 1239);
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
        this.moreFieldBean.iCollection = (ICollection<?>) Linq.of(null, "", Empty, "你好世界");
        this.moreFieldBean.listProvider = Linq.of(null, "", Empty, "你好世界").select(a -> a + "_suffix");
        this.moreFieldBean.enumerable = Linq.of(null, "", Empty, "你好世界").where(Objects::nonNull);
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
        this.moreFieldBeanExpected.iCollection = (ICollection<?>) Linq.of(null, "", Empty, "你好世界");
        this.moreFieldBeanExpected.listProvider = Linq.of(null, "", Empty, "你好世界").select(a -> a + "_suffix");
        this.moreFieldBeanExpected.enumerable = Linq.of(null, "", Empty, "你好世界").where(Objects::nonNull);
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
    void testEquals() {
        assertFalse(this.noFieldBean.equals(null));

        assertNotSame(this.noFieldBeanExpected, this.noFieldBean);
        assertTrue(this.noFieldBeanExpected.equals(this.noFieldBean));

        assertNotSame(this.oneFieldBeanExpected, this.oneFieldBean);
        assertTrue(this.oneFieldBeanExpected.equals(this.oneFieldBean));

        assertNotSame(this.twoFieldBeanExpected, this.twoFieldBean);
        assertTrue(this.twoFieldBeanExpected.equals(this.twoFieldBean));

        assertNotSame(this.nullMoreFieldBeanExpected, this.nullMoreFieldBean);
        assertTrue(this.nullMoreFieldBeanExpected.equals(this.nullMoreFieldBean));
        assertNotSame(this.emptyMoreFieldBeanExpected, this.emptyMoreFieldBean);
        assertTrue(this.emptyMoreFieldBeanExpected.equals(this.emptyMoreFieldBean));
        assertNotSame(this.moreFieldBeanExpected, this.moreFieldBean);
        assertTrue(this.moreFieldBeanExpected.equals(this.moreFieldBean));
    }

    @Test
    void testHashCode() {
        assertEquals(this.noFieldBeanExpected.hashCode(), this.noFieldBean.hashCode());
        assertEquals(this.oneFieldBeanExpected.hashCode(), this.oneFieldBean.hashCode());
        assertEquals(this.twoFieldBeanExpected.hashCode(), this.twoFieldBean.hashCode());
        assertEquals(this.nullMoreFieldBeanExpected.hashCode(), this.nullMoreFieldBean.hashCode());
        assertEquals(this.emptyMoreFieldBeanExpected.hashCode(), this.emptyMoreFieldBean.hashCode());
        assertEquals(this.moreFieldBeanExpected.hashCode(), this.moreFieldBean.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(this.noFieldBeanExpected.toString(), this.noFieldBean.toString());
        assertEquals("NoFieldBean{}", this.noFieldBean.toString());

        assertEquals(this.oneFieldBeanExpected.toString(), this.oneFieldBean.toString());
        assertEquals("OneFieldBean{Name='Robert'}", this.oneFieldBean.toString());

        assertEquals(this.twoFieldBeanExpected.toString(), this.twoFieldBean.toString());
        assertEquals("TwoFieldBean{Name='Robert', Score=45}", this.twoFieldBean.toString());

        assertEquals(this.nullMoreFieldBeanExpected.toString(), this.nullMoreFieldBean.toString());
        assertEquals("MoreFieldBean{nullField=null, decimal=null, boolArr=null, byteArr=null, shortArr=null, intArr=null, longArr=null, charArr=null, floatArr=null, doubleArr=null, objArr=null, objArr2=null, iCollection=null, listProvider=null, enumerable=null, collection=null, iterable=null, map=null, bean=null}", this.nullMoreFieldBean.toString());
        assertEquals(this.emptyMoreFieldBeanExpected.toString(), this.emptyMoreFieldBean.toString());
        assertEquals("MoreFieldBean{nullField=null, decimal=0.000000, boolArr=[], byteArr=[], shortArr=[], intArr=[], longArr=[], charArr=[], floatArr=[], doubleArr=[], objArr=[], objArr2=[], iCollection=[], listProvider=[], enumerable=[], collection=[], iterable=[], map=LinkedHashMap{}, bean=TwoFieldBean{Name='Tim', Score=1239}}", this.emptyMoreFieldBean.toString());
        assertEquals(this.moreFieldBeanExpected.toString(), this.moreFieldBean.toString());
        assertEquals("MoreFieldBean{nullField=null, decimal=123.456000, boolArr=[true, false, true], byteArr=[-128, 2, 126, 127], shortArr=[-32768, 2, 3, 32767], intArr=[-2147483648, 2, 3, 2147483647], longArr=[-9223372036854775808, 2, 3, 9223372036854775807], charArr=[\u0000, #, !, $, a], floatArr=[-Infinity, Infinity, 1.4E-45, 3.4028235E38], doubleArr=[-Infinity, Infinity, 4.9E-324, 1.7976931348623157E308], objArr=[null, '!@#$%^&*()_+', 123.456000, 9999999.100000, NoFieldBean{}], objArr2=[null, 355.990000], iCollection=[null, '', '', '你好世界'], listProvider=['null_suffix', '_suffix', '_suffix', '你好世界_suffix'], enumerable=['', '', '你好世界'], collection=[null, '', '', '你好世界'], iterable=[null, '', '', '你好世界'], map=HashMap{null='hello', ''=OneFieldBean{Name='世界'}, 'a'=1, 'key'=123.000000}, bean=TwoFieldBean{Name='Tim', Score=1239}}", this.moreFieldBean.toString());
    }


    private static class NoFieldBean extends ValueType {
    }

    private static class OneFieldBean extends ValueType {
        private final String Name;

        private OneFieldBean(String name) {
            this.Name = name;
        }
    }

    private static class TwoFieldBean extends ValueType {
        private final String Name;
        private final int Score;

        private TwoFieldBean(String name, int score) {
            this.Name = name;
            this.Score = score;
        }
    }

    private static class MoreFieldBean extends ValueType {
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
}
