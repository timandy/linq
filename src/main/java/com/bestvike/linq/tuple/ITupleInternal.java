package com.bestvike.linq.tuple;

import com.bestvike.linq.IEqualityComparer;

/**
 * Created by 许崇雷 on 2017-09-05.
 */
interface ITupleInternal extends ITuple {
    String toString(StringBuilder sb);

    int hashCode(IEqualityComparer comparer);
}
