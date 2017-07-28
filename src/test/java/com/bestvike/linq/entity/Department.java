package com.bestvike.linq.entity;

import java.util.List;

/**
 * Department.
 */
public final class Department {
    public final String name;
    public final Integer deptno;
    public final List<Employee> employees;

    public Department(String name, Integer deptno, List<Employee> employees) {
        this.name = name;
        this.deptno = deptno;
        this.employees = employees;
    }

    public String toString() {
        return String.format("Department(name: %s, deptno:%d, employees: %s)", this.name, this.deptno, this.employees);
    }
}
