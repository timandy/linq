package com.bestvike;

import com.bestvike.linq.enumerable.Values;

/**
 * Created by 许崇雷 on 2019-05-28.
 */
public abstract class ValueType {
    protected ValueType() {
    }

    @Override
    public boolean equals(Object obj) {
        return Values.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return Values.hashCode(this);
    }

    @Override
    public String toString() {
        return Values.toString(this);
    }
}
