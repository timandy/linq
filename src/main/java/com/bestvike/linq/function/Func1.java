package com.bestvike.linq.function;

/**
 * @author 许崇雷
 * @date 2017/7/10
 */
@FunctionalInterface
public interface Func1<T, TResult> {
    TResult apply(T arg);
}
