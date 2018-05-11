package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ToCollectionTest extends IteratorTest {

    @Test
    public void testToArray() {
        Object[] source = {1, 2, 3};
        Integer[] target = Linq.asEnumerable(source).cast(Integer.class).toArray(Integer.class);
        Assert.assertEquals(3, target.length);
        Assert.assertTrue(Linq.asEnumerable(source).sequenceEqual(Linq.asEnumerable(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        Integer[] target2 = Linq.asEnumerable(source2).toArray(Integer.class);
        Assert.assertEquals(3, target2.length);
        Assert.assertTrue(Linq.asEnumerable(target2).sequenceEqual(Linq.asEnumerable(source2)));

        Character[] lst = Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).toArray(Character.class);
        Assert.assertEquals(5, lst.length);
        Assert.assertEquals("o", lst[4].toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Character[] arr = Linq.asEnumerable(arrChar).toArray(Character.class);
        Assert.assertEquals(5, arr.length);
        Assert.assertEquals("o", arr[4].toString());

        Character[] hello = Linq.asEnumerable("hello").toArray(Character.class);
        Assert.assertEquals(5, hello.length);
        Assert.assertEquals("o", hello[4].toString());

        Character[] h = Linq.singleton('h').toArray(Character.class);
        Assert.assertEquals(1, h.length);
        Assert.assertEquals("h", h[0].toString());

        Character[] empty = Linq.<Character>empty().toArray(Character.class);
        Assert.assertEquals(0, empty.length);
    }

    @Test
    public void testToList() {
        Object[] source = {1, 2, 3};
        List<Integer> target = Linq.asEnumerable(source).cast(Integer.class).toList();
        Assert.assertEquals(3, target.size());
        Assert.assertTrue(Linq.asEnumerable(source).cast(Integer.class).sequenceEqual(Linq.asEnumerable(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        List<Integer> target2 = Linq.asEnumerable(source2).toList();
        Assert.assertEquals(3, target2.size());
        Assert.assertTrue(Linq.asEnumerable(target2).sequenceEqual(Linq.asEnumerable(source2)));

        List<Character> lst = Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).toList();
        Assert.assertEquals(5, lst.size());
        Assert.assertEquals("o", lst.get(4).toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        List<Character> arr = Linq.asEnumerable(arrChar).toList();
        Assert.assertEquals(5, arr.size());
        Assert.assertEquals("o", arr.get(4).toString());

        List<Character> hello = Linq.asEnumerable("hello").toList();
        Assert.assertEquals(5, hello.size());
        Assert.assertEquals("o", hello.get(4).toString());

        List<Character> h = Linq.singleton('h').toList();
        Assert.assertEquals(1, h.size());
        Assert.assertEquals("h", h.get(0).toString());

        List<Character> empty = Linq.<Character>empty().toList();
        Assert.assertEquals(0, empty.size());
    }

    @Test
    public void testToMap() {
        Map<Integer, Employee> map = Linq.asEnumerable(emps).toMap(emp -> emp.empno);
        Assert.assertTrue(map.get(110).name.equals("Bill"));
        Assert.assertEquals(4, map.size());

        //key 重复,保留最后一个
        Map<Integer, Employee> map2 = Linq.asEnumerable(emps).toMap(emp -> emp.deptno);
        Assert.assertEquals(emps[3], map2.get(10));
        Assert.assertEquals(emps[1], map2.get(30));
        Assert.assertEquals(2, map2.size());
    }

    @Test
    public void testToMapWithSelector() {
        Map<Integer, String> map = Linq.asEnumerable(emps).toMap(emp -> emp.empno, emp -> emp.name);
        Assert.assertTrue(map.get(110).equals("Bill"));
        Assert.assertEquals(4, map.size());

        //key 重复,保留最后一个
        Map<Integer, String> map2 = Linq.asEnumerable(emps).toMap(emp -> emp.deptno, emp -> emp.name);
        Assert.assertEquals(emps[3].name, map2.get(10));
        Assert.assertEquals(emps[1].name, map2.get(30));
        Assert.assertEquals(2, map2.size());
    }

}