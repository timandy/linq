package com.bestvike.collections.generic;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.enumerable.RunOnce;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public interface IList<T> extends ICollection<T> {
    T get(int index);

    @Override
    default IEnumerable<T> runOnce() {
        return RunOnce.runOnce(this);
    }
}
