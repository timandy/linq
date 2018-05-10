package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class GroupByTest extends IteratorTest {


    @Test
    public void testGroupByWithKeySelector() {
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno)
                .select(group -> String.format("%s: %s", group.getKey(), stringJoin(group.select(element -> element.name))))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };

        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno, comparer)
                .select(group -> String.format("%s: %s", group.getKey(), stringJoin(group.select(element -> element.name))))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelector() {
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno, emp -> emp.name)
                .select(group -> String.format("%s: %s", group.getKey(), stringJoin(group)))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelectorAndComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno, emp -> emp.name, comparer)
                .select(group -> String.format("%s: %s", group.getKey(), stringJoin(group)))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndResultSelector() {
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno, (key, group) -> String.format("%s: %s", key, stringJoin(group.select(element -> element.name))))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndResultSelectorAndComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno,
                        (key, group) -> String.format("%s: %s", key, stringJoin(group.select(element -> element.name))),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelectorAndResultSelector() {
        String s =
                Linq.asEnumerable(emps)
                        .groupBy(emp -> emp.deptno, emp -> emp.name, (key, group) -> String.format("%s: %s", key, stringJoin(group)))
                        .toList()
                        .toString();
        Assert.assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelectorAndResultSelectorAndComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno,
                        emp -> emp.name,
                        (key, group) -> String.format("%s: %s", key, stringJoin(group)),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

}