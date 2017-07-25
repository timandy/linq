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
public class Tuple2<T1, T2> implements IStructuralEquatable, IStructuralComparable, Comparable, ITuple {
    private final T1 item1;
    private final T2 item2;

    public Tuple2(T1 item1, T2 item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public T1 getItem1() {
        return this.item1;
    }

    public T2 getItem2() {
        return this.item2;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean equals(Object obj) {
        return this.equals(obj, EqualityComparer.Default());
    }

    @Override
    public boolean equals(Object other, IEqualityComparer comparer) {
        Tuple2 that;
        return other != null
                && other instanceof Tuple2
                && comparer.equals(this.item1, (that = (Tuple2) other).item1)
                && comparer.equals(this.item2, that.item2);
    }

    @Override
    public int compareTo(Object obj) {
        return this.compareTo(obj, Comparer.Default());
    }

    @Override
    public int compareTo(Object other, Comparator comparer) {
        if (other == null)
            return 1;
        if (!(other instanceof Tuple2))
            throw Errors.tupleIncorrectType(this.getClass().toString(), "other");
        Tuple2 that = (Tuple2) other;
        int num = comparer.compare(this.item1, that.item1);
        if (num != 0)
            return num;
        return comparer.compare(this.item2, that.item2);
    }

    @Override
    public int hashCode() {
        return this.hashCode(EqualityComparer.Default());
    }

    @Override
    public int hashCode(IEqualityComparer comparer) {
        return Tuple.combineHashCodes(comparer.hashCode(this.item1), comparer.hashCode(this.item2));
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
        builder.append(")");
        return builder.toString();
    }
}
