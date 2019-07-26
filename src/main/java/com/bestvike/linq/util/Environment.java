package com.bestvike.linq.util;

/**
 * Created by 许崇雷 on 2017-07-25.
 */
public final class Environment {
    /**
     * 获取 '\r' 字符
     */
    public static final char CR = '\r';
    /**
     * 获取 '\n' 字符
     */
    public static final char LF = '\n';
    /**
     * 获取当前系统换行符
     */
    public static final String NewLine = System.getProperty("line.separator");

    private Environment() {
    }
}
