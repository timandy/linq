package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.NotSupportedException;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-05-09.
 */
class EmptyPartitionTest extends TestCase {
    private static <T> IEnumerable<T> GetEmptyPartition() {
        return Linq.of((T[]) new Object[0]).take(0);
    }

    @Test
    void EmptyPartitionIsEmpty() {
        assertEmpty(EmptyPartitionTest.<Integer>GetEmptyPartition());
        assertEmpty(EmptyPartitionTest.<String>GetEmptyPartition());
    }

    @Test
    void SingleInstance() {
        // .NET Core returns the instance as an optimization.
        // see https://github.com/dotnet/corefx/pull/2401.
        assertSame(EmptyPartitionTest.<Integer>GetEmptyPartition(), EmptyPartitionTest.<Integer>GetEmptyPartition());
    }

    @Test
    void SkipSame() {
        IEnumerable<Integer> empty = EmptyPartitionTest.GetEmptyPartition();
        assertSame(empty, empty.skip(2));
    }

    @Test
    void TakeSame() {
        IEnumerable<Integer> empty = EmptyPartitionTest.GetEmptyPartition();
        assertSame(empty, empty.take(2));
    }

    @Test
    void ElementAtThrows() {
        assertThrows(ArgumentOutOfRangeException.class, () -> EmptyPartitionTest.<Integer>GetEmptyPartition().elementAt(0));
    }

    @Test
    void ElementAtOrDefaultIsDefault() {
        assertNull(EmptyPartitionTest.<Integer>GetEmptyPartition().elementAtOrDefault(0));
        assertNull(EmptyPartitionTest.<String>GetEmptyPartition().elementAtOrDefault(0));
    }

    @Test
    void FirstThrows() {
        assertThrows(InvalidOperationException.class, () -> EmptyPartitionTest.<Integer>GetEmptyPartition().first());
    }

    @Test
    void FirstOrDefaultIsDefault() {
        assertNull(EmptyPartitionTest.<Integer>GetEmptyPartition().firstOrDefault());
        assertNull(EmptyPartitionTest.<String>GetEmptyPartition().firstOrDefault());
    }

    @Test
    void LastThrows() {
        assertThrows(InvalidOperationException.class, () -> EmptyPartitionTest.<Integer>GetEmptyPartition().last());
    }

    @Test
    void LastOrDefaultIsDefault() {
        assertNull(EmptyPartitionTest.<Integer>GetEmptyPartition().lastOrDefault());
        assertNull(EmptyPartitionTest.<String>GetEmptyPartition().lastOrDefault());
    }

    @Test
    void ToArrayEmpty() {
        assertEmpty(EmptyPartitionTest.<Integer>GetEmptyPartition().toArray());
    }

    @Test
    void ToListEmpty() {
        assertEquals(0, EmptyPartitionTest.<Integer>GetEmptyPartition().toList().size());
    }

    @Test
    void ResetIsNop() {
        IEnumerator<Integer> en = EmptyPartitionTest.<Integer>GetEmptyPartition().enumerator();
        assertThrows(NotSupportedException.class, en::reset);
    }
}
