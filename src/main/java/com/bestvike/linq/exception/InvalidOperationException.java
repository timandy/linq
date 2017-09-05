package com.bestvike.linq.exception;

import com.bestvike.linq.resources.SR;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public final class InvalidOperationException extends RuntimeException {
    public InvalidOperationException() {
        super(SR.Arg_InvalidOperationException);
    }

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Exception innerException) {
        super(message, innerException);
    }
}
