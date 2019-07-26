package com.bestvike.linq.adapter.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.WordEnumerator;

/**
 * Created by 许崇雷 on 2019-07-26.
 */
public final class WordEnumerable implements IEnumerable<String> {
    private final String source;

    public WordEnumerable(String source) {
        this.source = source;
    }

    @Override
    public IEnumerator<String> enumerator() {
        return new WordEnumerator(this.source);
    }
}
