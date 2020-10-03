package com.bestvike.linq.util;

/**
 * Created by 许崇雷 on 2019-08-13.
 */
public enum StringSplitOptions {
    None(0),
    RemoveEmptyEntries(1),
    TrimEntries(2),
    TrimAndRemoveEmptyEntries(3);

    private final int value;

    StringSplitOptions(int value) {
        this.value = value;
    }

    public int intValue() {
        return this.value;
    }

    public boolean hasFlag(StringSplitOptions value) {
        return (this.intValue() & value.intValue()) != 0;
    }
}
