package com.bestvike.linq.entity;

import com.bestvike.ValueType;

/**
 * Created by 许崇雷 on 2017-07-24.
 */
public final class Employee extends ValueType {
    public final int empno;
    public final String name;
    public final Integer deptno;

    public Employee(int empno, String name, Integer deptno) {
        this.empno = empno;
        this.name = name;
        this.deptno = deptno;
    }
}
