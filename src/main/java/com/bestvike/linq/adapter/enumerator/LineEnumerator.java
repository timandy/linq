package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;
import com.bestvike.linq.util.Chars;

/**
 * Created by 许崇雷 on 2019-07-26.
 */
public final class LineEnumerator extends AbstractEnumerator<String> {
    private final String source;

    public LineEnumerator(String source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int length = this.source.length();
        while (this.state < length) {
            char head = this.source.charAt(this.state);
            if (head == Chars.CR || head == Chars.LF) {
                this.state++;
                continue;
            }
            int beginIndex = this.state++;
            while (this.state < length) {
                char tail = this.source.charAt(this.state);
                if (tail == Chars.CR || tail == Chars.LF)
                    break;
                this.state++;
            }
            this.current = this.source.substring(beginIndex, this.state);
            return true;
        }

        this.close();
        return false;
    }
}
