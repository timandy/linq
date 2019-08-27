package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.Strings;

/**
 * Created by 许崇雷 on 2019-08-07.
 */
public final class Joining {
    private Joining() {
    }

    public static <TSource> String joining(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                return Strings.Empty;

            String text = String.valueOf(e.current());
            if (!e.moveNext())
                return text;

            StringBuilder sb = new StringBuilder(text);
            do {
                sb.append(e.current());
            }
            while (e.moveNext());

            return sb.toString();
        }
    }

    public static <TSource> String joining(IEnumerable<TSource> source, CharSequence separator) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                return Strings.Empty;

            String text = String.valueOf(e.current());
            if (!e.moveNext())
                return text;

            StringBuilder sb = new StringBuilder(text);
            if (Strings.isNullOrEmpty(separator)) {
                do {
                    sb.append(e.current());
                }
                while (e.moveNext());
            } else {
                do {
                    sb.append(separator);
                    sb.append(e.current());
                }
                while (e.moveNext());
            }

            return sb.toString();
        }
    }

    public static <TSource> String joining(IEnumerable<TSource> source, CharSequence separator, CharSequence prefix, CharSequence suffix) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        StringBuilder sb = new StringBuilder();
        if (!Strings.isNullOrEmpty(prefix))
            sb.append(prefix);

        if (Strings.isNullOrEmpty(separator)) {
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext())
                    sb.append(e.current());
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (e.moveNext()) {
                    sb.append(e.current());
                    while (e.moveNext()) {
                        sb.append(separator);
                        sb.append(e.current());
                    }
                }
            }
        }

        if (!Strings.isNullOrEmpty(suffix))
            sb.append(suffix);

        return sb.toString();
    }
}
