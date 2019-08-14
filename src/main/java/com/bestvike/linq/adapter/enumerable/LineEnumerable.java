package com.bestvike.linq.adapter.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.LineEnumerator;

/**
 * Created by 许崇雷 on 2019-07-26.
 */
public final class LineEnumerable implements IEnumerable<String> {
    private final CharSequence source;

    public LineEnumerable(CharSequence source) {
        this.source = source;
    }

    @Override
    public IEnumerator<String> enumerator() {
        return new LineEnumerator(this.source);
    }
}
