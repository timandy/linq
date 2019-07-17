package com.bestvike.collections.generic;

/**
 * Created by 许崇雷 on 2019-04-28.
 */
public interface IArray<T> extends IArrayList<T> {
    Object getArray();

    default int _getStartIndex() {
        return 0;
    }

    default int _getEndIndex() {
        return this._getStartIndex() + this._getCount();
    }
}
