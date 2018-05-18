package com.bestvike;

/**
 * Created by 许崇雷 on 2018-04-20.
 */
public final class ref<T> {
    public T value;

    private ref() {
    }

    public static <T> ref<T> init(T value) {
        assert value != null;

        ref<T> ref = new ref<>();
        ref.value = value;
        return ref;
    }
}
