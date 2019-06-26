package com.bestvike.function;

import com.sun.istack.internal.NotNull;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2019-06-24.
 */
@FunctionalInterface
public interface DecimalFunc1<T> {
    @NotNull
    BigDecimal apply(T arg);
}
