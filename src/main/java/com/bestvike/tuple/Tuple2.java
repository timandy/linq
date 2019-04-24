package com.bestvike.tuple;

import com.bestvike.collections.IStructuralComparable;
import com.bestvike.collections.IStructuralEquatable;
import com.bestvike.collections.generic.Comparer;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017-07-23.
 */
@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
public final class Tuple2<T1, T2> implements IStructuralEquatable, IStructuralComparable, Comparable, ITupleInternal, ITuple {
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
    public Object get(int index) {
        switch (index) {
            case 0:
                return this.item1;
            case 1:
                return this.item2;
            default:
                ThrowHelper.throwIndexOutOfRangeException();
                return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return this.equals(obj, EqualityComparer.Default());
    }

    @Override
    public boolean equals(Object other, IEqualityComparer comparer) {
        Tuple2 objTuple;
        //noinspection unchecked
        return other instanceof Tuple2
                && comparer.equals(this.item1, (objTuple = (Tuple2) other).item1)
                && comparer.equals(this.item2, objTuple.item2);
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
            ThrowHelper.throwTupleIncorrectTypeException(this.getClass(), ExceptionArgument.other);
        Tuple2 objTuple = (Tuple2) other;
        //noinspection unchecked
        int c = comparer.compare(this.item1, objTuple.item1);
        if (c != 0)
            return c;
        //noinspection unchecked
        return comparer.compare(this.item2, objTuple.item2);
    }

    @Override
    public int hashCode() {
        return this.hashCode(EqualityComparer.Default());
    }

    @Override
    public int hashCode(IEqualityComparer comparer) {
        //noinspection unchecked
        return Tuple.combineHashCodes(comparer.hashCode(this.item1), comparer.hashCode(this.item2));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        return this.toString(sb);
    }

    @Override
    public String toString(StringBuilder sb) {
        sb.append(this.item1);
        sb.append(", ");
        sb.append(this.item2);
        sb.append(")");
        return sb.toString();
    }
}
