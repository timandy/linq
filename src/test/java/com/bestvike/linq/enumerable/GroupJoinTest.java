package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class GroupJoinTest extends EnumerableTest {
    private static JoinRec createJoinOrderRec(CustomerRec cr, IEnumerable<OrderRec> orIE) {
        return new JoinRec(cr.name,
                orIE.select(o -> o.orderID).toArray(Integer.class),
                orIE.select(o -> o.total).toArray(Integer.class));
    }

    private static JoinRec createJoinAnagramRec(CustomerRec cr, IEnumerable<AnagramRec> arIE) {
        return new JoinRec(cr.name,
                arIE.select(o -> o.orderID).toArray(Integer.class),
                arIE.select(o -> o.total).toArray(Integer.class));
    }

    @Test
    public void OuterEmptyInnerNonEmpty() {
        CustomerRec[] outer = {};
        OrderRec[] inner = {
                new OrderRec(45321, 98022, 50),
                new OrderRec(97865, 32103, 25)};

        assertEmpty(Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, GroupJoinTest::createJoinOrderRec));
    }

    @Test
    public void CustomComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};
        JoinRec[] expected = {
                new JoinRec("Tim", new Integer[]{93489}, new Integer[]{45}),
                new JoinRec("Bob", new Integer[]{}, new Integer[]{}),
                new JoinRec("Robert", new Integer[]{93483}, new Integer[]{19})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.name, e -> e.name, GroupJoinTest::createJoinAnagramRec, new AnagramEqualityComparer()));
    }

    @Test
    public void OuterNull() {
        IEnumerable<CustomerRec> outer = null;
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};

        assertThrows(NullPointerException.class, () -> outer.groupJoin(Linq.asEnumerable(inner), e -> e.name, e -> e.name, GroupJoinTest::createJoinAnagramRec, new AnagramEqualityComparer()));
    }

    @Test
    public void InnerNull() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        IEnumerable<AnagramRec> inner = null;

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).groupJoin(inner, e -> e.name, e -> e.name, GroupJoinTest::createJoinAnagramRec, new AnagramEqualityComparer()));
    }

    @Test
    public void OuterKeySelectorNull() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), null, e -> e.name, GroupJoinTest::createJoinAnagramRec, new AnagramEqualityComparer()));
    }

    @Test
    public void InnerKeySelectorNull() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.name, null, GroupJoinTest::createJoinAnagramRec, new AnagramEqualityComparer()));
    }

    @Test
    public void ResultSelectorNull() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        AnagramRec[] inner = new AnagramRec[]
                {
                        new AnagramRec("Robert", 93483, 19),
                        new AnagramRec("miT", 93489, 45)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.name, e -> e.name, (Func2<CustomerRec, IEnumerable<AnagramRec>, JoinRec>) null, new AnagramEqualityComparer()));
    }

    @Test
    public void OuterNullNoComparer() {
        IEnumerable<CustomerRec> outer = null;
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};

        assertThrows(NullPointerException.class, () -> outer.groupJoin(Linq.asEnumerable(inner), e -> e.name, e -> e.name, GroupJoinTest::createJoinAnagramRec));
    }

    @Test
    public void InnerNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        IEnumerable<AnagramRec> inner = null;

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).groupJoin(inner, e -> e.name, e -> e.name, GroupJoinTest::createJoinAnagramRec));
    }

    @Test
    public void OuterKeySelectorNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), null, e -> e.name, GroupJoinTest::createJoinAnagramRec));
    }

    @Test
    public void InnerKeySelectorNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.name, null, GroupJoinTest::createJoinAnagramRec));
    }

    @Test
    public void ResultSelectorNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.name, e -> e.name, (Func2<CustomerRec, IEnumerable<AnagramRec>, JoinRec>) null));
    }

    @Test
    public void OuterInnerBothSingleNullElement() {
        String[] outer = new String[]{null};
        String[] inner = new String[]{null};
        String[] expected = new String[]{null};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e, e -> e, (x, y) -> x, EqualityComparer.Default()));
    }

    @Test
    public void OuterNonEmptyInnerEmpty() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 43434),
                new CustomerRec("Bob", 34093)};
        OrderRec[] inner = {};
        JoinRec[] expected = {
                new JoinRec("Tim", new Integer[]{}, new Integer[]{}),
                new JoinRec("Bob", new Integer[]{}, new Integer[]{})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, GroupJoinTest::createJoinOrderRec));
    }

    @Test
    public void SingleElementEachAndMatches() {
        CustomerRec[] outer = {new CustomerRec("Tim", 43434)};
        OrderRec[] inner = {new OrderRec(97865, 43434, 25)};
        JoinRec[] expected = {new JoinRec("Tim", new Integer[]{97865}, new Integer[]{25})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, GroupJoinTest::createJoinOrderRec));
    }

    @Test
    public void SingleElementEachAndDoesntMatch() {
        CustomerRec[] outer = {new CustomerRec("Tim", 43434)};
        OrderRec[] inner = {new OrderRec(97865, 49434, 25)};
        JoinRec[] expected = {new JoinRec("Tim", new Integer[]{}, new Integer[]{})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, GroupJoinTest::createJoinOrderRec));
    }

    @Test
    public void SelectorsReturnNull() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", null),
                new CustomerRec("Bob", null)};
        OrderRec[] inner = {
                new OrderRec(97865, null, 25),
                new OrderRec(34390, null, 19)};
        JoinRec[] expected = {
                new JoinRec("Tim", new Integer[]{}, new Integer[]{}),
                new JoinRec("Bob", new Integer[]{}, new Integer[]{})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, GroupJoinTest::createJoinOrderRec));
    }

    @Test
    public void InnerSameKeyMoreThanOneElementAndMatches() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865)};
        OrderRec[] inner = {
                new OrderRec(97865, 1234, 25),
                new OrderRec(34390, 1234, 19),
                new OrderRec(34390, 9865, 19)};
        JoinRec[] expected = {
                new JoinRec("Tim", new Integer[]{97865, 34390}, new Integer[]{25, 19}),
                new JoinRec("Bob", new Integer[]{34390}, new Integer[]{19})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, GroupJoinTest::createJoinOrderRec));
    }

    @Test
    public void InnerSameKeyMoreThanOneElementAndMatchesRunOnce() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865)};
        OrderRec[] inner = {
                new OrderRec(97865, 1234, 25),
                new OrderRec(34390, 1234, 19),
                new OrderRec(34390, 9865, 19)};
        JoinRec[] expected = {
                new JoinRec("Tim", new Integer[]{97865, 34390}, new Integer[]{25, 19}),
                new JoinRec("Bob", new Integer[]{34390}, new Integer[]{19})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).runOnce().groupJoin(Linq.asEnumerable(inner).runOnce(), e -> e.custID, e -> e.custID, GroupJoinTest::createJoinOrderRec));
    }

    @Test
    public void OuterSameKeyMoreThanOneElementAndMatches() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9865)};
        OrderRec[] inner = {
                new OrderRec(97865, 1234, 25),
                new OrderRec(34390, 9865, 19)};
        JoinRec[] expected = {
                new JoinRec("Tim", new Integer[]{97865}, new Integer[]{25}),
                new JoinRec("Bob", new Integer[]{34390}, new Integer[]{19}),
                new JoinRec("Robert", new Integer[]{34390}, new Integer[]{19})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, GroupJoinTest::createJoinOrderRec));
    }

    @Test
    public void NoMatches() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        OrderRec[] inner = {
                new OrderRec(97865, 2334, 25),
                new OrderRec(34390, 9065, 19)};
        JoinRec[] expected = {
                new JoinRec("Tim", new Integer[]{}, new Integer[]{}),
                new JoinRec("Bob", new Integer[]{}, new Integer[]{}),
                new JoinRec("Robert", new Integer[]{}, new Integer[]{})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, GroupJoinTest::createJoinOrderRec));
    }

    @Test
    public void NullComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};
        JoinRec[] expected = {
                new JoinRec("Tim", new Integer[]{}, new Integer[]{}),
                new JoinRec("Bob", new Integer[]{}, new Integer[]{}),
                new JoinRec("Robert", new Integer[]{93483}, new Integer[]{19})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).groupJoin(Linq.asEnumerable(inner), e -> e.name, e -> e.name, GroupJoinTest::createJoinAnagramRec, null));
    }

    @Test
    public void NullComparerRunOnce() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 1234),
                new CustomerRec("Bob", 9865),
                new CustomerRec("Robert", 9895)};
        AnagramRec[] inner = {
                new AnagramRec("Robert", 93483, 19),
                new AnagramRec("miT", 93489, 45)};
        JoinRec[] expected = {
                new JoinRec("Tim", new Integer[]{}, new Integer[]{}),
                new JoinRec("Bob", new Integer[]{}, new Integer[]{}),
                new JoinRec("Robert", new Integer[]{93483}, new Integer[]{19})};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).runOnce().groupJoin(Linq.asEnumerable(inner).runOnce(), e -> e.name, e -> e.name, GroupJoinTest::createJoinAnagramRec, null));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<IEnumerable<Integer>> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).groupJoin(Linq.<Integer>empty(), i -> i, i -> i, (o, i) -> i);

        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<IEnumerable<Integer>> en = (IEnumerator<IEnumerable<Integer>>) iterator;
        Assert.assertFalse(en != null && en.moveNext());
    }

    @Test
    public void testGroupJoin() {
        //左连接,empty dept 保留,bad Emp 被滤掉
        String s = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)).groupJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                dept -> dept.deptno,
                emp -> emp.deptno,
                (dept, emps) -> {
                    final StringBuilder buf = new StringBuilder("[");
                    int n = 0;
                    for (Employee employee : emps) {
                        if (n++ > 0)
                            buf.append(", ");
                        buf.append(employee.name);
                    }
                    return buf.append("] work(s) in ").append(dept.name).toString();
                })
                .toList()
                .toString();
        Assert.assertEquals("[[Fred, Eric, Janet] work(s) in Sales, [] work(s) in HR, [Bill] work(s) in Marketing, [] work(s) in Manager]", s);
    }

    @Test
    public void testGroupJoinWithComparer() {
        //交叉连接,但 null key 不参与交叉
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

        String s = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .groupJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emps) -> {
                            final StringBuilder buf = new StringBuilder("[");
                            int n = 0;
                            for (Employee employee : emps) {
                                if (n++ > 0)
                                    buf.append(", ");
                                buf.append(employee.name);
                            }
                            return buf.append("] work(s) in ").append(dept.name)
                                    .toString();
                        },
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[[Fred, Bill, Eric, Janet, Cedric] work(s) in Sales, [Fred, Bill, Eric, Janet, Cedric] work(s) in HR, [Fred, Bill, Eric, Janet, Cedric] work(s) in Marketing, [] work(s) in Manager]", s);
    }

    //struct
    public static final class CustomerRec {
        final String name;
        final Integer custID;

        CustomerRec(String name, Integer custID) {
            this.name = name;
            this.custID = custID;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || this.getClass() != obj.getClass())
                return false;
            CustomerRec that = (CustomerRec) obj;
            return Objects.equals(this.name, that.name) &&
                    Objects.equals(this.custID, that.custID);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.custID);
        }
    }

    //struct
    public static final class OrderRec {
        final Integer orderID;
        final Integer custID;
        final Integer total;

        OrderRec(Integer orderID, Integer custID, Integer total) {
            this.orderID = orderID;
            this.custID = custID;
            this.total = total;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || this.getClass() != obj.getClass())
                return false;
            OrderRec orderRec = (OrderRec) obj;
            return Objects.equals(this.orderID, orderRec.orderID) &&
                    Objects.equals(this.custID, orderRec.custID) &&
                    Objects.equals(this.total, orderRec.total);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.orderID, this.custID, this.total);
        }
    }

    //struct
    public static final class AnagramRec {
        final String name;
        final Integer orderID;
        final Integer total;

        AnagramRec(String name, Integer orderID, Integer total) {
            this.name = name;
            this.orderID = orderID;
            this.total = total;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || this.getClass() != obj.getClass())
                return false;
            AnagramRec that = (AnagramRec) obj;
            return Objects.equals(this.name, that.name) &&
                    Objects.equals(this.orderID, that.orderID) &&
                    Objects.equals(this.total, that.total);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.orderID, this.total);
        }
    }

    //struct
    public static final class JoinRec {
        final String name;
        final Integer[] orderID;
        final Integer[] total;

        JoinRec(String name, Integer[] orderID, Integer[] total) {
            this.name = name;
            this.orderID = orderID;
            this.total = total;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || this.getClass() != obj.getClass())
                return false;
            JoinRec joinRec = (JoinRec) obj;
            return Objects.equals(this.name, joinRec.name) &&
                    Arrays.equals(this.orderID, joinRec.orderID) &&
                    Arrays.equals(this.total, joinRec.total);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(this.name);
            result = 31 * result + Arrays.hashCode(this.orderID);
            result = 31 * result + Arrays.hashCode(this.total);
            return result;
        }
    }
}
