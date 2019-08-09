package com.bestvike.linq.util;

/**
 * Created by 许崇雷 on 2019-08-09.
 */
@SuppressWarnings({"AssertWithSideEffects", "ConstantConditions"})
public final class Assertion {
    private static final boolean enabled;

    static {
        boolean ea = false;
        assert ea = true;
        enabled = ea;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}
