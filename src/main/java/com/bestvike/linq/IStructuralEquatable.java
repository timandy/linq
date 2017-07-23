package com.bestvike.linq;

/**
 * @author 许崇雷
 * @date 2017/7/23
 */
public interface IStructuralEquatable {
    boolean equals(Object other, IEqualityComparer comparer);

    int hashCode(IEqualityComparer comparer);
}
