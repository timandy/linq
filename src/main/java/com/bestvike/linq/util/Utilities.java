package com.bestvike.linq.util;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;

/**
 * Created by 许崇雷 on 2017/7/16.
 */
public final class Utilities {
    private Utilities() {
    }

    public static <TSource> boolean areEqualityComparersEqual(IEqualityComparer<TSource> left, IEqualityComparer<TSource> right) {
        if (left == right)
            return true;

        IEqualityComparer<TSource> defaultComparer = EqualityComparer.Default();
        if (left == null) {
            // Micro-opt: Typically it's impossible to get a different instance
            // of the default comparer without reflection/serialization.
            // Save a virtual method call to Equals in the common case.
            return right == defaultComparer || right.equals(defaultComparer);
        }

        if (right == null) {
            return left == defaultComparer || left.equals(defaultComparer);
        }

        return left.equals(right);
    }

    public static <TSource> Func1<TSource, Boolean> combinePredicates(Func1<TSource, Boolean> predicate1, Func1<TSource, Boolean> predicate2) {
        return x -> predicate1.apply(x) && predicate2.apply(x);
    }

    public static <TSource, TMiddle, TResult> Func1<TSource, TResult> combineSelectors(Func1<TSource, TMiddle> selector1, Func1<TMiddle, TResult> selector2) {
        return x -> selector2.apply(selector1.apply(x));
    }
}
