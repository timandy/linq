package com.bestvike.linq.exception;

import com.bestvike.linq.resources.SR;
import com.bestvike.linq.util.Environment;
import com.bestvike.linq.util.Strings;

/**
 * Created by 许崇雷 on 2017-07-11.
 */
public class ArgumentException extends RuntimeException {
    private String paramName;

    public ArgumentException() {
        super(SR.Arg_ArgumentException);
    }

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String message, Exception innerException) {
        super(message, innerException);
    }

    public ArgumentException(String message, String paramName, Exception innerException) {
        super(message, innerException);
        this.paramName = paramName;
    }

    public ArgumentException(String message, String paramName) {
        super(message);
        this.paramName = paramName;
    }

    public String getParamName() {
        return this.paramName;
    }

    @Override
    public String getMessage() {
        String s = super.getMessage();
        if (s == null)
            s = SR.Arg_ArgumentException;
        if (!Strings.isNullOrEmpty(this.paramName))
            s += Environment.NewLine + String.format(SR.Arg_ParamName_Name, this.paramName);
        return s;
    }
}
