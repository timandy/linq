package com.bestvike.tuple;

import com.bestvike.collections.IStructuralComparable;
import com.bestvike.collections.IStructuralEquatable;
import com.bestvike.collections.generic.Comparer;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.exception.Errors;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017/7/23.
 */
@SuppressWarnings({"unchecked", "EqualsWhichDoesntCheckParameterClass"})
public final class Tuple3<T1, T2, T3> implements IStructuralEquatable, IStructuralComparable, Comparable, ITupleInternal, ITuple {
    private final T1 item1;
    private final T2 item2;
    private final T3 item3;

    public Tuple3(T1 item1, T2 item2, T3 item3) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
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

    @Override
    public int size() {
        return 3;
    }

    @Override
    public Object get(int index) {
        switch (index) {
            case 0:
                return this.item1;
            case 1:
                return this.item2;
            case 2:
                return this.item3;
            default:
                throw Errors.indexOutOfRange();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return this.equals(obj, EqualityComparer.Default());
    }

    @Override
    public boolean equals(Object other, IEqualityComparer comparer) {
        Tuple3 that;
        return other instanceof Tuple3
                && comparer.equals(this.item1, (that = (Tuple3) other).item1)
                && comparer.equals(this.item2, that.item2)
                && comparer.equals(this.item3, that.item3);
    }

    @Override
    public int compareTo(Object obj) {
        return this.compareTo(obj, Comparer.Default());
    }

    @Override
    public int compareTo(Object other, Comparator comparer) {
        if (other == null)
            return 1;
        if (!(other instanceof Tuple3))
            throw Errors.tupleIncorrectType(this.getClass(), "other");
        Tuple3 that = (Tuple3) other;
        int num = comparer.compare(this.item1, that.item1);
        if (num != 0)
            return num;
        num = comparer.compare(this.item2, that.item2);
        if (num != 0)
            return num;
        return comparer.compare(this.item3, that.item3);
    }

    @Override
    public int hashCode() {
        return this.hashCode(EqualityComparer.Default());
    }

    @Override
    public int hashCode(IEqualityComparer comparer) {
        return Tuple.combineHashCodes(comparer.hashCode(this.item1),
                comparer.hashCode(this.item2),
                comparer.hashCode(this.item3));
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
        builder.append(")");
        return builder.toString();
    }
}
