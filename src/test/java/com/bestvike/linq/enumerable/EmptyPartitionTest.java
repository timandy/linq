package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.NotSupportedException;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-05-09.
 */
public class EmptyPartitionTest extends TestCase {
    private static <T> IEnumerable<T> GetEmptyPartition() {
        return Linq.<T>empty().take(0);
    }

    @Test
    public void EmptyPartitionIsEmpty() {
        assertEmpty(EmptyPartitionTest.<Integer>GetEmptyPartition());
        assertEmpty(EmptyPartitionTest.<String>GetEmptyPartition());
    }

    @Test
    public void SingleInstance() {
        // .NET Core returns the instance as an optimization.
        // see https://github.com/dotnet/corefx/pull/2401.
        assertSame(EmptyPartitionTest.<Integer>GetEmptyPartition(), EmptyPartitionTest.<Integer>GetEmptyPartition());
    }

    @Test
    public void SkipSame() {
        IEnumerable<Integer> empty = EmptyPartitionTest.GetEmptyPartition();
        assertSame(empty, empty.skip(2));
    }

    @Test
    public void TakeSame() {
        IEnumerable<Integer> empty = EmptyPartitionTest.GetEmptyPartition();
        assertSame(empty, empty.take(2));
    }

    @Test
    public void ElementAtThrows() {
        assertThrows(ArgumentOutOfRangeException.class, () -> EmptyPartitionTest.<Integer>GetEmptyPartition().elementAt(0));
    }

    @Test
    public void ElementAtOrDefaultIsDefault() {
        assertNull(EmptyPartitionTest.<Integer>GetEmptyPartition().elementAtOrDefault(0));
        assertNull(EmptyPartitionTest.<String>GetEmptyPartition().elementAtOrDefault(0));
    }

    @Test
    public void FirstThrows() {
        assertThrows(InvalidOperationException.class, () -> EmptyPartitionTest.<Integer>GetEmptyPartition().first());
    }

    @Test
    public void FirstOrDefaultIsDefault() {
        assertNull(EmptyPartitionTest.<Integer>GetEmptyPartition().firstOrDefault());
        assertNull(EmptyPartitionTest.<String>GetEmptyPartition().firstOrDefault());
    }

    @Test
    public void LastThrows() {
        assertThrows(InvalidOperationException.class, () -> EmptyPartitionTest.<Integer>GetEmptyPartition().last());
    }

    @Test
    public void LastOrDefaultIsDefault() {
        assertNull(EmptyPartitionTest.<Integer>GetEmptyPartition().lastOrDefault());
        assertNull(EmptyPartitionTest.<String>GetEmptyPartition().lastOrDefault());
    }

    @Test
    public void ToArrayEmpty() {
        assertEmpty(EmptyPartitionTest.<Integer>GetEmptyPartition().toArray());
    }

    @Test
    public void ToListEmpty() {
        assertEquals(0, EmptyPartitionTest.<Integer>GetEmptyPartition().toList().size());
    }

    @Test
    public void ResetIsNop() {
        IEnumerator<Integer> en = EmptyPartitionTest.<Integer>GetEmptyPartition().enumerator();
        assertThrows(NotSupportedException.class, en::reset);
    }
}
