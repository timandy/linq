package com.bestvike.linq.entity;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2017/7/24.
 */
public final class Employee {
    public final int empno;
    public final String name;
    public final Integer deptno;

    public Employee(int empno, String name, Integer deptno) {
        this.empno = empno;
        this.name = name;
        this.deptno = deptno;
    }

    public String toString() {
        return String.format("Employee(name: %s, deptno:%d)", this.name, this.deptno);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.deptno == null ? 0 : this.deptno.hashCode());
        result = prime * result + this.empno;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Employee other = (Employee) obj;
        if (!Objects.equals(this.deptno, other.deptno)) {
            return false;
        }
        if (this.empno != other.empno) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
