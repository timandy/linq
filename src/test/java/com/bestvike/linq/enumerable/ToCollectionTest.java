package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ToCollectionTest extends TestCase {
    @Test
    public void testToArray() {
        Object[] source = {1, 2, 3};
        Object[] target = Linq.asEnumerable(source).cast(Integer.class).toArray(Integer.class);
        assertEquals(3, target.length);
        assertTrue(Linq.asEnumerable(source).sequenceEqual(Linq.asEnumerable(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        Integer[] target2 = Linq.asEnumerable(source2).toArray(Integer.class);
        assertEquals(3, target2.length);
        assertTrue(Linq.asEnumerable(target2).sequenceEqual(Linq.asEnumerable(source2)));

        Character[] lst = Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).toArray(Character.class);
        assertEquals(5, lst.length);
        assertEquals("o", lst[4].toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Character[] arr = Linq.asEnumerable(arrChar).toArray(Character.class);
        assertEquals(5, arr.length);
        assertEquals("o", arr[4].toString());

        Character[] hello = Linq.asEnumerable("hello").toArray(Character.class);
        assertEquals(5, hello.length);
        assertEquals("o", hello[4].toString());

        Character[] h = Linq.singleton('h').toArray(Character.class);
        assertEquals(1, h.length);
        assertEquals("h", h[0].toString());

        Character[] empty = Linq.<Character>empty().toArray(Character.class);
        assertEquals(0, empty.length);

        String[] array = {"a", "b"};
        Array<String> stringArray = Linq.asEnumerable(array).toArray();
        stringArray.set(0, "c");
        assertEquals("a", array[0]);
        assertEquals("c", stringArray.get(0));
    }

    @Test
    public void testToList() {
        Object[] source = {1, 2, 3};
        List<Integer> target = Linq.asEnumerable(source).cast(Integer.class).toList();
        assertEquals(3, target.size());
        assertTrue(Linq.asEnumerable(source).cast(Integer.class).sequenceEqual(Linq.asEnumerable(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        List<Integer> target2 = Linq.asEnumerable(source2).toList();
        assertEquals(3, target2.size());
        assertTrue(Linq.asEnumerable(target2).sequenceEqual(Linq.asEnumerable(source2)));

        List<Character> lst = Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).toList();
        assertEquals(5, lst.size());
        assertEquals("o", lst.get(4).toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        List<Character> arr = Linq.asEnumerable(arrChar).toList();
        assertEquals(5, arr.size());
        assertEquals("o", arr.get(4).toString());

        List<Character> hello = Linq.asEnumerable("hello").toList();
        assertEquals(5, hello.size());
        assertEquals("o", hello.get(4).toString());

        List<Character> h = Linq.singleton('h').toList();
        assertEquals(1, h.size());
        assertEquals("h", h.get(0).toString());

        List<Character> empty = Linq.<Character>empty().toList();
        assertEquals(0, empty.size());
    }

    @Test
    public void testToMap() {
        Map<Integer, Employee> map = Linq.asEnumerable(emps).toMap(emp -> emp.empno);
        assertTrue(map.get(110).name.equals("Bill"));
        assertEquals(4, map.size());

        //key 重复,保留最后一个
        Map<Integer, Employee> map2 = Linq.asEnumerable(emps).toMap(emp -> emp.deptno);
        assertEquals(emps[3], map2.get(10));
        assertEquals(emps[1], map2.get(30));
        assertEquals(2, map2.size());
    }

    @Test
    public void testToMapWithSelector() {
        Map<Integer, String> map = Linq.asEnumerable(emps).toMap(emp -> emp.empno, emp -> emp.name);
        assertTrue(map.get(110).equals("Bill"));
        assertEquals(4, map.size());

        //key 重复,保留最后一个
        Map<Integer, String> map2 = Linq.asEnumerable(emps).toMap(emp -> emp.deptno, emp -> emp.name);
        assertEquals(emps[3].name, map2.get(10));
        assertEquals(emps[1].name, map2.get(30));
        assertEquals(2, map2.size());
    }
}
