package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.adapter.enumerable.SingletonEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.StringSplitOptions;
import com.bestvike.linq.util.Strings;

/**
 * Created by 许崇雷 on 2019-08-13.
 */
public final class Split {
    private Split() {
    }

    public static IEnumerable<String> split(CharSequence source, char separator) {
        return split(source, separator, StringSplitOptions.None);
    }

    public static IEnumerable<String> split(CharSequence source, char separator, StringSplitOptions options) {
        if (options == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.options);

        if (source == null)
            return Array.empty();

        boolean omitEmptyEntries = (options == StringSplitOptions.RemoveEmptyEntries);
        if (omitEmptyEntries && source.length() == 0)
            return Array.empty();

        return omitEmptyEntries
                ? new CharOmitEmptySplitIterator1(source, separator)
                : new CharKeepEmptySplitIterator1(source, separator);
    }

    public static IEnumerable<String> split(CharSequence source, char[] separator) {
        return split(source, separator, StringSplitOptions.None);
    }

    public static IEnumerable<String> split(CharSequence source, char[] separator, StringSplitOptions options) {
        if (options == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.options);

        if (source == null)
            return Array.empty();

        boolean omitEmptyEntries = (options == StringSplitOptions.RemoveEmptyEntries);
        if (omitEmptyEntries && source.length() == 0)
            return Array.empty();

        switch (separator == null ? 0 : separator.length) {
            case 0:
                return omitEmptyEntries
                        ? new CharOmitEmptySplitIterator0(source)
                        : new CharKeepEmptySplitIterator0(source);
            case 1:
                return omitEmptyEntries
                        ? new CharOmitEmptySplitIterator1(source, separator[0])
                        : new CharKeepEmptySplitIterator1(source, separator[0]);
            case 2:
                return omitEmptyEntries
                        ? new CharOmitEmptySplitIterator2(source, separator[0], separator[1])
                        : new CharKeepEmptySplitIterator2(source, separator[0], separator[1]);
            case 3:
                return omitEmptyEntries
                        ? new CharOmitEmptySplitIterator3(source, separator[0], separator[1], separator[2])
                        : new CharKeepEmptySplitIterator3(source, separator[0], separator[1], separator[2]);
            default:
                return omitEmptyEntries
                        ? new CharOmitEmptySplitIteratorN(source, separator)
                        : new CharKeepEmptySplitIteratorN(source, separator);
        }
    }

    public static IEnumerable<String> split(CharSequence source, CharSequence separator) {
        return split(source, separator, StringSplitOptions.None);
    }

    public static IEnumerable<String> split(CharSequence source, CharSequence separator, StringSplitOptions options) {
        if (options == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.options);

        if (source == null)
            return Array.empty();

        boolean omitEmptyEntries = options == StringSplitOptions.RemoveEmptyEntries;
        if (omitEmptyEntries && source.length() == 0)
            return Array.empty();

        if (Strings.isNullOrEmpty(separator))
            return new SingletonEnumerable<>(source.toString());

        return omitEmptyEntries
                ? new StringOmitEmptySplitIterator1(source, separator)
                : new StringKeepEmptySplitIterator1(source, separator);
    }

    public static IEnumerable<String> split(CharSequence source, CharSequence[] separator) {
        return split(source, separator, StringSplitOptions.None);
    }

    public static IEnumerable<String> split(CharSequence source, CharSequence[] separator, StringSplitOptions options) {
        if (options == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.options);

        if (source == null)
            return Array.empty();

        boolean omitEmptyEntries = (options == StringSplitOptions.RemoveEmptyEntries);
        if (omitEmptyEntries && source.length() == 0)
            return Array.empty();

        if (separator == null || separator.length == 0)
            return omitEmptyEntries
                    ? new CharOmitEmptySplitIterator0(source)
                    : new CharKeepEmptySplitIterator0(source);

        return omitEmptyEntries
                ? new StringOmitEmptySplitIteratorN(source, separator)
                : new StringKeepEmptySplitIteratorN(source, separator);
    }
}


final class CharKeepEmptySplitIterator0 extends AbstractIterator<String> {
    private final CharSequence source;

    CharKeepEmptySplitIterator0(CharSequence source) {
        assert source != null;
        this.source = source;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharKeepEmptySplitIterator0(this.source);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        if (index < 0) {
            this.close();
            return false;
        }

        int length = this.source.length();
        int beginIndex = index;
        while (index < length) {
            char current = this.source.charAt(index);
            if (Character.isWhitespace(current)) {
                this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
                this.state = index + 2;
                return true;
            }
            index++;
        }
        if (index == length) {
            this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
            this.state = index + 2;
            return true;
        }

        this.close();
        return false;
    }
}


final class CharKeepEmptySplitIterator1 extends AbstractIterator<String> {
    private final CharSequence source;
    private final char separator;

    CharKeepEmptySplitIterator1(CharSequence source, char separator) {
        assert source != null;
        this.source = source;
        this.separator = separator;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharKeepEmptySplitIterator1(this.source, this.separator);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        if (index < 0) {
            this.close();
            return false;
        }

        int length = this.source.length();
        int beginIndex = index;
        while (index < length) {
            char current = this.source.charAt(index);
            if (current == this.separator) {
                this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
                this.state = index + 2;
                return true;
            }
            index++;
        }
        if (index == length) {
            this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
            this.state = index + 2;
            return true;
        }

        this.close();
        return false;
    }
}


final class CharKeepEmptySplitIterator2 extends AbstractIterator<String> {
    private final CharSequence source;
    private final char separator0;
    private final char separator1;

    CharKeepEmptySplitIterator2(CharSequence source, char separator0, char separator1) {
        assert source != null;
        this.source = source;
        this.separator0 = separator0;
        this.separator1 = separator1;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharKeepEmptySplitIterator2(this.source, this.separator0, this.separator1);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        if (index < 0) {
            this.close();
            return false;
        }

        int length = this.source.length();
        int beginIndex = index;
        while (index < length) {
            char current = this.source.charAt(index);
            if (current == this.separator0 || current == this.separator1) {
                this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
                this.state = index + 2;
                return true;
            }
            index++;
        }
        if (index == length) {
            this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
            this.state = index + 2;
            return true;
        }

        this.close();
        return false;
    }
}


final class CharKeepEmptySplitIterator3 extends AbstractIterator<String> {
    private final CharSequence source;
    private final char separator0;
    private final char separator1;
    private final char separator2;

    CharKeepEmptySplitIterator3(CharSequence source, char separator0, char separator1, char separator2) {
        assert source != null;
        this.source = source;
        this.separator0 = separator0;
        this.separator1 = separator1;
        this.separator2 = separator2;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharKeepEmptySplitIterator3(this.source, this.separator0, this.separator1, this.separator2);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        if (index < 0) {
            this.close();
            return false;
        }

        int length = this.source.length();
        int beginIndex = index;
        while (index < length) {
            char current = this.source.charAt(index);
            if (current == this.separator0 || current == this.separator1 || current == this.separator2) {
                this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
                this.state = index + 2;
                return true;
            }
            index++;
        }
        if (index == length) {
            this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
            this.state = index + 2;
            return true;
        }

        this.close();
        return false;
    }
}


final class CharKeepEmptySplitIteratorN extends AbstractIterator<String> {
    private final CharSequence source;
    private final char[] separators;
    private ProbabilisticMap charMap;

    CharKeepEmptySplitIteratorN(CharSequence source, char[] separators) {
        assert source != null;
        assert separators != null && separators.length > 3;
        this.source = source;
        this.separators = separators;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharKeepEmptySplitIteratorN(this.source, this.separators);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        if (this.state == 1)
            this.charMap = new ProbabilisticMap(this.separators);

        int index = this.state - 1;
        if (index < 0) {
            this.close();
            return false;
        }

        int length = this.source.length();
        int beginIndex = index;
        while (index < length) {
            char current = this.source.charAt(index);
            if (this.charMap.contains(current)) {
                this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
                this.state = index + 2;
                return true;
            }
            index++;
        }
        if (index == length) {
            this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
            this.state = index + 2;
            return true;
        }

        this.close();
        return false;
    }

    @Override
    public void close() {
        this.charMap = null;
        super.close();
    }
}


final class CharOmitEmptySplitIterator0 extends AbstractIterator<String> {
    private final CharSequence source;

    CharOmitEmptySplitIterator0(CharSequence source) {
        assert !Strings.isNullOrEmpty(source);
        this.source = source;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharOmitEmptySplitIterator0(this.source);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        int length = this.source.length();
        while (index < length) {
            char head = this.source.charAt(index);
            if (Character.isWhitespace(head)) {
                index++;
                continue;
            }
            int beginIndex = index++;
            while (index < length) {
                char tail = this.source.charAt(index);
                if (Character.isWhitespace(tail))
                    break;
                index++;
            }
            this.current = this.source.subSequence(beginIndex, index).toString();
            this.state = index + 1;
            return true;
        }

        this.close();
        return false;
    }
}


final class CharOmitEmptySplitIterator1 extends AbstractIterator<String> {
    private final CharSequence source;
    private final char separator;

    CharOmitEmptySplitIterator1(CharSequence source, char separator) {
        assert !Strings.isNullOrEmpty(source);
        this.source = source;
        this.separator = separator;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharOmitEmptySplitIterator1(this.source, this.separator);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        int length = this.source.length();
        while (index < length) {
            char head = this.source.charAt(index);
            if (head == this.separator) {
                index++;
                continue;
            }
            int beginIndex = index++;
            while (index < length) {
                char tail = this.source.charAt(index);
                if (tail == this.separator)
                    break;
                index++;
            }
            this.current = this.source.subSequence(beginIndex, index).toString();
            this.state = index + 1;
            return true;
        }

        this.close();
        return false;
    }
}


final class CharOmitEmptySplitIterator2 extends AbstractIterator<String> {
    private final CharSequence source;
    private final char separator0;
    private final char separator1;

    CharOmitEmptySplitIterator2(CharSequence source, char separator0, char separator1) {
        assert !Strings.isNullOrEmpty(source);
        this.source = source;
        this.separator0 = separator0;
        this.separator1 = separator1;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharOmitEmptySplitIterator2(this.source, this.separator0, this.separator1);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        int length = this.source.length();
        while (index < length) {
            char head = this.source.charAt(index);
            if (head == this.separator0 || head == this.separator1) {
                index++;
                continue;
            }
            int beginIndex = index++;
            while (index < length) {
                char tail = this.source.charAt(index);
                if (tail == this.separator0 || tail == this.separator1)
                    break;
                index++;
            }
            this.current = this.source.subSequence(beginIndex, index).toString();
            this.state = index + 1;
            return true;
        }

        this.close();
        return false;
    }
}


final class CharOmitEmptySplitIterator3 extends AbstractIterator<String> {
    private final CharSequence source;
    private final char separator0;
    private final char separator1;
    private final char separator2;

    CharOmitEmptySplitIterator3(CharSequence source, char separator0, char separator1, char separator2) {
        assert !Strings.isNullOrEmpty(source);
        this.source = source;
        this.separator0 = separator0;
        this.separator1 = separator1;
        this.separator2 = separator2;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharOmitEmptySplitIterator3(this.source, this.separator0, this.separator1, this.separator2);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        int length = this.source.length();
        while (index < length) {
            char head = this.source.charAt(index);
            if (head == this.separator0 || head == this.separator1 || head == this.separator2) {
                index++;
                continue;
            }
            int beginIndex = index++;
            while (index < length) {
                char tail = this.source.charAt(index);
                if (tail == this.separator0 || tail == this.separator1 || tail == this.separator2)
                    break;
                index++;
            }
            this.current = this.source.subSequence(beginIndex, index).toString();
            this.state = index + 1;
            return true;
        }

        this.close();
        return false;
    }
}


final class CharOmitEmptySplitIteratorN extends AbstractIterator<String> {
    private final CharSequence source;
    private final char[] separators;
    private ProbabilisticMap charMap;

    CharOmitEmptySplitIteratorN(CharSequence source, char[] separators) {
        assert !Strings.isNullOrEmpty(source);
        assert separators != null && separators.length > 3;
        this.source = source;
        this.separators = separators;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new CharOmitEmptySplitIteratorN(this.source, this.separators);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        if (this.state == 1)
            this.charMap = new ProbabilisticMap(this.separators);

        int index = this.state - 1;
        int length = this.source.length();
        while (index < length) {
            char head = this.source.charAt(index);
            if (this.charMap.contains(head)) {
                index++;
                continue;
            }
            int beginIndex = index++;
            while (index < length) {
                char tail = this.source.charAt(index);
                if (this.charMap.contains(tail))
                    break;
                index++;
            }
            this.current = this.source.subSequence(beginIndex, index).toString();
            this.state = index + 1;
            return true;
        }

        this.close();
        return false;
    }

    @Override
    public void close() {
        this.charMap = null;
        super.close();
    }
}


final class StringKeepEmptySplitIterator1 extends AbstractIterator<String> {
    private final CharSequence source;
    private final CharSequence separator;

    StringKeepEmptySplitIterator1(CharSequence source, CharSequence separator) {
        assert source != null;
        assert !Strings.isNullOrEmpty(separator);
        this.source = source;
        this.separator = separator;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new StringKeepEmptySplitIterator1(this.source, this.separator);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        if (index < 0) {
            this.close();
            return false;
        }

        int length = this.source.length();
        char firstSepChar = this.separator.charAt(0);
        int currentSepLength = this.separator.length();
        int beginIndex = index;
        while (index < length) {
            char current = this.source.charAt(index);
            if (current == firstSepChar && currentSepLength <= length - index && (currentSepLength == 1 || SplitInternal.sequenceEqual(this.source, index + 1, this.separator, 1, currentSepLength - 1))) {
                this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
                this.state = index + currentSepLength + 1;
                return true;
            }
            index++;
        }
        if (index == length) {
            this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
            this.state = index + 2;
            return true;
        }

        this.close();
        return false;
    }
}


final class StringKeepEmptySplitIteratorN extends AbstractIterator<String> {
    private final CharSequence source;
    private final CharSequence[] separators;

    StringKeepEmptySplitIteratorN(CharSequence source, CharSequence[] separators) {
        assert source != null;
        assert separators != null && separators.length > 0;
        this.source = source;
        this.separators = separators;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new StringKeepEmptySplitIteratorN(this.source, this.separators);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        if (index < 0) {
            this.close();
            return false;
        }

        int length = this.source.length();
        int beginIndex = index;
        while (index < length) {
            char current = this.source.charAt(index);
            for (CharSequence separator : this.separators) {
                if (Strings.isNullOrEmpty(separator))
                    continue;
                int currentSepLength = separator.length();
                if (current == separator.charAt(0) && currentSepLength <= length - index && (currentSepLength == 1 || SplitInternal.sequenceEqual(this.source, index + 1, separator, 1, currentSepLength - 1))) {
                    this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
                    this.state = index + currentSepLength + 1;
                    return true;
                }
            }
            index++;
        }
        if (index == length) {
            this.current = index == beginIndex ? Strings.Empty : this.source.subSequence(beginIndex, index).toString();
            this.state = index + 2;
            return true;
        }

        this.close();
        return false;
    }
}


final class StringOmitEmptySplitIterator1 extends AbstractIterator<String> {
    private final CharSequence source;
    private final CharSequence separator;

    StringOmitEmptySplitIterator1(CharSequence source, CharSequence separator) {
        assert !Strings.isNullOrEmpty(source);
        assert !Strings.isNullOrEmpty(separator);
        this.source = source;
        this.separator = separator;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new StringOmitEmptySplitIterator1(this.source, this.separator);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        int length = this.source.length();
        char firstSepChar = this.separator.charAt(0);
        int currentSepLength = this.separator.length();
        while (index < length) {
            char head = this.source.charAt(index);
            if (head == firstSepChar && currentSepLength <= length - index && (currentSepLength == 1 || SplitInternal.sequenceEqual(this.source, index + 1, this.separator, 1, currentSepLength - 1))) {
                index += currentSepLength;
                continue;
            }
            int beginIndex = index++;
            while (index < length) {
                char tail = this.source.charAt(index);
                if (tail == firstSepChar && currentSepLength <= length - index && (currentSepLength == 1 || SplitInternal.sequenceEqual(this.source, index + 1, this.separator, 1, currentSepLength - 1)))
                    break;
                index++;
            }
            this.current = this.source.subSequence(beginIndex, index).toString();
            this.state = index + 1;
            return true;
        }

        this.close();
        return false;
    }
}


final class StringOmitEmptySplitIteratorN extends AbstractIterator<String> {
    private final CharSequence source;
    private final CharSequence[] separators;

    StringOmitEmptySplitIteratorN(CharSequence source, CharSequence[] separators) {
        assert !Strings.isNullOrEmpty(source);
        assert separators != null && separators.length > 0;
        this.source = source;
        this.separators = separators;
    }

    @Override
    public AbstractIterator<String> clone() {
        return new StringOmitEmptySplitIteratorN(this.source, this.separators);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        int length = this.source.length();
        HEAD:
        while (index < length) {
            char head = this.source.charAt(index);
            for (CharSequence separator : this.separators) {
                if (Strings.isNullOrEmpty(separator))
                    continue;
                int currentSepLength = separator.length();
                if (head == separator.charAt(0) && currentSepLength <= length - index && (currentSepLength == 1 || SplitInternal.sequenceEqual(this.source, index + 1, separator, 1, currentSepLength - 1))) {
                    index += currentSepLength;
                    continue HEAD;
                }
            }
            int beginIndex = index++;
            TAIL:
            while (index < length) {
                char tail = this.source.charAt(index);
                for (CharSequence separator : this.separators) {
                    if (Strings.isNullOrEmpty(separator))
                        continue;
                    int currentSepLength = separator.length();
                    if (tail == separator.charAt(0) && currentSepLength <= length - index && (currentSepLength == 1 || SplitInternal.sequenceEqual(this.source, index + 1, separator, 1, currentSepLength - 1)))
                        break TAIL;
                }
                index++;
            }
            this.current = this.source.subSequence(beginIndex, index).toString();
            this.state = index + 1;
            return true;
        }

        this.close();
        return false;
    }
}


final class ProbabilisticMap {
    private static final int PROBABILISTICMAP_BLOCK_INDEX_MASK = 0x7;
    private static final int PROBABILISTICMAP_BLOCK_INDEX_SHIFT = 0x3;
    private static final int PROBABILISTICMAP_SIZE = 0x8;
    private final char[] chars;
    private final int[] charMap;

    ProbabilisticMap(char[] chars) {
        int[] charMap = new int[PROBABILISTICMAP_SIZE];
        boolean hasAscii = false;

        for (int c : chars) {
            // Map low bit
            setCharBit(charMap, (byte) c);

            // Map high bit
            c >>>= 8;

            if (c == 0)
                hasAscii = true;
            else
                setCharBit(charMap, (byte) c);
        }

        if (hasAscii) {
            // Common to search for ASCII symbols. Just set the high value once.
            charMap[0] |= 1;
        }

        this.chars = chars;
        this.charMap = charMap;
    }

    private static void setCharBit(int[] charMap, byte value) {
        charMap[value & PROBABILISTICMAP_BLOCK_INDEX_MASK] |= 1 << (value >>> PROBABILISTICMAP_BLOCK_INDEX_SHIFT);
    }

    private static boolean isCharBitSet(int[] charMap, byte value) {
        return (charMap[value & PROBABILISTICMAP_BLOCK_INDEX_MASK] & (1 << (value >>> PROBABILISTICMAP_BLOCK_INDEX_SHIFT))) != 0;
    }

    public boolean contains(char value) {
        return isCharBitSet(this.charMap, (byte) value)
                && isCharBitSet(this.charMap, (byte) (value >> 8))
                && SplitInternal.contains(this.chars, value);
    }
}


final class SplitInternal {
    private SplitInternal() {
    }

    public static boolean contains(char[] chars, char value) {
        for (char c : chars) {
            if (c == value)
                return true;
        }
        return false;
    }

    public static boolean sequenceEqual(CharSequence first, int firstStartIndex, CharSequence second, int secondStartIndex, int count) {
        int firstEndIndex = firstStartIndex + count;
        for (int i = firstStartIndex, j = secondStartIndex; i < firstEndIndex; i++, j++) {
            if (first.charAt(i) != second.charAt(j))
                return false;
        }
        return true;
    }
}
