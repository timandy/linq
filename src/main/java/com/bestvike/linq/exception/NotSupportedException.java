package com.bestvike.linq.exception;

import com.bestvike.linq.resources.SR;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public final class NotSupportedException extends RuntimeException {
    public NotSupportedException() {
        super(SR.Arg_NotSupportedException);
    }

    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(String message, Exception innerException) {
        super(message, innerException);
    }
}
