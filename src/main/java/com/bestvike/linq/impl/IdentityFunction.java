package com.bestvike.linq.impl;

import com.bestvike.linq.function.Func1;

/**
 * Created by 许崇雷 on 2017/7/18.
 */
public final class IdentityFunction {
    private IdentityFunction() {
    }

    public static <TElement> Func1<TElement, TElement> Instance() {
        return x -> x;
    }
}
