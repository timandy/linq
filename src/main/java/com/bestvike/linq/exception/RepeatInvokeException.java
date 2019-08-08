package com.bestvike.linq.exception;

import com.bestvike.linq.resources.SR;

/**
 * Created by 许崇雷 on 2019-08-08.
 */
public final class RepeatInvokeException extends RuntimeException {
    public RepeatInvokeException() {
        super(SR.Arg_RepeatInvokeException);
    }

    public RepeatInvokeException(String message) {
        super(message);
    }

    public RepeatInvokeException(String message, Exception innerException) {
        super(message, innerException);
    }
}
