package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.tuple.Tuple;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SelectManyTest extends EnumerableTest {

    @Test
    public void testSelectMany() {
        final List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany(dept -> Linq.asEnumerable(dept.employees))
                .select((emp, index) -> String.format("#%d: %s", index, emp.name))
                .toList();
        Assert.assertEquals("[#0: Fred, #1: Eric, #2: Janet, #3: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManyIndexed() {
        final List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany((dept, index) -> Linq.asEnumerable(dept.employees).select(emp -> String.format("#%d: %s", index, emp.name)))
                .toList();
        Assert.assertEquals("[#0: Fred, #0: Eric, #0: Janet, #2: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManySelect() {
        final List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany(dept -> Linq.asEnumerable(dept.employees), (dept, emp) -> String.format("#%s: %s", dept.name, emp.name))
                .toList();
        Assert.assertEquals("[#Sales: Fred, #Sales: Eric, #Sales: Janet, #Marketing: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManyIndexedSelect() {
        final List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany((dept, index) -> Linq.asEnumerable(dept.employees).select(emp -> Tuple.create(index, emp)), (dept, empInfo) -> String.format("#%s: %s: %s", empInfo.getItem1(), dept.name, empInfo.getItem2().name))
                .toList();
        Assert.assertEquals("[#0: Sales: Fred, #0: Sales: Eric, #0: Sales: Janet, #2: Marketing: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManyCrossJoin() {
        //selectMany 实现简单的 cross join
        String cross = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .selectMany(emp -> Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)).select(dept -> String.format("%s works in %s", emp.name, dept.name)))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Fred works in Manager, Bill works in Sales, Bill works in HR, Bill works in Marketing, Bill works in Manager, Eric works in Sales, Eric works in HR, Eric works in Marketing, Eric works in Manager, Janet works in Sales, Janet works in HR, Janet works in Marketing, Janet works in Manager, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Cedric works in Manager, Gates works in Sales, Gates works in HR, Gates works in Marketing, Gates works in Manager]", cross);
    }
}