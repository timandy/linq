package com.bestvike.collections;

import com.bestvike.collections.generic.IEqualityComparer;

/**
 * Created by 许崇雷 on 2017/7/23.
 */
public interface IStructuralEquatable {
    boolean equals(Object other, IEqualityComparer comparer);

    int hashCode(IEqualityComparer comparer);
}
