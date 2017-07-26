package com.bestvike.linq.util;

/**
 * @author 许崇雷
 * @date 2017/7/25
 */
public final class Environment {
    private Environment() {
    }

    /**
     * 获取当前系统换行符
     */
    public static final String NewLine = System.getProperty("line.separator");
}
