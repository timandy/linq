package com.bestvike;

import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

import java.util.Locale;

/**
 * Created by 许崇雷 on 2019-06-20.
 */
class CultureInfoTest extends TestCase {
    @Test
    void testSetCurrent() {
        CultureInfo.setCurrent(Locale.CHINA);
        assertSame(Locale.CHINA, CultureInfo.getCurrent());

        assertThrows(ArgumentNullException.class, () -> CultureInfo.setCurrent(null));
    }
}
