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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-05-29.
 */
public class ValuesTest extends TestCase {
    private NoFieldBean noFieldBean;
    private NoFieldBean noFieldBeanExpected;
    private OneFieldBean oneFieldBean;
    private OneFieldBean oneFieldBeanExpected;
    private TwoFieldBean twoFieldBean;
    private TwoFieldBean twoFieldBeanExpected;
    private MoreFieldBean nullMoreFieldBean;
    private MoreFieldBean nullMoreFieldBeanExpected;
    private MoreFieldBean emptyMoreFieldBean;
    private MoreFieldBean emptyMoreFieldBeanExpected;
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
        this.emptyMoreFieldBean.iCollection = (ICollection<?>) Linq.asEnumerable();
        this.emptyMoreFieldBean.listProvider = Linq.asEnumerable().select(a -> a + "_suffix");
        this.emptyMoreFieldBean.enumerable = Linq.asEnumerable().where(Objects::nonNull);
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
        this.emptyMoreFieldBeanExpected.iCollection = (ICollection<?>) Linq.asEnumerable();
        this.emptyMoreFieldBeanExpected.listProvider = Linq.asEnumerable().select(a -> a + "_suffix");
        this.emptyMoreFieldBeanExpected.enumerable = Linq.asEnumerable().where(Objects::nonNull);
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
        assertTrue(Values.equals(null, null));
        assertFalse(Values.equals(null, this.noFieldBean));
        assertFalse(Values.equals(this.noFieldBean, null));

        assertNotSame(this.noFieldBeanExpected, this.noFieldBean);
        assertTrue(Values.equals(this.noFieldBeanExpected, this.noFieldBean));

        assertNotSame(this.oneFieldBeanExpected, this.oneFieldBean);
        assertTrue(Values.equals(this.oneFieldBeanExpected, this.oneFieldBean));

        assertNotSame(this.twoFieldBeanExpected, this.twoFieldBean);
        assertTrue(Values.equals(this.twoFieldBeanExpected, this.twoFieldBean));

        assertNotSame(this.nullMoreFieldBeanExpected, this.nullMoreFieldBean);
        assertTrue(Values.equals(this.nullMoreFieldBeanExpected, this.nullMoreFieldBean));
        assertNotSame(this.emptyMoreFieldBeanExpected, this.emptyMoreFieldBean);
        assertTrue(Values.equals(this.emptyMoreFieldBeanExpected, this.emptyMoreFieldBean));
        assertNotSame(this.moreFieldBeanExpected, this.moreFieldBean);
        assertTrue(Values.equals(this.moreFieldBeanExpected, this.moreFieldBean));
    }

    @Test
    public void testEqualsRefEq() {
        Object obj = new Object();
        assertTrue(Values.equals(null, null));
        assertTrue(Values.equals(obj, obj));
    }

    @Test
    public void testEqualsNull() {
        Object obj = new Object();
        assertFalse(Values.equals(obj, null));
        assertFalse(Values.equals(null, obj));
    }

    @Test
    public void testEqualsDecimal() {
        assertTrue(Values.equals(m("1.1"), m("1.1000")));
        assertFalse(Values.equals(m("1.1"), m("9.1000")));
    }

    @Test
    public void testEqualsBooleanArray() {
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));

        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new Object[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new Object[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new Object[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));

        assertTrue(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE) instanceof ICollection);
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE)));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
        assertTrue(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)));

        assertFalse(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE).select(a -> a)));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE).select(a -> a)));
        assertTrue(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE).where(a -> true)));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE).where(a -> true)));
        assertTrue(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE)) instanceof IIListProvider);
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE))));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(new ArrayIterable<>(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE))));
        assertTrue(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Linq.asEnumerable(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE))));

        assertFalse(Arrays.asList(Boolean.TRUE, Boolean.FALSE) instanceof IEnumerable);
        assertTrue(Arrays.asList(Boolean.TRUE, Boolean.FALSE) instanceof Collection);
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Arrays.asList(Boolean.TRUE, Boolean.FALSE)));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Arrays.asList(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
        assertTrue(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)));

        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE)));
        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new ArrayIterable<>(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
        assertTrue(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)));

        assertFalse(Values.equals(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new Object()));
    }

    @Test
    public void testEqualsByteArray() {
        final byte BYTE_ZERO = 0;
        final byte BYTE_ONE = 1;
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new byte[]{Byte.MIN_VALUE, BYTE_ZERO}));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new byte[]{Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE}));
        assertTrue(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}));

        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new Object[]{Byte.MIN_VALUE, BYTE_ZERO}));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new Object[]{Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE}));
        assertTrue(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new Object[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO) instanceof ICollection);
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO)));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE)));
        assertTrue(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO).select(a -> a)));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO).where(a -> true)));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO))));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE))));
        assertTrue(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE))));

        assertFalse(Arrays.asList(Byte.MIN_VALUE, BYTE_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Byte.MIN_VALUE, BYTE_ZERO) instanceof Collection);
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Arrays.asList(Byte.MIN_VALUE, BYTE_ZERO)));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Arrays.asList(Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE)));
        assertTrue(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, Arrays.asList(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE)));

        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO)));
        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE)));
        assertTrue(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE)));

        assertFalse(Values.equals(new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new Object()));
    }

    @Test
    public void testEqualsShortArray() {
        final short SHORT_ZERO = 0;
        final short SHORT_ONE = 1;
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new short[]{Short.MIN_VALUE, SHORT_ZERO}));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new short[]{Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE}));
        assertTrue(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}));

        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new Object[]{Short.MIN_VALUE, SHORT_ZERO}));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new Object[]{Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE}));
        assertTrue(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new Object[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO)));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE)));
        assertTrue(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO).select(a -> a)));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO).where(a -> true)));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO))));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE))));
        assertTrue(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE))));

        assertFalse(Arrays.asList(Short.MIN_VALUE, SHORT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Short.MIN_VALUE, SHORT_ZERO) instanceof Collection);
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Arrays.asList(Short.MIN_VALUE, SHORT_ZERO)));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Arrays.asList(Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE)));
        assertTrue(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, Arrays.asList(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE)));

        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO)));
        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new ArrayIterable<>(Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE)));
        assertTrue(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE)));

        assertFalse(Values.equals(new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new Object()));
    }

    @Test
    public void testEqualsIntArray() {
        final int INT_ZERO = 0;
        final int INT_ONE = 1;
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new int[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new int[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new Object[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new Object[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a)));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true)));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO))));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE))));
        assertTrue(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE))));

        assertFalse(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof Collection);
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Arrays.asList(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Arrays.asList(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new Object()));
    }

    @Test
    public void testEqualsLongArray() {
        final long LONG_ZERO = 0;
        final long LONG_ONE = 1;
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new long[]{Long.MIN_VALUE, LONG_ZERO}));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new long[]{Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE}));
        assertTrue(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}));

        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new Object[]{Long.MIN_VALUE, LONG_ZERO}));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new Object[]{Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE}));
        assertTrue(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new Object[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO) instanceof ICollection);
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO)));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE)));
        assertTrue(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO).select(a -> a)));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO).where(a -> true)));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO))));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE))));
        assertTrue(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE))));

        assertFalse(Arrays.asList(Long.MIN_VALUE, LONG_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Long.MIN_VALUE, LONG_ZERO) instanceof Collection);
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Arrays.asList(Long.MIN_VALUE, LONG_ZERO)));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Arrays.asList(Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE)));
        assertTrue(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, Arrays.asList(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE)));

        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO)));
        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new ArrayIterable<>(Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE)));
        assertTrue(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE)));

        assertFalse(Values.equals(new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new Object()));
    }

    @Test
    public void testEqualsCharArray() {
        final char CHAR_ZERO = 0;
        final char CHAR_ONE = 1;
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new char[]{Character.MIN_VALUE, CHAR_ZERO}));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new char[]{Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE}));
        assertTrue(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}));

        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new Object[]{Character.MIN_VALUE, CHAR_ZERO}));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new Object[]{Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE}));
        assertTrue(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new Object[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO) instanceof ICollection);
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO)));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE)));
        assertTrue(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO).select(a -> a)));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO).where(a -> true)));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO))));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE))));
        assertTrue(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE))));

        assertFalse(Arrays.asList(Character.MIN_VALUE, CHAR_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Character.MIN_VALUE, CHAR_ZERO) instanceof Collection);
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Arrays.asList(Character.MIN_VALUE, CHAR_ZERO)));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Arrays.asList(Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE)));
        assertTrue(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, Arrays.asList(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE)));

        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO)));
        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new ArrayIterable<>(Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE)));
        assertTrue(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE)));

        assertFalse(Values.equals(new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new Object()));
    }

    @Test
    public void testEqualsFloatArray() {
        final float FLOAT_ZERO = 0;
        final float FLOAT_ONE = 1;
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new float[]{Float.MIN_VALUE, FLOAT_ZERO}));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new float[]{Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE}));
        assertTrue(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}));

        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new Object[]{Float.MIN_VALUE, FLOAT_ZERO}));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new Object[]{Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE}));
        assertTrue(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new Object[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO)));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE)));
        assertTrue(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO).select(a -> a)));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO).where(a -> true)));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO))));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE))));
        assertTrue(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE))));

        assertFalse(Arrays.asList(Float.MIN_VALUE, FLOAT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Float.MIN_VALUE, FLOAT_ZERO) instanceof Collection);
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Arrays.asList(Float.MIN_VALUE, FLOAT_ZERO)));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Arrays.asList(Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE)));
        assertTrue(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, Arrays.asList(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE)));

        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO)));
        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE)));
        assertTrue(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE)));

        assertFalse(Values.equals(new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new Object()));
    }

    @Test
    public void testEqualsDoubleArray() {
        final double DOUBLE_ZERO = 0;
        final double DOUBLE_ONE = 1;
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new double[]{Double.MIN_VALUE, DOUBLE_ZERO}));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new double[]{Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE}));
        assertTrue(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}));

        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new Object[]{Double.MIN_VALUE, DOUBLE_ZERO}));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new Object[]{Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE}));
        assertTrue(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new Object[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO) instanceof ICollection);
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO)));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE)));
        assertTrue(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO).select(a -> a)));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO).where(a -> true)));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO))));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE))));
        assertTrue(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE))));

        assertFalse(Arrays.asList(Double.MIN_VALUE, DOUBLE_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Double.MIN_VALUE, DOUBLE_ZERO) instanceof Collection);
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Arrays.asList(Double.MIN_VALUE, DOUBLE_ZERO)));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Arrays.asList(Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE)));
        assertTrue(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, Arrays.asList(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE)));

        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO)));
        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE)));
        assertTrue(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE)));

        assertFalse(Values.equals(new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new Object()));
    }

    @Test
    public void testEqualsObjectArray() {
        assertFalse(Values.equals(new Object[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(Values.equals(new Object[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(Values.equals(new Object[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));

        final byte BYTE_ZERO = 0;
        final byte BYTE_ONE = 1;
        assertFalse(Values.equals(new Object[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new byte[]{Byte.MIN_VALUE, BYTE_ZERO}));
        assertFalse(Values.equals(new Object[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new byte[]{Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE}));
        assertTrue(Values.equals(new Object[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}, new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}));

        final short SHORT_ZERO = 0;
        final short SHORT_ONE = 1;
        assertFalse(Values.equals(new Object[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new short[]{Short.MIN_VALUE, SHORT_ZERO}));
        assertFalse(Values.equals(new Object[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new short[]{Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE}));
        assertTrue(Values.equals(new Object[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}, new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}));

        final int INT_ZERO = 0;
        final int INT_ONE = 1;
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new int[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new int[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        final long LONG_ZERO = 0;
        final long LONG_ONE = 1;
        assertFalse(Values.equals(new Object[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new long[]{Long.MIN_VALUE, LONG_ZERO}));
        assertFalse(Values.equals(new Object[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new long[]{Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE}));
        assertTrue(Values.equals(new Object[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}, new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}));

        final char CHAR_ZERO = 0;
        final char CHAR_ONE = 1;
        assertFalse(Values.equals(new Object[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new char[]{Character.MIN_VALUE, CHAR_ZERO}));
        assertFalse(Values.equals(new Object[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new char[]{Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE}));
        assertTrue(Values.equals(new Object[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}, new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}));

        final float FLOAT_ZERO = 0;
        final float FLOAT_ONE = 1;
        assertFalse(Values.equals(new Object[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new float[]{Float.MIN_VALUE, FLOAT_ZERO}));
        assertFalse(Values.equals(new Object[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new float[]{Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE}));
        assertTrue(Values.equals(new Object[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}, new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}));

        final double DOUBLE_ZERO = 0;
        final double DOUBLE_ONE = 1;
        assertFalse(Values.equals(new Object[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new double[]{Double.MIN_VALUE, DOUBLE_ZERO}));
        assertFalse(Values.equals(new Object[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new double[]{Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE}));
        assertTrue(Values.equals(new Object[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}, new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}));

        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new Object[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new Object[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a)));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true)));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO))));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE))));
        assertTrue(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE))));

        assertFalse(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof Collection);
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Arrays.asList(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Arrays.asList(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}, new Object()));
    }

    @Test
    public void testEqualsICollection() {
        assertFalse(Values.equals(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), new boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(Values.equals(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));

        final byte BYTE_ZERO = 0;
        final byte BYTE_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE), new byte[]{Byte.MIN_VALUE, BYTE_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE), new byte[]{Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE), new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}));

        final short SHORT_ZERO = 0;
        final short SHORT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE), new short[]{Short.MIN_VALUE, SHORT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE), new short[]{Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE), new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}));

        final int INT_ZERO = 0;
        final int INT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new int[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new int[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        final long LONG_ZERO = 0;
        final long LONG_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE), new long[]{Long.MIN_VALUE, LONG_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE), new long[]{Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE), new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}));

        final char CHAR_ZERO = 0;
        final char CHAR_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE), new char[]{Character.MIN_VALUE, CHAR_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE), new char[]{Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE), new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}));

        final float FLOAT_ZERO = 0;
        final float FLOAT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE), new float[]{Float.MIN_VALUE, FLOAT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE), new float[]{Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE), new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}));

        final double DOUBLE_ZERO = 0;
        final double DOUBLE_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE), new double[]{Double.MIN_VALUE, DOUBLE_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE), new double[]{Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE), new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}));

        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO))));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE))));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE))));

        assertFalse(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof Collection);
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Arrays.asList(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Arrays.asList(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object()));
    }

    @Test
    public void testEqualsIIListProviderLength() {
        assertFalse(Values.equals(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE).select(a -> a), new boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(Values.equals(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE).select(a -> a), new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE).select(a -> a), new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));

        final byte BYTE_ZERO = 0;
        final byte BYTE_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE).select(a -> a), new byte[]{Byte.MIN_VALUE, BYTE_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE).select(a -> a), new byte[]{Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE).select(a -> a), new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}));

        final short SHORT_ZERO = 0;
        final short SHORT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE).select(a -> a), new short[]{Short.MIN_VALUE, SHORT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE).select(a -> a), new short[]{Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE).select(a -> a), new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}));

        final int INT_ZERO = 0;
        final int INT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new int[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new int[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        final long LONG_ZERO = 0;
        final long LONG_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE).select(a -> a), new long[]{Long.MIN_VALUE, LONG_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE).select(a -> a), new long[]{Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE).select(a -> a), new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}));

        final char CHAR_ZERO = 0;
        final char CHAR_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE).select(a -> a), new char[]{Character.MIN_VALUE, CHAR_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE).select(a -> a), new char[]{Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE).select(a -> a), new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}));

        final float FLOAT_ZERO = 0;
        final float FLOAT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE).select(a -> a), new float[]{Float.MIN_VALUE, FLOAT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE).select(a -> a), new float[]{Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE).select(a -> a), new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}));

        final double DOUBLE_ZERO = 0;
        final double DOUBLE_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE).select(a -> a), new double[]{Double.MIN_VALUE, DOUBLE_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE).select(a -> a), new double[]{Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE).select(a -> a), new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}));

        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new Object[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new Object[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO))));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE))));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE))));

        assertFalse(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof Collection);
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Arrays.asList(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Arrays.asList(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a), new Object()));
    }

    @Test
    public void testEqualsIIListProviderNoLength() {
        assertFalse(Values.equals(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE).where(a -> true), new boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(Values.equals(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE).where(a -> true), new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE).where(a -> true), new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));

        final byte BYTE_ZERO = 0;
        final byte BYTE_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE).where(a -> true), new byte[]{Byte.MIN_VALUE, BYTE_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE).where(a -> true), new byte[]{Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE).where(a -> true), new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}));

        final short SHORT_ZERO = 0;
        final short SHORT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE).where(a -> true), new short[]{Short.MIN_VALUE, SHORT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE).where(a -> true), new short[]{Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE).where(a -> true), new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}));

        final int INT_ZERO = 0;
        final int INT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new int[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new int[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        final long LONG_ZERO = 0;
        final long LONG_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE).where(a -> true), new long[]{Long.MIN_VALUE, LONG_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE).where(a -> true), new long[]{Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE).where(a -> true), new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}));

        final char CHAR_ZERO = 0;
        final char CHAR_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE).where(a -> true), new char[]{Character.MIN_VALUE, CHAR_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE).where(a -> true), new char[]{Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE).where(a -> true), new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}));

        final float FLOAT_ZERO = 0;
        final float FLOAT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE).where(a -> true), new float[]{Float.MIN_VALUE, FLOAT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE).where(a -> true), new float[]{Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE).where(a -> true), new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}));

        final double DOUBLE_ZERO = 0;
        final double DOUBLE_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE).where(a -> true), new double[]{Double.MIN_VALUE, DOUBLE_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE).where(a -> true), new double[]{Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE).where(a -> true), new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}));

        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new Object[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new Object[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO))));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE))));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE))));

        assertFalse(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof Collection);
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Arrays.asList(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Arrays.asList(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true), new Object()));
    }

    @Test
    public void testEqualsIEnumerable() {
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)), new boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)), new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)), new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));

        final byte BYTE_ZERO = 0;
        final byte BYTE_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE)), new byte[]{Byte.MIN_VALUE, BYTE_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE)), new byte[]{Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE)), new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}));

        final short SHORT_ZERO = 0;
        final short SHORT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE)), new short[]{Short.MIN_VALUE, SHORT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE)), new short[]{Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE)), new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}));

        final int INT_ZERO = 0;
        final int INT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new int[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new int[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        final long LONG_ZERO = 0;
        final long LONG_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE)), new long[]{Long.MIN_VALUE, LONG_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE)), new long[]{Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE)), new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}));

        final char CHAR_ZERO = 0;
        final char CHAR_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE)), new char[]{Character.MIN_VALUE, CHAR_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE)), new char[]{Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE)), new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}));

        final float FLOAT_ZERO = 0;
        final float FLOAT_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE)), new float[]{Float.MIN_VALUE, FLOAT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE)), new float[]{Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE)), new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}));

        final double DOUBLE_ZERO = 0;
        final double DOUBLE_ONE = 1;
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE)), new double[]{Double.MIN_VALUE, DOUBLE_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE)), new double[]{Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE)), new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}));

        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new Object[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new Object[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a)));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true)));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO))));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE))));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE))));

        assertFalse(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof Collection);
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Arrays.asList(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Arrays.asList(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)), new Object()));
    }

    @Test
    public void testEqualsCollection() {
        assertFalse(Values.equals(Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), new boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(Values.equals(Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(Values.equals(Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));

        final byte BYTE_ZERO = 0;
        final byte BYTE_ONE = 1;
        assertFalse(Values.equals(Arrays.asList(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE), new byte[]{Byte.MIN_VALUE, BYTE_ZERO}));
        assertFalse(Values.equals(Arrays.asList(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE), new byte[]{Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE}));
        assertTrue(Values.equals(Arrays.asList(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE), new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}));

        final short SHORT_ZERO = 0;
        final short SHORT_ONE = 1;
        assertFalse(Values.equals(Arrays.asList(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE), new short[]{Short.MIN_VALUE, SHORT_ZERO}));
        assertFalse(Values.equals(Arrays.asList(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE), new short[]{Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE}));
        assertTrue(Values.equals(Arrays.asList(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE), new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}));

        final int INT_ZERO = 0;
        final int INT_ONE = 1;
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new int[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new int[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        final long LONG_ZERO = 0;
        final long LONG_ONE = 1;
        assertFalse(Values.equals(Arrays.asList(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE), new long[]{Long.MIN_VALUE, LONG_ZERO}));
        assertFalse(Values.equals(Arrays.asList(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE), new long[]{Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE}));
        assertTrue(Values.equals(Arrays.asList(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE), new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}));

        final char CHAR_ZERO = 0;
        final char CHAR_ONE = 1;
        assertFalse(Values.equals(Arrays.asList(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE), new char[]{Character.MIN_VALUE, CHAR_ZERO}));
        assertFalse(Values.equals(Arrays.asList(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE), new char[]{Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE}));
        assertTrue(Values.equals(Arrays.asList(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE), new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}));

        final float FLOAT_ZERO = 0;
        final float FLOAT_ONE = 1;
        assertFalse(Values.equals(Arrays.asList(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE), new float[]{Float.MIN_VALUE, FLOAT_ZERO}));
        assertFalse(Values.equals(Arrays.asList(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE), new float[]{Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE}));
        assertTrue(Values.equals(Arrays.asList(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE), new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}));

        final double DOUBLE_ZERO = 0;
        final double DOUBLE_ONE = 1;
        assertFalse(Values.equals(Arrays.asList(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE), new double[]{Double.MIN_VALUE, DOUBLE_ZERO}));
        assertFalse(Values.equals(Arrays.asList(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE), new double[]{Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE}));
        assertTrue(Values.equals(Arrays.asList(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE), new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}));

        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a)));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true)));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO))));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE))));
        assertTrue(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE))));

        assertFalse(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof Collection);
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Arrays.asList(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Arrays.asList(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object()));
    }

    @Test
    public void testEqualsIterable() {
        assertFalse(Values.equals(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), new boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(Values.equals(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(Values.equals(new ArrayIterable<>(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE), new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));

        final byte BYTE_ZERO = 0;
        final byte BYTE_ONE = 1;
        assertFalse(Values.equals(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE), new byte[]{Byte.MIN_VALUE, BYTE_ZERO}));
        assertFalse(Values.equals(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE), new byte[]{Byte.MIN_VALUE, BYTE_ONE, Byte.MAX_VALUE}));
        assertTrue(Values.equals(new ArrayIterable<>(Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE), new byte[]{Byte.MIN_VALUE, BYTE_ZERO, Byte.MAX_VALUE}));

        final short SHORT_ZERO = 0;
        final short SHORT_ONE = 1;
        assertFalse(Values.equals(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE), new short[]{Short.MIN_VALUE, SHORT_ZERO}));
        assertFalse(Values.equals(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE), new short[]{Short.MIN_VALUE, SHORT_ONE, Short.MAX_VALUE}));
        assertTrue(Values.equals(new ArrayIterable<>(Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE), new short[]{Short.MIN_VALUE, SHORT_ZERO, Short.MAX_VALUE}));

        final int INT_ZERO = 0;
        final int INT_ONE = 1;
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new int[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new int[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new int[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        final long LONG_ZERO = 0;
        final long LONG_ONE = 1;
        assertFalse(Values.equals(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE), new long[]{Long.MIN_VALUE, LONG_ZERO}));
        assertFalse(Values.equals(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE), new long[]{Long.MIN_VALUE, LONG_ONE, Long.MAX_VALUE}));
        assertTrue(Values.equals(new ArrayIterable<>(Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE), new long[]{Long.MIN_VALUE, LONG_ZERO, Long.MAX_VALUE}));

        final char CHAR_ZERO = 0;
        final char CHAR_ONE = 1;
        assertFalse(Values.equals(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE), new char[]{Character.MIN_VALUE, CHAR_ZERO}));
        assertFalse(Values.equals(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE), new char[]{Character.MIN_VALUE, CHAR_ONE, Character.MAX_VALUE}));
        assertTrue(Values.equals(new ArrayIterable<>(Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE), new char[]{Character.MIN_VALUE, CHAR_ZERO, Character.MAX_VALUE}));

        final float FLOAT_ZERO = 0;
        final float FLOAT_ONE = 1;
        assertFalse(Values.equals(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE), new float[]{Float.MIN_VALUE, FLOAT_ZERO}));
        assertFalse(Values.equals(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE), new float[]{Float.MIN_VALUE, FLOAT_ONE, Float.MAX_VALUE}));
        assertTrue(Values.equals(new ArrayIterable<>(Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE), new float[]{Float.MIN_VALUE, FLOAT_ZERO, Float.MAX_VALUE}));

        final double DOUBLE_ZERO = 0;
        final double DOUBLE_ONE = 1;
        assertFalse(Values.equals(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE), new double[]{Double.MIN_VALUE, DOUBLE_ZERO}));
        assertFalse(Values.equals(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE), new double[]{Double.MIN_VALUE, DOUBLE_ONE, Double.MAX_VALUE}));
        assertTrue(Values.equals(new ArrayIterable<>(Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE), new double[]{Double.MIN_VALUE, DOUBLE_ZERO, Double.MAX_VALUE}));

        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object[]{Integer.MIN_VALUE, INT_ZERO}));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object[]{Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE}));
        assertTrue(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object[]{Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE}));

        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO) instanceof ICollection);
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a) instanceof IIListProvider);
        assertEquals(2, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a))._getCount(true));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).select(a -> a)));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).select(a -> a)));
        assertTrue(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).select(a -> a)));

        assertFalse(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof ICollection);
        assertTrue(Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true) instanceof IIListProvider);
        assertEquals(-1, ((IIListProvider) Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true))._getCount(true));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO).where(a -> true)));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE).where(a -> true)));
        assertTrue(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE).where(a -> true)));

        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof ICollection);
        assertFalse(Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)) instanceof IIListProvider);
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO))));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE))));
        assertTrue(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Linq.asEnumerable(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE))));

        assertFalse(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof IEnumerable);
        assertTrue(Arrays.asList(Integer.MIN_VALUE, INT_ZERO) instanceof Collection);
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Arrays.asList(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Arrays.asList(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), Arrays.asList(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO)));
        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new ArrayIterable<>(Integer.MIN_VALUE, INT_ONE, Integer.MAX_VALUE)));
        assertTrue(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE)));

        assertFalse(Values.equals(new ArrayIterable<>(Integer.MIN_VALUE, INT_ZERO, Integer.MAX_VALUE), new Object()));
    }

    @Test
    public void testEqualsMap() {
        assertFalse(Values.equals(new HashMap<>(), null));
        assertTrue(Values.equals(new HashMap<>(), new LinkedHashMap<>()));

        Map<Object, Object> a = new HashMap<>();
        a.put(1, "hello");
        a.put("2", "world");
        Map<Object, Object> b = new LinkedHashMap<>();
        b.put(1, "hello");
        assertFalse(Values.equals(a, b));

        Map<Object, Object> aa = new HashMap<>();
        aa.put(1, "hello");
        aa.put("2", "world");
        Map<Object, Object> bb = new LinkedHashMap<>();
        bb.put(1, "hello");
        bb.put("2", "world--");
        assertFalse(Values.equals(aa, bb));

        Map<Object, Object> aaa = new HashMap<>();
        aaa.put(1, "hello");
        aaa.put("2", "world");
        Map<Object, Object> bbb = new LinkedHashMap<>();
        bbb.put(1, "hello");
        bbb.put("2", "world");
        assertTrue(Values.equals(aaa, bbb));

        assertFalse(Values.equals(new HashMap<>(), new Object()));
    }

    @Test
    public void testJDKObject() {
        assertTrue(Values.equals("123", "123"));
        assertFalse(Values.equals("123", "abc"));
        assertFalse(Values.equals("123", 123));
        assertFalse(Values.equals("123", null));
    }

    @Test
    public void testUserObject() {
        assertFalse(Values.equals(new TwoFieldBean("abc", 123), null));
        assertFalse(Values.equals(new TwoFieldBean("abc", 123), new OneFieldBean("abc")));
        assertTrue(Values.equals(new TwoFieldBean("abc", 123), new TwoFieldBean("abc", 123)));
        assertTrue(Values.equals(this.emptyMoreFieldBeanExpected, this.emptyMoreFieldBean));
        assertTrue(Values.equals(this.moreFieldBeanExpected, this.moreFieldBean));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Values.hashCode(null));
        assertEquals(1, Values.hashCode(new int[0]));

        assertEquals(Values.hashCode(this.noFieldBeanExpected), Values.hashCode(this.noFieldBean));
        assertEquals(Values.hashCode(this.oneFieldBeanExpected), Values.hashCode(this.oneFieldBean));
        assertEquals(Values.hashCode(this.twoFieldBeanExpected), Values.hashCode(this.twoFieldBean));
        assertEquals(Values.hashCode(this.nullMoreFieldBeanExpected), Values.hashCode(this.nullMoreFieldBean));
        assertEquals(Values.hashCode(this.emptyMoreFieldBeanExpected), Values.hashCode(this.emptyMoreFieldBean));
        assertEquals(Values.hashCode(this.moreFieldBeanExpected), Values.hashCode(this.moreFieldBean));
    }

    @Test
    public void testHashCodeDecimal() {
        assertEquals(Values.hashCode(m("1.0")), Values.hashCode(m("1.00000")));
        assertEquals(Values.hashCode(m("-1.0")), Values.hashCode(m("-1.00000")));
    }

    @Test
    public void testHashCodeAll() {
        assertEquals(Values.hashCode(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}), Values.hashCode(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertEquals(Values.hashCode(new Byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}), Values.hashCode(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}));
        assertEquals(Values.hashCode(new Short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE}), Values.hashCode(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE}));
        assertEquals(Values.hashCode(new Integer[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}), Values.hashCode(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}));
        assertEquals(Values.hashCode(new Long[]{Long.MIN_VALUE, 0L, Long.MAX_VALUE}), Values.hashCode(new long[]{Long.MIN_VALUE, 0L, Long.MAX_VALUE}));
        assertEquals(Values.hashCode(new Character[]{Character.MIN_VALUE, 0, Character.MAX_VALUE}), Values.hashCode(new char[]{Character.MIN_VALUE, 0, Character.MAX_VALUE}));
        assertEquals(Values.hashCode(new Float[]{Float.MIN_VALUE, 0f, Float.MAX_VALUE}), Values.hashCode(new float[]{Float.MIN_VALUE, 0f, Float.MAX_VALUE}));
        assertEquals(Values.hashCode(new Double[]{Double.MIN_VALUE, 0d, Double.MAX_VALUE}), Values.hashCode(new double[]{Double.MIN_VALUE, 0d, Double.MAX_VALUE}));

        assertEquals(Values.hashCode(new Object[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}), Values.hashCode(new Integer[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}));
        assertEquals(Values.hashCode(new Object[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}), Values.hashCode(Linq.asEnumerable(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)));
        assertEquals(Values.hashCode(new Object[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}), Values.hashCode(new ArrayIterable<>(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)));

        Map<Object, Object> a = new LinkedHashMap<>();
        a.put("2", "world");
        a.put("1", "hello");
        Map<Object, Object> b = new LinkedHashMap<>();
        b.put("1", "hello");
        b.put("2", "world");
        assertEquals(Values.hashCode(a), Values.hashCode(b));

        assertEquals("abc".hashCode(), Values.hashCode("abc"));

        assertEquals(1, Values.hashCode(this.noFieldBean));
    }

    @Test
    public void testToString() {
        assertEquals("null", Values.toString(null));

        assertEquals(Values.toString(this.noFieldBeanExpected), Values.toString(this.noFieldBean));
        assertEquals("NoFieldBean{}", Values.toString(this.noFieldBean));

        assertEquals(Values.toString(this.oneFieldBeanExpected), Values.toString(this.oneFieldBean));
        assertEquals("OneFieldBean{Name='Robert'}", Values.toString(this.oneFieldBean));

        assertEquals(Values.toString(this.twoFieldBeanExpected), Values.toString(this.twoFieldBean));
        assertEquals("TwoFieldBean{Name='Robert', Score=45}", Values.toString(this.twoFieldBean));

        assertEquals(Values.toString(this.nullMoreFieldBeanExpected), Values.toString(this.nullMoreFieldBean));
        assertEquals("MoreFieldBean{nullField=null, decimal=null, boolArr=null, byteArr=null, shortArr=null, intArr=null, longArr=null, charArr=null, floatArr=null, doubleArr=null, objArr=null, objArr2=null, iCollection=null, listProvider=null, enumerable=null, collection=null, iterable=null, map=null, bean=null}", Values.toString(this.nullMoreFieldBean));
        assertEquals(Values.toString(this.emptyMoreFieldBeanExpected), Values.toString(this.emptyMoreFieldBean));
        assertEquals("MoreFieldBean{nullField=null, decimal=0.000000, boolArr=[], byteArr=[], shortArr=[], intArr=[], longArr=[], charArr=[], floatArr=[], doubleArr=[], objArr=[], objArr2=[], iCollection=[], listProvider=[], enumerable=[], collection=[], iterable=[], map=LinkedHashMap{}, bean=TwoFieldBean{Name='Tim', Score=1239}}", Values.toString(this.emptyMoreFieldBean));
        assertEquals(Values.toString(this.moreFieldBeanExpected), Values.toString(this.moreFieldBean));
        assertEquals("MoreFieldBean{nullField=null, decimal=123.456000, boolArr=[true, false, true], byteArr=[-128, 2, 126, 127], shortArr=[-32768, 2, 3, 32767], intArr=[-2147483648, 2, 3, 2147483647], longArr=[-9223372036854775808, 2, 3, 9223372036854775807], charArr=[\u0000, #, !, $, a], floatArr=[-Infinity, Infinity, 1.4E-45, 3.4028235E38], doubleArr=[-Infinity, Infinity, 4.9E-324, 1.7976931348623157E308], objArr=[null, '!@#$%^&*()_+', 123.456000, 9999999.100000, NoFieldBean{}], objArr2=[null, 355.990000], iCollection=[null, '', '', '你好世界'], listProvider=['null_suffix', '_suffix', '_suffix', '你好世界_suffix'], enumerable=['', '', '你好世界'], collection=[null, '', '', '你好世界'], iterable=[null, '', '', '你好世界'], map=HashMap{null='hello', ''=OneFieldBean{Name='世界'}, 'a'=1, 'key'=123.000000}, bean=TwoFieldBean{Name='Tim', Score=1239}}", Values.toString(this.moreFieldBean));
    }

    @Test
    public void testToStringString() {
        assertEquals("'abc'", Values.toString("abc"));
    }

    @Test
    public void testToStringDecimal() {
        assertEquals(Values.toString(m("1.0")), Values.toString(m("1.00000")));
        assertEquals(Values.toString(m("-1.0")), Values.toString(m("-1.00000")));
    }

    @Test
    public void testToStringAll() {
        assertEquals("[true, false, true]", Values.toString(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertEquals("[-128, 0, 127]", Values.toString(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}));
        assertEquals("[-32768, 0, 32767]", Values.toString(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE}));
        assertEquals("[-2147483648, 0, 2147483647]", Values.toString(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}));
        assertEquals("[-9223372036854775808, 0, 9223372036854775807]", Values.toString(new long[]{Long.MIN_VALUE, 0L, Long.MAX_VALUE}));
        assertEquals("[a, ~, $]", Values.toString(new char[]{'a', '~', '$'}));
        assertEquals("[1.4E-45, 0.0, 3.4028235E38]", Values.toString(new float[]{Float.MIN_VALUE, 0f, Float.MAX_VALUE}));
        assertEquals("[4.9E-324, 0.0, 1.7976931348623157E308]", Values.toString(new double[]{Double.MIN_VALUE, 0d, Double.MAX_VALUE}));

        assertEquals("[-2147483648, 0, 2147483647]", Values.toString(new Object[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}));
        assertEquals("[-2147483648, 0, 2147483647]", Values.toString(Linq.asEnumerable(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)));
        assertEquals("[-2147483648, 0, 2147483647]", Values.toString(new ArrayIterable<>(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)));

        Map<Object, Object> a = new LinkedHashMap<>();
        a.put("2", "world");
        a.put("1", "hello");
        Map<Object, Object> b = new LinkedHashMap<>();
        b.put("1", "hello");
        b.put("2", "world");
        assertEquals(Values.toString(a), Values.toString(b));
        assertEquals("LinkedHashMap{'1'='hello', '2'='world'}", Values.toString(a));

        assertEquals("'abc'", Values.toString("abc"));

        assertEquals("NoFieldBean{}", Values.toString(this.noFieldBean));
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
