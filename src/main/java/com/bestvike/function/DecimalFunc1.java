package com.bestvike.function;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2019-06-24.
 */
@FunctionalInterface
public interface DecimalFunc1<T> {
    BigDecimal apply(T arg);
}
