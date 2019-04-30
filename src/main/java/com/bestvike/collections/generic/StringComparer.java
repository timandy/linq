package com.bestvike.collections.generic;

import com.bestvike.CultureInfo;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by 许崇雷 on 2019-04-29.
 */
public abstract class StringComparer implements Comparator<String>, IEqualityComparer<String> {
    public static final StringComparer CurrentCulture = new CultureAwareCaseSensitiveComparer(null);
    public static final StringComparer CurrentCultureIgnoreCase = new CultureAwareIgnoreCaseComparer(null);
    public static final StringComparer InvariantCulture = new CultureAwareCaseSensitiveComparer(CultureInfo.getInvariantCulture());
    public static final StringComparer InvariantCultureIgnoreCase = new CultureAwareIgnoreCaseComparer(CultureInfo.getInvariantCulture());
    public static final StringComparer Ordinal = new OrdinalCaseSensitiveComparer();
    public static final StringComparer OrdinalIgnoreCase = new OrdinalIgnoreCaseComparer();

    private StringComparer() {
    }

    public static StringComparer create(Collator collator, boolean ignoreCase) {
        if (collator == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.collator);
        return ignoreCase ? new CultureAwareIgnoreCaseComparer(collator) : new CultureAwareCaseSensitiveComparer(collator);
    }


    private static class CultureAwareComparer extends StringComparer {
        final Collator collator;
        final boolean ignoreCase;

        private CultureAwareComparer(Collator collator, boolean ignoreCase) {
            this.collator = collator;
            this.ignoreCase = ignoreCase;
        }

        @Override
        public boolean equals(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return true;
            if (x == null || y == null)
                return false;
            return this.ignoreCase ? x.equalsIgnoreCase(y) : x.equals(y);
        }

        @Override
        public int hashCode(String obj) {
            return obj == null ? 0 : (this.ignoreCase ? obj.toUpperCase().hashCode() : obj.hashCode());
        }

        @Override
        public int compare(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return 0;
            if (x == null)
                return -1;
            if (y == null)
                return 1;
            Collator collator = this.collator == null ? CultureInfo.getCurrentCulture() : this.collator;
            return this.ignoreCase ? collator.compare(x.toUpperCase(), y.toUpperCase()) : collator.compare(x, y);
        }
    }


    private static final class CultureAwareCaseSensitiveComparer extends CultureAwareComparer {
        private CultureAwareCaseSensitiveComparer(Collator collator) {
            super(collator, false);
        }

        @Override
        public boolean equals(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return true;
            if (x == null || y == null)
                return false;
            return x.equals(y);
        }

        @Override
        public int hashCode(String obj) {
            return obj == null ? 0 : obj.hashCode();
        }

        @Override
        public int compare(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return 0;
            if (x == null)
                return -1;
            if (y == null)
                return 1;
            return (this.collator == null ? CultureInfo.getCurrentCulture() : this.collator).compare(x, y);
        }
    }


    private static final class CultureAwareIgnoreCaseComparer extends CultureAwareComparer {
        private CultureAwareIgnoreCaseComparer(Collator collator) {
            super(collator, true);
        }

        @Override
        public boolean equals(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return true;
            if (x == null || y == null)
                return false;
            return x.equalsIgnoreCase(y);
        }

        @Override
        public int hashCode(String obj) {
            return obj == null ? 0 : obj.toUpperCase().hashCode();
        }

        @Override
        public int compare(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return 0;
            if (x == null)
                return -1;
            if (y == null)
                return 1;
            return (this.collator == null ? CultureInfo.getCurrentCulture() : this.collator).compare(x.toUpperCase(), y.toUpperCase());
        }
    }


    private static class OrdinalComparer extends StringComparer {
        final boolean ignoreCase;

        private OrdinalComparer(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
        }

        @Override
        public boolean equals(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return true;
            if (x == null || y == null)
                return false;
            return this.ignoreCase ? x.equalsIgnoreCase(y) : x.equals(y);
        }

        @Override
        public int hashCode(String obj) {
            return obj == null ? 0 : (this.ignoreCase ? obj.toUpperCase().hashCode() : obj.hashCode());
        }

        @Override
        public int compare(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return 0;
            if (x == null)
                return -1;
            if (y == null)
                return 1;
            return this.ignoreCase ? x.compareToIgnoreCase(y) : x.compareTo(y);
        }
    }


    private static final class OrdinalCaseSensitiveComparer extends OrdinalComparer {
        private OrdinalCaseSensitiveComparer() {
            super(false);
        }

        @Override
        public boolean equals(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return true;
            if (x == null || y == null)
                return false;
            return x.equals(y);
        }

        @Override
        public int hashCode(String obj) {
            return obj == null ? 0 : obj.hashCode();
        }

        @Override
        public int compare(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return 0;
            if (x == null)
                return -1;
            if (y == null)
                return 1;
            return x.compareTo(y);
        }
    }


    private static final class OrdinalIgnoreCaseComparer extends OrdinalComparer {
        private OrdinalIgnoreCaseComparer() {
            super(true);
        }

        @Override
        public boolean equals(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return true;
            if (x == null || y == null)
                return false;
            return x.equalsIgnoreCase(y);
        }

        @Override
        public int hashCode(String obj) {
            return obj == null ? 0 : obj.toUpperCase().hashCode();
        }

        @Override
        public int compare(String x, String y) {
            //noinspection StringEquality
            if (x == y)
                return 0;
            if (x == null)
                return -1;
            if (y == null)
                return 1;
            return x.compareToIgnoreCase(y);
        }
    }
}
