package com.bestvike.linq.tuple;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2017/7/23.
 */
public class TupleTest {
    @Test
    public void testTuple1() {
        Tuple1<String> source = Tuple.create("1");
        Tuple1<String> target = Tuple.create("1");
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals("1".hashCode(), source.hashCode());
        Assert.assertEquals("(1)", source.toString());
    }

    @Test
    public void testTuple2() {
        Tuple2<String, String> source = Tuple.create("1", "2");
        Tuple2<String, String> target = Tuple.create("1", "2");
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2)", source.toString());
    }

    @Test
    public void testTuple3() {
        Tuple3<String, String, String> source = Tuple.create("1", "2", "3");
        Tuple3<String, String, String> target = Tuple.create("1", "2", "3");
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3)", source.toString());
    }

    @Test
    public void testTuple4() {
        Tuple4<String, String, String, String> source = Tuple.create("1", "2", "3", "4");
        Tuple4<String, String, String, String> target = Tuple.create("1", "2", "3", "4");
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4)", source.toString());
    }

    @Test
    public void testTuple5() {
        Tuple5<String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5");
        Tuple5<String, String, String, String, String> target = Tuple.create("1", "2", "3", "4", "5");
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5)", source.toString());
    }

    @Test
    public void testTuple6() {
        Tuple6<String, String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5", "6");
        Tuple6<String, String, String, String, String, String> target = Tuple.create("1", "2", "3", "4", "5", "6");
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5, 6)", source.toString());
    }

    @Test
    public void testTuple7() {
        Tuple7<String, String, String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5", "6", "7");
        Tuple7<String, String, String, String, String, String, String> target = Tuple.create("1", "2", "3", "4", "5", "6", "7");
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode(), "7".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5, 6, 7)", source.toString());
    }

    @Test
    public void testTuple8() {
        TupleMore<String, String, String, String, String, String, String, Tuple1<String>> source = Tuple.create("1", "2", "3", "4", "5", "6", "7", "8");
        TupleMore<String, String, String, String, String, String, String, Tuple1<String>> target = Tuple.create("1", "2", "3", "4", "5", "6", "7", "8");
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode(), "7".hashCode(), "8".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5, 6, 7, 8)", source.toString());
    }

    @Test
    public void testTupleMore() {
        TupleMore<String, String, String, String, String, String, String, TupleMore<String, String, String, String, String, String, String, Tuple2<String, String>>> source =
                new TupleMore<>("1", "2", "3", "4", "5", "6", "7",
                        new TupleMore<>("8", "9", "10", "11", "12", "13", "14", new Tuple2<>("15", "16")));
        TupleMore<String, String, String, String, String, String, String, TupleMore<String, String, String, String, String, String, String, Tuple2<String, String>>> target =
                new TupleMore<>("1", "2", "3", "4", "5", "6", "7",
                        new TupleMore<>("8", "9", "10", "11", "12", "13", "14", new Tuple2<>("15", "16")));
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("9".hashCode(), "10".hashCode(), "11".hashCode(), "12".hashCode(), "13".hashCode(), "14".hashCode(), "15".hashCode(), "16".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)", source.toString());
    }
}
