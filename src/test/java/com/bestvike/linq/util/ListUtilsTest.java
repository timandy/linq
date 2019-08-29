package com.bestvike.linq.util;

import com.bestvike.TestCase;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Created by 许崇雷 on 2019-07-12.
 */
public class ListUtilsTest extends TestCase {
    @Test
    public void addRange() {
        assertThrows(ArgumentNullException.class, () -> ListUtils.addRange(null, null));
        assertThrows(ArgumentNullException.class, () -> ListUtils.addRange(Arrays.asList(1, 2), null));
    }
}
