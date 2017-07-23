package com.bestvike.linq.tuple;

import com.bestvike.linq.IEqualityComparer;

/**
 * @author 许崇雷
 * @date 2017/7/23
 */
interface ITuple {
    int size();

    String toString(StringBuilder sb);

    int hashCode(IEqualityComparer comparer);
}
