package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

/**
 * Created by 许崇雷 on 2017-07-25.
 */
public final class CharEnumerator extends AbstractEnumerator<Character> {
    private final CharSequence source;

    public CharEnumerator(CharSequence source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;
        if (this.state < this.source.length()) {
            this.current = this.source.charAt(this.state++);
            return true;
        }
        this.close();
        return false;
    }
}
