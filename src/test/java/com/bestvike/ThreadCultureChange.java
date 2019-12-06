package com.bestvike;

import java.util.Locale;

public final class ThreadCultureChange implements IDisposable {
    private Locale origLocale = Locale.getDefault();

    public ThreadCultureChange(Locale newLocale) {
        if (newLocale != null) {
            this.origLocale = Locale.getDefault();
            Locale.setDefault(newLocale);
        }
    }

    public void close() {
        Locale.setDefault(this.origLocale);
    }
}
