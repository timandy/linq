package com.bestvike.linq.util;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-06-20.
 */
public class FormatterTest extends TestCase {
    private Formatter formatter;
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

    {
        this.formatter = new Formatter();
        this.formatter.setNullString("NULL");
        this.formatter.setStringQuotes("\"");
        this.formatter.setDecimalWithScale(false);
        this.formatter.setDecimalScale(0);
        this.formatter.setDecimalRounding(RoundingMode.CEILING);
        this.formatter.setArrayTypeStyle(FormatTypeStyle.SimpleName);
        this.formatter.setArrayPrefix("[ ");
        this.formatter.setArraySuffix(" ]");
        this.formatter.setArrayEmpty("[ ]");
        this.formatter.setArrayValueSeparator(",");
        this.formatter.setMapTypeStyle(null);
        this.formatter.setMapPrefix("( ");
        this.formatter.setMapSuffix(" )");
        this.formatter.setMapEmpty("( )");
        this.formatter.setMapEntrySeparator(",");
        this.formatter.setMapKeyValueSeparator(":");
        this.formatter.setObjectTypeStyle(FormatTypeStyle.FullName);
        this.formatter.setObjectPrefix("( ");
        this.formatter.setObjectSuffix(" )");
        this.formatter.setObjectEmpty("( )");
        this.formatter.setObjectFieldSeparator(",");
        this.formatter.setObjectFieldValueSeparator(":");
        //
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
    public void AssertGetters() {
        assertEquals("NULL", this.formatter.getNullString());
        assertEquals("\"", this.formatter.getStringQuotes());
        assertEquals(false, this.formatter.isDecimalWithScale());
        assertEquals(0, this.formatter.getDecimalScale());
        assertEquals(RoundingMode.CEILING, this.formatter.getDecimalRounding());
        assertEquals(FormatTypeStyle.SimpleName, this.formatter.getArrayTypeStyle());
        assertEquals("[ ", this.formatter.getArrayPrefix());
        assertEquals(" ]", this.formatter.getArraySuffix());
        assertEquals("[ ]", this.formatter.getArrayEmpty());
        assertEquals(",", this.formatter.getArrayValueSeparator());
        assertEquals(null, this.formatter.getMapTypeStyle());
        assertEquals("( ", this.formatter.getMapPrefix());
        assertEquals(" )", this.formatter.getMapSuffix());
        assertEquals("( )", this.formatter.getMapEmpty());
        assertEquals(",", this.formatter.getMapEntrySeparator());
        assertEquals(":", this.formatter.getMapKeyValueSeparator());
        assertEquals(FormatTypeStyle.FullName, this.formatter.getObjectTypeStyle());
        assertEquals("( ", this.formatter.getObjectPrefix());
        assertEquals(" )", this.formatter.getObjectSuffix());
        assertEquals("( )", this.formatter.getObjectEmpty());
        assertEquals(",", this.formatter.getObjectFieldSeparator());
        assertEquals(":", this.formatter.getObjectFieldValueSeparator());
    }

    @Test
    public void DefaultToString() {
        assertEquals("null", Formatter.DEFAULT.format(null));

        assertEquals(Formatter.DEFAULT.format(this.noFieldBeanExpected), Formatter.DEFAULT.format(this.noFieldBean));
        assertEquals("NoFieldBean{}", Formatter.DEFAULT.format(this.noFieldBean));

        assertEquals(Formatter.DEFAULT.format(this.oneFieldBeanExpected), Formatter.DEFAULT.format(this.oneFieldBean));
        assertEquals("OneFieldBean{Name='Robert'}", Formatter.DEFAULT.format(this.oneFieldBean));

        assertEquals(Formatter.DEFAULT.format(this.twoFieldBeanExpected), Formatter.DEFAULT.format(this.twoFieldBean));
        assertEquals("TwoFieldBean{Name='Robert', Score=45}", Formatter.DEFAULT.format(this.twoFieldBean));

        assertEquals(Formatter.DEFAULT.format(this.nullMoreFieldBeanExpected), Formatter.DEFAULT.format(this.nullMoreFieldBean));
        assertEquals("MoreFieldBean{nullField=null, decimal=null, boolArr=null, byteArr=null, shortArr=null, intArr=null, longArr=null, charArr=null, floatArr=null, doubleArr=null, objArr=null, objArr2=null, iCollection=null, listProvider=null, enumerable=null, collection=null, iterable=null, map=null, bean=null}", Formatter.DEFAULT.format(this.nullMoreFieldBean));
        assertEquals(Formatter.DEFAULT.format(this.emptyMoreFieldBeanExpected), Formatter.DEFAULT.format(this.emptyMoreFieldBean));
        assertEquals("MoreFieldBean{nullField=null, decimal=0.000000, boolArr=[], byteArr=[], shortArr=[], intArr=[], longArr=[], charArr=[], floatArr=[], doubleArr=[], objArr=[], objArr2=[], iCollection=[], listProvider=[], enumerable=[], collection=[], iterable=[], map=LinkedHashMap{}, bean=TwoFieldBean{Name='Tim', Score=1239}}", Formatter.DEFAULT.format(this.emptyMoreFieldBean));
        assertEquals(Formatter.DEFAULT.format(this.moreFieldBeanExpected), Formatter.DEFAULT.format(this.moreFieldBean));
        assertEquals("MoreFieldBean{nullField=null, decimal=123.456000, boolArr=[true, false, true], byteArr=[-128, 2, 126, 127], shortArr=[-32768, 2, 3, 32767], intArr=[-2147483648, 2, 3, 2147483647], longArr=[-9223372036854775808, 2, 3, 9223372036854775807], charArr=[\u0000, #, !, $, a], floatArr=[-Infinity, Infinity, 1.4E-45, 3.4028235E38], doubleArr=[-Infinity, Infinity, 4.9E-324, 1.7976931348623157E308], objArr=[null, '!@#$%^&*()_+', 123.456000, 9999999.100000, NoFieldBean{}], objArr2=[null, 355.990000], iCollection=[null, '', '', '你好世界'], listProvider=['null_suffix', '_suffix', '_suffix', '你好世界_suffix'], enumerable=['', '', '你好世界'], collection=[null, '', '', '你好世界'], iterable=[null, '', '', '你好世界'], map=HashMap{null='hello', ''=OneFieldBean{Name='世界'}, 'a'=1, 'key'=123.000000}, bean=TwoFieldBean{Name='Tim', Score=1239}}", Formatter.DEFAULT.format(this.moreFieldBean));
    }

    @Test
    public void DefaultToStringString() {
        assertEquals("'abc'", Formatter.DEFAULT.format("abc"));
    }

    @Test
    public void DefaultToStringDecimal() {
        assertEquals(Formatter.DEFAULT.format(m("1.0")), Formatter.DEFAULT.format(m("1.00000")));
        assertEquals(Formatter.DEFAULT.format(m("-1.0")), Formatter.DEFAULT.format(m("-1.00000")));
    }

    @Test
    public void DefaultToStringAll() {
        assertEquals("[true, false, true]", Formatter.DEFAULT.format(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertEquals("[-128, 0, 127]", Formatter.DEFAULT.format(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}));
        assertEquals("[-32768, 0, 32767]", Formatter.DEFAULT.format(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE}));
        assertEquals("[-2147483648, 0, 2147483647]", Formatter.DEFAULT.format(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}));
        assertEquals("[-9223372036854775808, 0, 9223372036854775807]", Formatter.DEFAULT.format(new long[]{Long.MIN_VALUE, 0L, Long.MAX_VALUE}));
        assertEquals("[a, ~, $]", Formatter.DEFAULT.format(new char[]{'a', '~', '$'}));
        assertEquals("[1.4E-45, 0.0, 3.4028235E38]", Formatter.DEFAULT.format(new float[]{Float.MIN_VALUE, 0f, Float.MAX_VALUE}));
        assertEquals("[4.9E-324, 0.0, 1.7976931348623157E308]", Formatter.DEFAULT.format(new double[]{Double.MIN_VALUE, 0d, Double.MAX_VALUE}));

        assertEquals("[-2147483648, 0, 2147483647]", Formatter.DEFAULT.format(new Object[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}));
        assertEquals("[-2147483648, 0, 2147483647]", Formatter.DEFAULT.format(Linq.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)));
        assertEquals("[-2147483648, 0, 2147483647]", Formatter.DEFAULT.format(new ArrayIterable<>(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)));

        Map<Object, Object> a = new LinkedHashMap<>();
        a.put("2", "world");
        a.put("1", "hello");
        Map<Object, Object> b = new LinkedHashMap<>();
        b.put("1", "hello");
        b.put("2", "world");
        assertEquals(Formatter.DEFAULT.format(a), Formatter.DEFAULT.format(b));
        assertEquals("LinkedHashMap{'1'='hello', '2'='world'}", Formatter.DEFAULT.format(a));

        assertEquals("'abc'", Formatter.DEFAULT.format("abc"));

        assertEquals("NoFieldBean{}", Formatter.DEFAULT.format(this.noFieldBean));
    }

    @Test
    public void CustomToString() {
        assertEquals("NULL", this.formatter.format(null));

        assertEquals(this.formatter.format(this.noFieldBeanExpected), this.formatter.format(this.noFieldBean));
        assertEquals("com.bestvike.linq.util.FormatterTest$NoFieldBean( )", this.formatter.format(this.noFieldBean));

        assertEquals(this.formatter.format(this.oneFieldBeanExpected), this.formatter.format(this.oneFieldBean));
        assertEquals("com.bestvike.linq.util.FormatterTest$OneFieldBean( Name:\"Robert\" )", this.formatter.format(this.oneFieldBean));

        assertEquals(this.formatter.format(this.twoFieldBeanExpected), this.formatter.format(this.twoFieldBean));
        assertEquals("com.bestvike.linq.util.FormatterTest$TwoFieldBean( Name:\"Robert\",Score:45 )", this.formatter.format(this.twoFieldBean));

        assertEquals(this.formatter.format(this.nullMoreFieldBeanExpected), this.formatter.format(this.nullMoreFieldBean));
        assertEquals("com.bestvike.linq.util.FormatterTest$MoreFieldBean( nullField:NULL,decimal:NULL,boolArr:NULL,byteArr:NULL,shortArr:NULL,intArr:NULL,longArr:NULL,charArr:NULL,floatArr:NULL,doubleArr:NULL,objArr:NULL,objArr2:NULL,iCollection:NULL,listProvider:NULL,enumerable:NULL,collection:NULL,iterable:NULL,map:NULL,bean:NULL )", this.formatter.format(this.nullMoreFieldBean));
        assertNotEquals(this.formatter.format(this.emptyMoreFieldBeanExpected), this.formatter.format(this.emptyMoreFieldBean));
        assertEquals("com.bestvike.linq.util.FormatterTest$MoreFieldBean( nullField:NULL,decimal:0,boolArr:boolean[][ ],byteArr:byte[][ ],shortArr:short[][ ],intArr:int[][ ],longArr:long[][ ],charArr:char[][ ],floatArr:float[][ ],doubleArr:double[][ ],objArr:Object[][ ],objArr2:BigDecimal[][ ],iCollection:GenericArrayEnumerable[ ],listProvider:EmptyPartition[ ],enumerable:EmptyPartition[ ],collection:EmptyList[ ],iterable:ArrayIterable[ ],map:( ),bean:com.bestvike.linq.util.FormatterTest$TwoFieldBean( Name:\"Tim\",Score:1239 ) )", this.formatter.format(this.emptyMoreFieldBean));
        assertNotEquals(this.formatter.format(this.moreFieldBeanExpected), this.formatter.format(this.moreFieldBean));
        assertEquals("com.bestvike.linq.util.FormatterTest$MoreFieldBean( nullField:NULL,decimal:123.456,boolArr:boolean[][ true,false,true ],byteArr:byte[][ -128,2,126,127 ],shortArr:short[][ -32768,2,3,32767 ],intArr:int[][ -2147483648,2,3,2147483647 ],longArr:long[][ -9223372036854775808,2,3,9223372036854775807 ],charArr:char[][ \u0000,#,!,$,a ],floatArr:float[][ -Infinity,Infinity,1.4E-45,3.4028235E38 ],doubleArr:double[][ -Infinity,Infinity,4.9E-324,1.7976931348623157E308 ],objArr:Object[][ NULL,\"!@#$%^&*()_+\",123.456,9999999.099999999,com.bestvike.linq.util.FormatterTest$NoFieldBean( ) ],objArr2:BigDecimal[][ NULL,355.99 ],iCollection:GenericArrayEnumerable[ NULL,\"\",\"\",\"你好世界\" ],listProvider:SelectArrayIterator[ \"null_suffix\",\"_suffix\",\"_suffix\",\"你好世界_suffix\" ],enumerable:WhereArrayIterator[ \"\",\"\",\"你好世界\" ],collection:ArrayList[ NULL,\"\",\"\",\"你好世界\" ],iterable:ArrayIterable[ NULL,\"\",\"\",\"你好世界\" ],map:( NULL:\"hello\",\"\":com.bestvike.linq.util.FormatterTest$OneFieldBean( Name:\"世界\" ),\"a\":1,\"key\":123.0 ),bean:com.bestvike.linq.util.FormatterTest$TwoFieldBean( Name:\"Tim\",Score:1239 ) )", this.formatter.format(this.moreFieldBean));
    }

    @Test
    public void CustomToStringString() {
        assertEquals("\"abc\"", this.formatter.format("abc"));
    }

    @Test
    public void CustomToStringDecimal() {
        assertEquals("1.00000", this.formatter.format(m("1.00000")));
        assertEquals("-1.00000", this.formatter.format(m("-1.00000")));
    }

    @Test
    public void CustomToStringAll() {
        assertEquals("boolean[][ true,false,true ]", this.formatter.format(new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertEquals("byte[][ -128,0,127 ]", this.formatter.format(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}));
        assertEquals("short[][ -32768,0,32767 ]", this.formatter.format(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE}));
        assertEquals("int[][ -2147483648,0,2147483647 ]", this.formatter.format(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}));
        assertEquals("long[][ -9223372036854775808,0,9223372036854775807 ]", this.formatter.format(new long[]{Long.MIN_VALUE, 0L, Long.MAX_VALUE}));
        assertEquals("char[][ a,~,$ ]", this.formatter.format(new char[]{'a', '~', '$'}));
        assertEquals("float[][ 1.4E-45,0.0,3.4028235E38 ]", this.formatter.format(new float[]{Float.MIN_VALUE, 0f, Float.MAX_VALUE}));
        assertEquals("double[][ 4.9E-324,0.0,1.7976931348623157E308 ]", this.formatter.format(new double[]{Double.MIN_VALUE, 0d, Double.MAX_VALUE}));

        assertEquals("Object[][ -2147483648,0,2147483647 ]", this.formatter.format(new Object[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}));
        assertEquals("GenericArrayEnumerable[ -2147483648,0,2147483647 ]", this.formatter.format(Linq.of(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)));
        assertEquals("ArrayIterable[ -2147483648,0,2147483647 ]", this.formatter.format(new ArrayIterable<>(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)));

        Map<Object, Object> a = new LinkedHashMap<>();
        a.put("2", "world");
        a.put("1", "hello");
        Map<Object, Object> b = new LinkedHashMap<>();
        b.put("1", "hello");
        b.put("2", "world");
        assertEquals(this.formatter.format(a), this.formatter.format(b));
        assertEquals("( \"1\":\"hello\",\"2\":\"world\" )", this.formatter.format(a));

        assertEquals("\"abc\"", this.formatter.format("abc"));

        assertEquals("com.bestvike.linq.util.FormatterTest$NoFieldBean( )", this.formatter.format(this.noFieldBean));
    }

    @Test
    public void ShouldNotUseHashCodeSortMap() {
        String[] source = new String[]{"Aa", "BB"};
        assertEquals(Linq.of("Aa", "BB"), Linq.of(source).orderBy(String::hashCode));

        String[] source2 = new String[]{"BB", "Aa"};
        assertEquals(Linq.of("BB", "Aa"), Linq.of(source2).orderBy(String::hashCode));
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
}
