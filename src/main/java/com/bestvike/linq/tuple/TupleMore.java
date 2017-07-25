package com.bestvike.linq.tuple;

import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.IStructuralComparable;
import com.bestvike.linq.IStructuralEquatable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.Comparer;
import com.bestvike.linq.util.EqualityComparer;

import java.util.Comparator;

/**
 * @author 许崇雷
 * @date 2017/7/23
 */
@SuppressWarnings({"unchecked", "EqualsWhichDoesntCheckParameterClass"})
public class TupleMore<T1, T2, T3, T4, T5, T6, T7, TRest extends ITuple> implements IStructuralEquatable, IStructuralComparable, Comparable, ITuple {
    private final T1 item1;
    private final T2 item2;
    private final T3 item3;
    private final T4 item4;
    private final T5 item5;
    private final T6 item6;
    private final T7 item7;
    private final TRest rest;

    public TupleMore(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6, T7 item7, TRest rest) {
        if (rest == null)
            throw Errors.argumentNull("rest");
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.item6 = item6;
        this.item7 = item7;
        this.rest = rest;
    }

    public T1 getItem1() {
        return this.item1;
    }

    public T2 getItem2() {
        return this.item2;
    }

    public T3 getItem3() {
        return this.item3;
    }

    public T4 getItem4() {
        return this.item4;
    }

    public T5 getItem5() {
        return this.item5;
    }

    public T6 getItem6() {
        return this.item6;
    }

    public T7 getItem7() {
        return this.item7;
    }

    public TRest getRest() {
        return this.rest;
    }

    @Override
    public int size() {
        return 7 + this.rest.size();
    }

    @Override
    public boolean equals(Object obj) {
        return this.equals(obj, EqualityComparer.Default());
    }

    @Override
    public boolean equals(Object other, IEqualityComparer comparer) {
        TupleMore that;
        return other != null
                && other instanceof TupleMore
                && comparer.equals(this.item1, (that = (TupleMore) other).item1)
                && comparer.equals(this.item2, that.item2)
                && comparer.equals(this.item3, that.item3)
                && comparer.equals(this.item4, that.item4)
                && comparer.equals(this.item5, that.item5)
                && comparer.equals(this.item6, that.item6)
                && comparer.equals(this.item7, that.item7)
                && comparer.equals(this.rest, that.rest);
    }

    @Override
    public int compareTo(Object obj) {
        return this.compareTo(obj, Comparer.Default());
    }

    @Override
    public int compareTo(Object other, Comparator comparer) {
        if (other == null)
            return 1;
        if (!(other instanceof TupleMore))
            throw Errors.tupleIncorrectType(this.getClass().toString(), "other");
        TupleMore that = (TupleMore) other;
        int num = comparer.compare(this.item1, that.item1);
        if (num != 0)
            return num;
        num = comparer.compare(this.item2, that.item2);
        if (num != 0)
            return num;
        num = comparer.compare(this.item3, that.item3);
        if (num != 0)
            return num;
        num = comparer.compare(this.item4, that.item4);
        if (num != 0)
            return num;
        num = comparer.compare(this.item5, that.item5);
        if (num != 0)
            return num;
        num = comparer.compare(this.item6, that.item6);
        if (num != 0)
            return num;
        num = comparer.compare(this.item7, that.item7);
        if (num != 0)
            return num;
        return comparer.compare(this.rest, that.rest);
    }

    @Override
    public int hashCode() {
        return this.hashCode(EqualityComparer.Default());
    }

    @Override
    public int hashCode(IEqualityComparer comparer) {
        if (this.rest.size() >= 8)
            return this.rest.hashCode(comparer);
        switch (8 - this.rest.size()) {
            case 1:
                return Tuple.combineHashCodes(comparer.hashCode(this.item7), this.rest.hashCode(comparer));
            case 2:
                return Tuple.combineHashCodes(comparer.hashCode(this.item6),
                        comparer.hashCode(this.item7),
                        this.rest.hashCode(comparer));
            case 3:
                return Tuple.combineHashCodes(comparer.hashCode(this.item5),
                        comparer.hashCode(this.item6),
                        comparer.hashCode(this.item7),
                        this.rest.hashCode(comparer));
            case 4:
                return Tuple.combineHashCodes(comparer.hashCode(this.item4),
                        comparer.hashCode(this.item5),
                        comparer.hashCode(this.item6),
                        comparer.hashCode(this.item7),
                        this.rest.hashCode(comparer));
            case 5:
                return Tuple.combineHashCodes(comparer.hashCode(this.item3),
                        comparer.hashCode(this.item4),
                        comparer.hashCode(this.item5),
                        comparer.hashCode(this.item6),
                        comparer.hashCode(this.item7),
                        this.rest.hashCode(comparer));
            case 6:
                return Tuple.combineHashCodes(comparer.hashCode(this.item2),
                        comparer.hashCode(this.item3),
                        comparer.hashCode(this.item4),
                        comparer.hashCode(this.item5),
                        comparer.hashCode(this.item6),
                        comparer.hashCode(this.item7),
                        this.rest.hashCode(comparer));
            case 7:
                return Tuple.combineHashCodes(comparer.hashCode(this.item1),
                        comparer.hashCode(this.item2),
                        comparer.hashCode(this.item3),
                        comparer.hashCode(this.item4),
                        comparer.hashCode(this.item5),
                        comparer.hashCode(this.item6),
                        comparer.hashCode(this.item7),
                        this.rest.hashCode(comparer));
            default:
                return -1;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        return this.toString(builder);
    }

    @Override
    public String toString(StringBuilder builder) {
        builder.append(this.item1);
        builder.append(", ");
        builder.append(this.item2);
        builder.append(", ");
        builder.append(this.item3);
        builder.append(", ");
        builder.append(this.item4);
        builder.append(", ");
        builder.append(this.item5);
        builder.append(", ");
        builder.append(this.item6);
        builder.append(", ");
        builder.append(this.item7);
        builder.append(", ");
        return this.rest.toString(builder);
    }
}
