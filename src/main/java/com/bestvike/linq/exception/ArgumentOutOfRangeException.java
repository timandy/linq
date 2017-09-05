package com.bestvike.linq.exception;

import com.bestvike.linq.resources.SR;
import com.bestvike.linq.util.Environment;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public final class ArgumentOutOfRangeException extends ArgumentException {
    private Object actualValue;

    public ArgumentOutOfRangeException() {
        super(SR.Arg_ArgumentOutOfRangeException);
    }

    public ArgumentOutOfRangeException(String paramName) {
        super(SR.Arg_ArgumentOutOfRangeException, paramName);
    }

    public ArgumentOutOfRangeException(String paramName, String message) {
        super(message, paramName);
    }

    public ArgumentOutOfRangeException(String message, Exception innerException) {
        super(message, innerException);
    }

    public ArgumentOutOfRangeException(String paramName, Object actualValue, String message) {
        super(message, paramName);
        this.actualValue = actualValue;
    }

    public Object getActualValue() {
        return this.actualValue;
    }

    @Override
    public String getMessage() {
        String s = super.getMessage();
        if (this.actualValue != null) {
            String valueMessage = String.format(SR.ArgumentOutOfRange_ActualValue, this.actualValue.toString());
            if (s == null)
                return valueMessage;
            return s + Environment.NewLine + valueMessage;
        }
        return s;
    }
}
