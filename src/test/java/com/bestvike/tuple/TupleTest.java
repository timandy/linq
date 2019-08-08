package com.bestvike.tuple;

import com.bestvike.TestCase;
import com.bestvike.linq.exception.ArgumentException;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2017-07-23.
 */
public class TupleTest extends TestCase {
    @Test
    public void testTuple1() {
        Tuple1<String> source = Tuple.create("1");
        Tuple1<String> expected = Tuple.create("1");
        assertEquals(1, source.size());
        assertNotSame(expected, source);
        assertEquals(expected, source);
        assertEquals(0, source.compareTo(expected));
        assertEquals(1, source.compareTo(null));
        assertThrows(ArgumentException.class, () -> source.compareTo(Tuple.create("1", "2")));
        assertEquals("1".hashCode(), source.hashCode());
        assertEquals("(1)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("1", source.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> source.get(1));
    }

    @Test
    public void testTuple2() {
        Tuple2<String, String> source = Tuple.create("1", "2");
        Tuple2<String, String> expected = Tuple.create("1", "2");
        assertEquals(2, source.size());
        assertNotSame(expected, source);
        assertEquals(expected, source);
        assertEquals(0, source.compareTo(expected));
        assertEquals(1, source.compareTo(Tuple.create("0", "2")));
        assertEquals(1, source.compareTo(null));
        assertThrows(ArgumentException.class, () -> source.compareTo(Tuple.create("1")));
        assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode()), source.hashCode());
        assertEquals("(1, 2)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> source.get(2));
    }

    @Test
    public void testTuple3() {
        Tuple3<String, String, String> source = Tuple.create("1", "2", "3");
        Tuple3<String, String, String> expected = Tuple.create("1", "2", "3");
        assertEquals(3, source.size());
        assertNotSame(expected, source);
        assertEquals(expected, source);
        assertEquals(0, source.compareTo(expected));
        assertEquals(1, source.compareTo(Tuple.create("0", "2", "3")));
        assertEquals(1, source.compareTo(Tuple.create("1", "0", "3")));
        assertEquals(1, source.compareTo(null));
        assertThrows(ArgumentException.class, () -> source.compareTo(Tuple.create("1")));
        assertEquals(Tuple.combineHashCodes("1".hashCode(), "2".hashCode(), "3".hashCode()), source.hashCode());
        assertEquals("(1, 2, 3)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("3", source.getItem3());
        assertEquals("1", source.get(0));
        assertEquals("2", source.get(1));
        assertEquals("3", source.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> source.get(3));
    }

    @Test
    public void testTuple4() {
        Tuple4<String, String, String, String> source = Tuple.create("1", "2", "3", "4");
        Tuple4<String, String, String, String> expected = Tuple.create("1", "2", "3", "4");
        assertEquals(4, source.size());
        assertNotSame(expected, source);
        assertEquals(expected, source);
        assertEquals(0, source.compareTo(expected));
        assertEquals(1, source.compareTo(Tuple.create("0", "2", "3", "4")));
        assertEquals(1, source.compareTo(Tuple.create("1", "0", "3", "4")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "0", "4")));
        assertEquals(1, source.compareTo(null));
        assertThrows(ArgumentException.class, () -> source.compareTo(Tuple.create("1")));
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
        assertThrows(IndexOutOfBoundsException.class, () -> source.get(4));
    }

    @Test
    public void testTuple5() {
        Tuple5<String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5");
        Tuple5<String, String, String, String, String> expected = Tuple.create("1", "2", "3", "4", "5");
        assertEquals(5, source.size());
        assertNotSame(expected, source);
        assertEquals(expected, source);
        assertEquals(0, source.compareTo(expected));
        assertEquals(1, source.compareTo(Tuple.create("0", "2", "3", "4", "5")));
        assertEquals(1, source.compareTo(Tuple.create("1", "0", "3", "4", "5")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "0", "4", "5")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "0", "5")));
        assertEquals(1, source.compareTo(null));
        assertThrows(ArgumentException.class, () -> source.compareTo(Tuple.create("1")));
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
        assertThrows(IndexOutOfBoundsException.class, () -> source.get(5));
    }

    @Test
    public void testTuple6() {
        Tuple6<String, String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5", "6");
        Tuple6<String, String, String, String, String, String> expected = Tuple.create("1", "2", "3", "4", "5", "6");
        assertEquals(6, source.size());
        assertNotSame(expected, source);
        assertEquals(expected, source);
        assertEquals(0, source.compareTo(expected));
        assertEquals(1, source.compareTo(Tuple.create("0", "2", "3", "4", "5", "6")));
        assertEquals(1, source.compareTo(Tuple.create("1", "0", "3", "4", "5", "6")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "0", "4", "5", "6")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "0", "5", "6")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "4", "0", "6")));
        assertEquals(1, source.compareTo(null));
        assertThrows(ArgumentException.class, () -> source.compareTo(Tuple.create("1")));
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
        assertThrows(IndexOutOfBoundsException.class, () -> source.get(6));
    }

    @Test
    public void testTuple7() {
        Tuple7<String, String, String, String, String, String, String> source = Tuple.create("1", "2", "3", "4", "5", "6", "7");
        Tuple7<String, String, String, String, String, String, String> expected = Tuple.create("1", "2", "3", "4", "5", "6", "7");
        assertEquals(7, source.size());
        assertNotSame(expected, source);
        assertEquals(expected, source);
        assertEquals(0, source.compareTo(expected));
        assertEquals(1, source.compareTo(Tuple.create("0", "2", "3", "4", "5", "6", "7")));
        assertEquals(1, source.compareTo(Tuple.create("1", "0", "3", "4", "5", "6", "7")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "0", "4", "5", "6", "7")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "0", "5", "6", "7")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "4", "0", "6", "7")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "4", "5", "0", "7")));
        assertEquals(1, source.compareTo(null));
        assertThrows(ArgumentException.class, () -> source.compareTo(Tuple.create("1")));
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
        assertThrows(IndexOutOfBoundsException.class, () -> source.get(7));
    }

    @Test
    public void testTuple8() {
        TupleN<String, String, String, String, String, String, String, Tuple1<String>> source = Tuple.create("1", "2", "3", "4", "5", "6", "7", "8");
        TupleN<String, String, String, String, String, String, String, Tuple1<String>> expected = Tuple.create("1", "2", "3", "4", "5", "6", "7", "8");
        assertEquals(8, source.size());
        assertNotSame(expected, source);
        assertEquals(expected, source);
        assertEquals(0, source.compareTo(expected));
        assertEquals(1, source.compareTo(Tuple.create("0", "2", "3", "4", "5", "6", "7", "8")));
        assertEquals(1, source.compareTo(Tuple.create("1", "0", "3", "4", "5", "6", "7", "8")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "0", "4", "5", "6", "7", "8")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "0", "5", "6", "7", "8")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "4", "0", "6", "7", "8")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "4", "5", "0", "7", "8")));
        assertEquals(1, source.compareTo(Tuple.create("1", "2", "3", "4", "5", "6", "0", "8")));
        assertEquals(1, source.compareTo(null));
        assertThrows(ArgumentException.class, () -> source.compareTo(Tuple.create("1")));
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
        assertThrows(IndexOutOfBoundsException.class, () -> source.get(8));
    }

    @Test
    public void testTupleN() {
        assertThrows(ArgumentException.class, () -> new TupleN<>("1", "2", "3", "4", "5", "6", "7", "8"));

        TupleN<String, String, String, String, String, String, String, TupleN<String, String, String, String, String, String, String, Tuple2<String, String>>> source =
                new TupleN<>("1", "2", "3", "4", "5", "6", "7", new TupleN<>("8", "9", "10", "11", "12", "13", "14", Tuple.create("15", "16")));
        TupleN<String, String, String, String, String, String, String, TupleN<String, String, String, String, String, String, String, Tuple2<String, String>>> expected =
                new TupleN<>("1", "2", "3", "4", "5", "6", "7", new TupleN<>("8", "9", "10", "11", "12", "13", "14", Tuple.create("15", "16")));
        assertEquals(16, source.size());
        assertNotSame(expected, source);
        assertEquals(expected, source);
        assertEquals(0, source.compareTo(expected));

        assertEquals(Tuple.combineHashCodes("2".hashCode(), "3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode(), "7".hashCode(), Tuple.combineHashCodes("8".hashCode(), "9".hashCode())),
                new TupleN<>("1", "2", "3", "4", "5", "6", "7", Tuple.create("8", "9")).hashCode());

        assertEquals(Tuple.combineHashCodes("3".hashCode(), "4".hashCode(), "5".hashCode(), "6".hashCode(), "7".hashCode(), Tuple.combineHashCodes("8".hashCode(), "9".hashCode(), "10".hashCode())),
                new TupleN<>("1", "2", "3", "4", "5", "6", "7", Tuple.create("8", "9", "10")).hashCode());

        assertEquals(Tuple.combineHashCodes("4".hashCode(), "5".hashCode(), "6".hashCode(), "7".hashCode(), Tuple.combineHashCodes("8".hashCode(), "9".hashCode(), "10".hashCode(), "11".hashCode())),
                new TupleN<>("1", "2", "3", "4", "5", "6", "7", Tuple.create("8", "9", "10", "11")).hashCode());

        assertEquals(Tuple.combineHashCodes("5".hashCode(), "6".hashCode(), "7".hashCode(), Tuple.combineHashCodes("8".hashCode(), "9".hashCode(), "10".hashCode(), "11".hashCode(), "12".hashCode())),
                new TupleN<>("1", "2", "3", "4", "5", "6", "7", Tuple.create("8", "9", "10", "11", "12")).hashCode());

        assertEquals(Tuple.combineHashCodes("6".hashCode(), "7".hashCode(), Tuple.combineHashCodes("8".hashCode(), "9".hashCode(), "10".hashCode(), "11".hashCode(), "12".hashCode(), "13".hashCode())),
                new TupleN<>("1", "2", "3", "4", "5", "6", "7", Tuple.create("8", "9", "10", "11", "12", "13")).hashCode());

        assertEquals(Tuple.combineHashCodes("7".hashCode(), Tuple.combineHashCodes("8".hashCode(), "9".hashCode(), "10".hashCode(), "11".hashCode(), "12".hashCode(), "13".hashCode(), "14".hashCode())),
                new TupleN<>("1", "2", "3", "4", "5", "6", "7", Tuple.create("8", "9", "10", "11", "12", "13", "14")).hashCode());

        assertEquals(Tuple.combineHashCodes("8".hashCode(), "9".hashCode(), "10".hashCode(), "11".hashCode(), "12".hashCode(), "13".hashCode(), "14".hashCode(), "15".hashCode()),
                new TupleN<>("1", "2", "3", "4", "5", "6", "7", new TupleN<>("8", "9", "10", "11", "12", "13", "14", Tuple.create("15"))).hashCode());

        assertEquals(Tuple.combineHashCodes("9".hashCode(), "10".hashCode(), "11".hashCode(), "12".hashCode(), "13".hashCode(), "14".hashCode(), Tuple.combineHashCodes("15".hashCode(), "16".hashCode())), source.hashCode());

        assertEquals("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)", source.toString());
        assertEquals("1", source.getItem1());
        assertEquals("2", source.getItem2());
        assertEquals("3", source.getItem3());
        assertEquals("4", source.getItem4());
        assertEquals("5", source.getItem5());
        assertEquals("6", source.getItem6());
        assertEquals("7", source.getItem7());
        assertEquals(new TupleN<>("8", "9", "10", "11", "12", "13", "14", Tuple.create("15", "16")), source.getRest());
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
        assertThrows(IndexOutOfBoundsException.class, () -> source.get(16));
    }
}
