package com.bestvike.linq.bridge.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

/**
 * Created by 许崇雷 on 2017-07-25.
 */
public final class CharSequenceEnumerator extends AbstractEnumerator<Character> {
    private final CharSequence source;

    public CharSequenceEnumerator(CharSequence source) {
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
