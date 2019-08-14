package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;
import com.bestvike.linq.util.Chars;

/**
 * Created by 许崇雷 on 2019-07-26.
 */
public final class LineEnumerator extends AbstractEnumerator<String> {
    private final CharSequence source;

    public LineEnumerator(CharSequence source) {
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
            if (head == Chars.CR || head == Chars.LF) {
                index++;
                continue;
            }
            int beginIndex = index++;
            while (index < length) {
                char tail = this.source.charAt(index);
                if (tail == Chars.CR || tail == Chars.LF)
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
