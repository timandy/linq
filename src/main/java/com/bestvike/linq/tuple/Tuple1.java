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
public final class Tuple1<T1> implements IStructuralEquatable, IStructuralComparable, Comparable, ITuple {
    private final T1 item1;

    public Tuple1(T1 item1) {
        this.item1 = item1;
    }

    public T1 getItem1() {
        return this.item1;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return this.equals(obj, EqualityComparer.Default());
    }

    @Override
    public boolean equals(Object other, IEqualityComparer comparer) {
        return other != null
                && other instanceof Tuple1
                && comparer.equals(this.item1, ((Tuple1) other).item1);
    }

    @Override
    public int compareTo(Object obj) {
        return this.compareTo(obj, Comparer.Default());
    }

    @Override
    public int compareTo(Object other, Comparator comparer) {
        if (other == null)
            return 1;
        if (!(other instanceof Tuple1))
            throw Errors.argumentNotValid("other");
        return comparer.compare(this.item1, ((Tuple1) other).item1);
    }

    @Override
    public int hashCode() {
        return this.hashCode(EqualityComparer.Default());
    }

    @Override
    public int hashCode(IEqualityComparer comparer) {
        return comparer.hashCode(this.item1);
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
        builder.append(")");
        return builder.toString();
    }
}
