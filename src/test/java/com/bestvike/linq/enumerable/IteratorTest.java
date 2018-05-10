package com.bestvike.linq.enumerable;

import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public abstract class IteratorTest {
    protected static final Employee[] badEmps = {
            new Employee(140, "Cedric", 40),
            new Employee(150, "Gates", null)};
    protected static final Department[] badDepts = {
            new Department("Manager", null, Collections.emptyList())};
    protected static final Employee[] emps = {
            new Employee(100, "Fred", 10),
            new Employee(110, "Bill", 30),
            new Employee(120, "Eric", 10),
            new Employee(130, "Janet", 10)};
    protected static final Department[] depts = {
            new Department("Sales", 10, Arrays.asList(emps[0], emps[2], emps[3])),
            new Department("HR", 20, Collections.emptyList()),
            new Department("Marketing", 30, Collections.singletonList(emps[1]))};

    protected static String stringJoin(Iterable<String> group) {
        final StringBuilder builder = new StringBuilder();
        final Iterator<String> iterator = group.iterator();
        if (iterator.hasNext()) {
            builder.append(iterator.next());
            while (iterator.hasNext()) {
                builder.append("+").append(iterator.next());
            }
        }
        return builder.toString();
    }
}
