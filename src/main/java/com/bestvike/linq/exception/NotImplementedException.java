package com.bestvike.linq.exception;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public final class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super("not implemented.");
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
