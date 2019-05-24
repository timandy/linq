package com.bestvike.tuple;

import com.bestvike.TestCase;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2017-07-23.
 */
public class TupleTest extends TestCase {
    @Test
    public void testTuple1() {
        Tuple1<String> source = Tuple.create("1");
        Tuple1<String> target = Tuple.create("1");
        assertEquals(1, source.size());
        assertNotSame(source, target);
        assertEquals(source, target);
        assertEquals(source.compareTo(target), 0);
        assertEquals("1".hashCode(), source.hashCode());
        assertEquals("(1)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("1", source.get(0));
        try {
            Object obj = source.get(1);
            fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple2() {
        Tuple2<String, String> source = Tuple.create("1", "2");
        Tuple2<String, String> target = Tuple.create("1", "2");
        assertEquals(2, source.size());
        assertNotSame(source, target);
        assertEquals(source, target);
        assertEquals(source.compareTo(target), 0);
        assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode()), source.hashCode());
        assertEquals("(1, 2)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        try {
            Object obj = source.get(3);
            fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple3() {
        Tuple3<String, String, String> source = Tuple.create("1", "2", "3");
        Tuple3<String, String, String> target = Tuple.create("1", "2", "3");
        assertEquals(3, source.size());
        assertNotSame(source, target);
        assertEquals(source, target);
        assertEquals(source.compareTo(target), 0);
        assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode()), source.hashCode());
        assertEquals("(1, 2, 3)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("3", source.getItem3());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        assertEquals("3", source.get(2));
        try {
            Object obj = source.get(3);
            fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple4() {
        Tuple4<String, String, String, String> source = Tuple.create("1", "2", "3", "4");
        Tuple4<String, String, String, String> target = Tuple.create("1", "2", "3", "4");
        assertEquals(4, source.size());
        assertNotSame(source, target);
        assertEquals(source, target);
        assertEquals(source.compareTo(target), 0);
        assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode()), source.hashCode());
        assertEquals("(1, 2, 3, 4)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("3", source.getItem3());
        assertEquals("4", source.getItem4());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        assertEquals("3", source.get(2));
        assertEquals("4", source.get(3));
        try {
            Object obj = source.get(4);
            fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple5() {
        Tuple5<String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5");
        Tuple5<String, String, String, String, String> target = Tuple.create("1", "2", "3", "4", "5");
        assertEquals(5, source.size());
        assertNotSame(source, target);
        assertEquals(source, target);
        assertEquals(source.compareTo(target), 0);
        assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode()), source.hashCode());
        assertEquals("(1, 2, 3, 4, 5)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("3", source.getItem3());
        assertEquals("4", source.getItem4());
        assertEquals("5", source.getItem5());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        assertEquals("3", source.get(2));
        assertEquals("4", source.get(3));
        assertEquals("5", source.get(4));
        try {
            Object obj = source.get(5);
            fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple6() {
        Tuple6<String, String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5", "6");
        Tuple6<String, String, String, String, String, String> target = Tuple.create("1", "2", "3", "4", "5", "6");
        assertEquals(6, source.size());
        assertNotSame(source, target);
        assertEquals(source, target);
        assertEquals(source.compareTo(target), 0);
        assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode()), source.hashCode());
        assertEquals("(1, 2, 3, 4, 5, 6)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("3", source.getItem3());
        assertEquals("4", source.getItem4());
        assertEquals("5", source.getItem5());
        assertEquals("6", source.getItem6());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        assertEquals("3", source.get(2));
        assertEquals("4", source.get(3));
        assertEquals("5", source.get(4));
        assertEquals("6", source.get(5));
        try {
            Object obj = source.get(6);
            fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple7() {
        Tuple7<String, String, String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5", "6", "7");
        Tuple7<String, String, String, String, String, String, String> target = Tuple.create("1", "2", "3", "4", "5", "6", "7");
        assertEquals(7, source.size());
        assertNotSame(source, target);
        assertEquals(source, target);
        assertEquals(source.compareTo(target), 0);
        assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode(), "7".hashCode()), source.hashCode());
        assertEquals("(1, 2, 3, 4, 5, 6, 7)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("3", source.getItem3());
        assertEquals("4", source.getItem4());
        assertEquals("5", source.getItem5());
        assertEquals("6", source.getItem6());
        assertEquals("7", source.getItem7());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        assertEquals("3", source.get(2));
        assertEquals("4", source.get(3));
        assertEquals("5", source.get(4));
        assertEquals("6", source.get(5));
        assertEquals("7", source.get(6));
        try {
            Object obj = source.get(7);
            fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testTuple8() {
        TupleMore<String, String, String, String, String, String, String, Tuple1<String>> source = Tuple.create("1", "2", "3", "4", "5", "6", "7", "8");
        TupleMore<String, String, String, String, String, String, String, Tuple1<String>> target = Tuple.create("1", "2", "3", "4", "5", "6", "7", "8");
        assertEquals(8, source.size());
        assertNotSame(source, target);
        assertEquals(source, target);
        assertEquals(source.compareTo(target), 0);
        assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode(), "7".hashCode(), "8".hashCode()), source.hashCode());
        assertEquals("(1, 2, 3, 4, 5, 6, 7, 8)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("3", source.getItem3());
        assertEquals("4", source.getItem4());
        assertEquals("5", source.getItem5());
        assertEquals("6", source.getItem6());
        assertEquals("7", source.getItem7());
        assertEquals(Tuple.create("8"), source.getRest());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        assertEquals("3", source.get(2));
        assertEquals("4", source.get(3));
        assertEquals("5", source.get(4));
        assertEquals("6", source.get(5));
        assertEquals("7", source.get(6));
        assertEquals("8", source.get(7));
        try {
            Object obj = source.get(8);
            fail("should fail,but get " + obj);
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
        assertEquals(16, source.size());
        assertNotSame(source, target);
        assertEquals(source, target);
        assertEquals(source.compareTo(target), 0);
        assertEquals(Tuple.combineHashCodes("9".hashCode(), "10".hashCode(), "11".hashCode(), "12".hashCode(), "13".hashCode(), "14".hashCode(), "15".hashCode(), "16".hashCode()), source.hashCode());
        assertEquals("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("3", source.getItem3());
        assertEquals("4", source.getItem4());
        assertEquals("5", source.getItem5());
        assertEquals("6", source.getItem6());
        assertEquals("7", source.getItem7());
        assertEquals(new TupleMore<>("8", "9", "10", "11", "12", "13", "14", new Tuple2<>("15", "16")), source.getRest());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        assertEquals("3", source.get(2));
        assertEquals("4", source.get(3));
        assertEquals("5", source.get(4));
        assertEquals("6", source.get(5));
        assertEquals("7", source.get(6));
        assertEquals("8", source.get(7));
        assertEquals("9", source.get(8));
        assertEquals("10", source.get(9));
        assertEquals("11", source.get(10));
        assertEquals("12", source.get(11));
        assertEquals("13", source.get(12));
        assertEquals("14", source.get(13));
        assertEquals("15", source.get(14));
        assertEquals("16", source.get(15));
        try {
            Object obj = source.get(16);
            fail("should fail,but get " + obj);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }
}
