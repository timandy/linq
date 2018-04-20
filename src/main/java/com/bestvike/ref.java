package com.bestvike;

/**
 * Created by 许崇雷 on 2018-04-20.
 */
public final class ref<T> {
    private T value;

    private ref(T value) {
        this.value = value;
    }

    public static <T> ref<T> init(T value) {
        return new ref<>(value);
    }

    public T getValue() {
        return this.value;
    }

    public T setValue(T value) {
        return this.value = value;
    }
}
