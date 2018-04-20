package com.bestvike;

/**
 * Created by 许崇雷 on 2018-04-20.
 */
public final class out<T> {
    private T value;

    private out() {
    }

    public static <T> out<T> init() {
        return new out<>();
    }

    public T getValue() {
        return this.value;
    }

    public T setValue(T value) {
        return this.value = value;
    }
}
