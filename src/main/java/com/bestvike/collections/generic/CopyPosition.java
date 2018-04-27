package com.bestvike.collections.generic;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2017-09-11.
 */
public final class CopyPosition {//struct
    private int row;
    private int column;

    CopyPosition(int row, int column) {
        assert row >= 0;
        assert column >= 0;
        this.row = row;
        this.column = column;
    }

    public static CopyPosition start() {
        return new CopyPosition(0, 0);
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public CopyPosition normalize(int endColumn) {
        assert this.column <= endColumn;
        return this.column == endColumn ? new CopyPosition(this.row + 1, 0) : this;
    }

    @Override
    public boolean equals(Object obj) {
        CopyPosition that = (CopyPosition) obj;
        return Objects.equals(this.row, that.row)
                && Objects.equals(this.column, that.column);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.row;
        result = prime * result + this.column;
        return result;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.row, this.column);
    }
}
