package com.bestvike.linq.exception;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public class ArgumentException extends RuntimeException {
    private String paramName;

    public String getParamName() {
        return this.paramName;
    }

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ArgumentException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ArgumentException(String paramName, String message) {
        super(message);
        this.paramName = paramName;
    }
}
