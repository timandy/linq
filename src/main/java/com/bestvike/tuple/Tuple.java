package com.bestvike.tuple;

/**
 * Created by 许崇雷 on 2017/7/23.
 */
public final class Tuple {
    private Tuple() {
    }

    public static <T1> Tuple1<T1> create(T1 item1) {
        return new Tuple1<>(item1);
    }

    public static <T1, T2> Tuple2<T1, T2> create(T1 item1, T2 item2) {
        return new Tuple2<>(item1, item2);
    }

    public static <T1, T2, T3> Tuple3<T1, T2, T3> create(T1 item1, T2 item2, T3 item3) {
        return new Tuple3<>(item1, item2, item3);
    }

    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> create(T1 item1, T2 item2, T3 item3, T4 item4) {
        return new Tuple4<>(item1, item2, item3, item4);
    }

    public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5) {
        return new Tuple5<>(item1, item2, item3, item4, item5);
    }

    public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6) {
        return new Tuple6<>(item1, item2, item3, item4, item5, item6);
    }

    public static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6, T7 item7) {
        return new Tuple7<>(item1, item2, item3, item4, item5, item6, item7);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8> TupleMore<T1, T2, T3, T4, T5, T6, T7, Tuple1<T8>> create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6, T7 item7, T8 item8) {
        return new TupleMore<>(item1, item2, item3, item4, item5, item6, item7, new Tuple1<>(item8));
    }

    static int combineHashCodes(int h1, int h2) {
        return (h1 << 5) + h1 ^ h2;
    }

    static int combineHashCodes(int h1, int h2, int h3) {
        return Tuple.combineHashCodes(Tuple.combineHashCodes(h1, h2), h3);
    }

    static int combineHashCodes(int h1, int h2, int h3, int h4) {
        return Tuple.combineHashCodes(Tuple.combineHashCodes(h1, h2), Tuple.combineHashCodes(h3, h4));
    }

    static int combineHashCodes(int h1, int h2, int h3, int h4, int h5) {
        return Tuple.combineHashCodes(Tuple.combineHashCodes(h1, h2, h3, h4), h5);
    }

    static int combineHashCodes(int h1, int h2, int h3, int h4, int h5, int h6) {
        return Tuple.combineHashCodes(Tuple.combineHashCodes(h1, h2, h3, h4), Tuple.combineHashCodes(h5, h6));
    }

    static int combineHashCodes(int h1, int h2, int h3, int h4, int h5, int h6, int h7) {
        return Tuple.combineHashCodes(Tuple.combineHashCodes(h1, h2, h3, h4), Tuple.combineHashCodes(h5, h6, h7));
    }

    static int combineHashCodes(int h1, int h2, int h3, int h4, int h5, int h6, int h7, int h8) {
        return Tuple.combineHashCodes(Tuple.combineHashCodes(h1, h2, h3, h4), Tuple.combineHashCodes(h5, h6, h7, h8));
    }
}
