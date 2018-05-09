package com.bestvike.tuple;

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
        Assert.assertEquals(1, source.size());
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals("1".hashCode(), source.hashCode());
        Assert.assertEquals("(1)", source.toString());
        Assert.assertEquals("1", source.getItem1());
        Assert.assertEquals("1", source.get(0));
        try {
            Object obj = source.get(1);
            Assert.fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple2() {
        Tuple2<String, String> source = Tuple.create("1", "2");
        Tuple2<String, String> target = Tuple.create("1", "2");
        Assert.assertEquals(2, source.size());
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2)", source.toString());
        Assert.assertEquals("1", source.getItem1());
        Assert.assertEquals("2", source.getItem2());
        Assert.assertEquals("1", source.get(0));
        Assert.assertEquals("2", source.get(1));
        try {
            Object obj = source.get(3);
            Assert.fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple3() {
        Tuple3<String, String, String> source = Tuple.create("1", "2", "3");
        Tuple3<String, String, String> target = Tuple.create("1", "2", "3");
        Assert.assertEquals(3, source.size());
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3)", source.toString());
        Assert.assertEquals("1", source.getItem1());
        Assert.assertEquals("2", source.getItem2());
        Assert.assertEquals("3", source.getItem3());
        Assert.assertEquals("1", source.get(0));
        Assert.assertEquals("2", source.get(1));
        Assert.assertEquals("3", source.get(2));
        try {
            Object obj = source.get(3);
            Assert.fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple4() {
        Tuple4<String, String, String, String> source = Tuple.create("1", "2", "3", "4");
        Tuple4<String, String, String, String> target = Tuple.create("1", "2", "3", "4");
        Assert.assertEquals(4, source.size());
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4)", source.toString());
        Assert.assertEquals("1", source.getItem1());
        Assert.assertEquals("2", source.getItem2());
        Assert.assertEquals("3", source.getItem3());
        Assert.assertEquals("4", source.getItem4());
        Assert.assertEquals("1", source.get(0));
        Assert.assertEquals("2", source.get(1));
        Assert.assertEquals("3", source.get(2));
        Assert.assertEquals("4", source.get(3));
        try {
            Object obj = source.get(4);
            Assert.fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple5() {
        Tuple5<String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5");
        Tuple5<String, String, String, String, String> target = Tuple.create("1", "2", "3", "4", "5");
        Assert.assertEquals(5, source.size());
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5)", source.toString());
        Assert.assertEquals("1", source.getItem1());
        Assert.assertEquals("2", source.getItem2());
        Assert.assertEquals("3", source.getItem3());
        Assert.assertEquals("4", source.getItem4());
        Assert.assertEquals("5", source.getItem5());
        Assert.assertEquals("1", source.get(0));
        Assert.assertEquals("2", source.get(1));
        Assert.assertEquals("3", source.get(2));
        Assert.assertEquals("4", source.get(3));
        Assert.assertEquals("5", source.get(4));
        try {
            Object obj = source.get(5);
            Assert.fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple6() {
        Tuple6<String, String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5", "6");
        Tuple6<String, String, String, String, String, String> target = Tuple.create("1", "2", "3", "4", "5", "6");
        Assert.assertEquals(6, source.size());
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5, 6)", source.toString());
        Assert.assertEquals("1", source.getItem1());
        Assert.assertEquals("2", source.getItem2());
        Assert.assertEquals("3", source.getItem3());
        Assert.assertEquals("4", source.getItem4());
        Assert.assertEquals("5", source.getItem5());
        Assert.assertEquals("6", source.getItem6());
        Assert.assertEquals("1", source.get(0));
        Assert.assertEquals("2", source.get(1));
        Assert.assertEquals("3", source.get(2));
        Assert.assertEquals("4", source.get(3));
        Assert.assertEquals("5", source.get(4));
        Assert.assertEquals("6", source.get(5));
        try {
            Object obj = source.get(6);
            Assert.fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple7() {
        Tuple7<String, String, String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5", "6", "7");
        Tuple7<String, String, String, String, String, String, String> target = Tuple.create("1", "2", "3", "4", "5", "6", "7");
        Assert.assertEquals(7, source.size());
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode(), "7".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5, 6, 7)", source.toString());
        Assert.assertEquals("1", source.getItem1());
        Assert.assertEquals("2", source.getItem2());
        Assert.assertEquals("3", source.getItem3());
        Assert.assertEquals("4", source.getItem4());
        Assert.assertEquals("5", source.getItem5());
        Assert.assertEquals("6", source.getItem6());
        Assert.assertEquals("7", source.getItem7());
        Assert.assertEquals("1", source.get(0));
        Assert.assertEquals("2", source.get(1));
        Assert.assertEquals("3", source.get(2));
        Assert.assertEquals("4", source.get(3));
        Assert.assertEquals("5", source.get(4));
        Assert.assertEquals("6", source.get(5));
        Assert.assertEquals("7", source.get(6));
        try {
            Object obj = source.get(7);
            Assert.fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple8() {
        TupleMore<String, String, String, String, String, String, String, Tuple1<String>> source = Tuple.create("1", "2", "3", "4", "5", "6", "7", "8");
        TupleMore<String, String, String, String, String, String, String, Tuple1<String>> target = Tuple.create("1", "2", "3", "4", "5", "6", "7", "8");
        Assert.assertEquals(8, source.size());
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode(), "7".hashCode(), "8".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5, 6, 7, 8)", source.toString());
        Assert.assertEquals("1", source.getItem1());
        Assert.assertEquals("2", source.getItem2());
        Assert.assertEquals("3", source.getItem3());
        Assert.assertEquals("4", source.getItem4());
        Assert.assertEquals("5", source.getItem5());
        Assert.assertEquals("6", source.getItem6());
        Assert.assertEquals("7", source.getItem7());
        Assert.assertEquals(Tuple.create("8"), source.getRest());
        Assert.assertEquals("1", source.get(0));
        Assert.assertEquals("2", source.get(1));
        Assert.assertEquals("3", source.get(2));
        Assert.assertEquals("4", source.get(3));
        Assert.assertEquals("5", source.get(4));
        Assert.assertEquals("6", source.get(5));
        Assert.assertEquals("7", source.get(6));
        Assert.assertEquals("8", source.get(7));
        try {
            Object obj = source.get(8);
            Assert.fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTupleMore() {
        TupleMore<String, String, String, String, String, String, String, TupleMore<String, String, String, String, String, String, String, Tuple2<String, String>>> source =
                new TupleMore<>("1", "2", "3", "4", "5", "6", "7",
                        new TupleMore<>("8", "9", "10", "11", "12", "13", "14", new Tuple2<>("15", "16")));
        TupleMore<String, String, String, String, String, String, String, TupleMore<String, String, String, String, String, String, String, Tuple2<String, String>>> target =
                new TupleMore<>("1", "2", "3", "4", "5", "6", "7",
                        new TupleMore<>("8", "9", "10", "11", "12", "13", "14", new Tuple2<>("15", "16")));
        Assert.assertEquals(16, source.size());
        Assert.assertFalse(source == target);
        Assert.assertEquals(source, target);
        Assert.assertEquals(source.compareTo(target), 0);
        Assert.assertEquals(Tuple.combineHashCodes("9".hashCode(), "10".hashCode(), "11".hashCode(), "12".hashCode(), "13".hashCode(), "14".hashCode(), "15".hashCode(), "16".hashCode()), source.hashCode());
        Assert.assertEquals("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)", source.toString());
        Assert.assertEquals("1", source.getItem1());
        Assert.assertEquals("2", source.getItem2());
        Assert.assertEquals("3", source.getItem3());
        Assert.assertEquals("4", source.getItem4());
        Assert.assertEquals("5", source.getItem5());
        Assert.assertEquals("6", source.getItem6());
        Assert.assertEquals("7", source.getItem7());
        Assert.assertEquals(new TupleMore<>("8", "9", "10", "11", "12", "13", "14", new Tuple2<>("15", "16")), source.getRest());
        Assert.assertEquals("1", source.get(0));
        Assert.assertEquals("2", source.get(1));
        Assert.assertEquals("3", source.get(2));
        Assert.assertEquals("4", source.get(3));
        Assert.assertEquals("5", source.get(4));
        Assert.assertEquals("6", source.get(5));
        Assert.assertEquals("7", source.get(6));
        Assert.assertEquals("8", source.get(7));
        Assert.assertEquals("9", source.get(8));
        Assert.assertEquals("10", source.get(9));
        Assert.assertEquals("11", source.get(10));
        Assert.assertEquals("12", source.get(11));
        Assert.assertEquals("13", source.get(12));
        Assert.assertEquals("14", source.get(13));
        Assert.assertEquals("15", source.get(14));
        Assert.assertEquals("16", source.get(15));
        try {
            Object obj = source.get(16);
            Assert.fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }
}
