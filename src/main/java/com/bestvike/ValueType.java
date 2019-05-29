package com.bestvike;

import com.bestvike.linq.enumerable.Value;

/**
 * Created by 许崇雷 on 2019-05-28.
 */
public abstract class ValueType {
    protected ValueType() {
    }

    @Override
    public boolean equals(Object obj) {
        return Value.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return Value.hashCode(this);
    }

    @Override
    public String toString() {
        return Value.toString(this);
    }
}
