package com.bestvike.linq.exception;

import com.bestvike.linq.resources.SR;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public final class ArgumentNullException extends ArgumentException {
    public ArgumentNullException() {
        super(SR.ArgumentNull_Generic);
    }

    public ArgumentNullException(String paramName) {
        super(SR.ArgumentNull_Generic, paramName);
    }

    public ArgumentNullException(String message, Exception innerException) {
        super(message, innerException);
    }

    public ArgumentNullException(String paramName, String message) {
        super(message, paramName);
    }
}
