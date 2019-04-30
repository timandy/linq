package com.bestvike;

import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by 许崇雷 on 2019-04-30.
 */
public final class CultureInfo {
    private static final Locale INVARIANT = Locale.ROOT;
    private static final Collator INVARIANT_CULTURE = Collator.getInstance(INVARIANT);
    private static volatile Locale CURRENT = Locale.getDefault();
    private static volatile Collator CURRENT_CULTURE = Collator.getInstance(CURRENT);

    private CultureInfo() {
    }

    public static Locale getCurrent() {
        return CURRENT;
    }

    public static void setCurrent(Locale locale) {
        if (locale == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.locale);
        CURRENT = locale;
        CURRENT_CULTURE = Collator.getInstance(locale);
    }

    public static Collator getCurrentCulture() {
        return CURRENT_CULTURE;
    }

    public static Locale getInvariant() {
        return INVARIANT;
    }

    public static Collator getInvariantCulture() {
        return INVARIANT_CULTURE;
    }
}
