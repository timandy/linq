package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

/**
 * Created by 许崇雷 on 2019-07-26.
 */
public final class WordEnumerator extends AbstractEnumerator<String> {
    private final CharSequence source;

    public WordEnumerator(CharSequence source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state;
        int length = this.source.length();
        while (index < length) {
            char head = this.source.charAt(index);
            if (!Character.isLetterOrDigit(head)) {
                index++;
                continue;
            }
            int beginIndex = index++;
            while (index < length) {
                char tail = this.source.charAt(index);
                if (!Character.isLetterOrDigit(tail))
                    break;
                index++;
            }
            this.current = this.source.subSequence(beginIndex, index).toString();
            this.state = index;
            return true;
        }

        this.close();
        return false;
    }
}
