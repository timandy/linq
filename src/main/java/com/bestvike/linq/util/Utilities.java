package com.bestvike.linq.util;

import com.bestvike.function.Func1;

/**
 * Created by 许崇雷 on 2017/7/16.
 */
public final class Utilities {
    private Utilities() {
    }

    public static <TSource> Func1<TSource, Boolean> combinePredicates(Func1<TSource, Boolean> predicate1, Func1<TSource, Boolean> predicate2) {
        return x -> predicate1.apply(x) && predicate2.apply(x);
    }

    public static <TSource, TMiddle, TResult> Func1<TSource, TResult> combineSelectors(Func1<TSource, TMiddle> selector1, Func1<TMiddle, TResult> selector2) {
        return x -> selector2.apply(selector1.apply(x));
    }
}
