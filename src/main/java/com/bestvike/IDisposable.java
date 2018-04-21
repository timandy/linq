package com.bestvike;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public interface IDisposable extends AutoCloseable {
    @Override
    void close();
}
