package com.bestvike.linq.enumerator;

/**
 * Created by 许崇雷 on 2017/7/25.
 */
public final class CharSequenceEnumerator extends AbstractEnumerator<Character> {
    private final CharSequence source;
    private final int endIndex;
    private int index;

    public CharSequenceEnumerator(CharSequence source) {
        this(source, 0, source.length());
    }

    public CharSequenceEnumerator(CharSequence source, int index, int count) {
        this.source = source;
        this.index = index;
        this.endIndex = Math.addExact(index, count);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                if (this.index < this.endIndex) {
                    this.current = this.source.charAt(this.index++);
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }
}
