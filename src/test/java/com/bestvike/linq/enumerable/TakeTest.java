package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import org.junit.Test;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class TakeTest extends TestCase {
    @Test
    public void testTake() {
        List<Department> enumerableDeptsResult = Linq.asEnumerable(depts).take(2).toList();
        assertEquals(2, enumerableDeptsResult.size());
        assertEquals(depts[0], enumerableDeptsResult.get(0));
        assertEquals(depts[1], enumerableDeptsResult.get(1));

        List<Department> enumerableDeptsResult5 = Linq.asEnumerable(depts).take(5).toList();
        assertEquals(3, enumerableDeptsResult5.size());
    }

    @Test
    public void testTakeWhile() {
        List<Department> deptList = Linq.asEnumerable(depts).takeWhile(dept -> dept.name.contains("e")).toList();
        // Only one department:
        // 0: Sales --> true
        // 1: HR --> false
        // 2: Marketing --> never get to it (we stop after false)
        assertEquals(1, deptList.size());
        assertEquals(depts[0], deptList.get(0));
    }

    @Test
    public void testTakeWhileIndexed() {
        List<Department> deptList = Linq.asEnumerable(depts).takeWhile((dept, index) -> index < 2).toList();
        assertEquals(2, deptList.size());
        assertEquals(depts[0], deptList.get(0));
        assertEquals(depts[1], deptList.get(1));
    }
}
