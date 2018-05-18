package com.bestvike;

/**
 * Created by 许崇雷 on 2018-04-20.
 */
public final class out<T> {
    public T value;

    private out() {
    }

    public static <T> out<T> init() {
        return new out<>();
    }
}
