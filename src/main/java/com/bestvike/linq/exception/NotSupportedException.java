package com.bestvike.linq.exception;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public final class NotSupportedException extends RuntimeException {
    public NotSupportedException() {
        super("Specified method is not supported.");
    }

    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(String message, Exception innerException) {
        super(message, innerException);
    }
}
