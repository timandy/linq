package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Func0;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.NotSupportedException;
import com.bestvike.ref;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class WhereTest extends TestCase {
    @Test
    public void Where_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> source = null;
        Func1<Integer, Boolean> simplePredicate = (value) -> true;
        Func2<Integer, Integer, Boolean> complexPredicate = (value, index) -> true;

        assertThrows(NullPointerException.class, () -> source.where(simplePredicate));
        assertThrows(NullPointerException.class, () -> source.where(complexPredicate));
    }

    @Test
    public void Where_PredicateIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> source = Linq.range(1, 10);
        Func1<Integer, Boolean> simplePredicate = null;
        Func2<Integer, Integer, Boolean> complexPredicate = null;

        assertThrows(ArgumentNullException.class, () -> source.where(simplePredicate));
        assertThrows(ArgumentNullException.class, () -> source.where(complexPredicate));
    }

    @Test
    public void Where_Array_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Func0<Boolean>[] source = new Func0[]{() -> {
            funcCalled.value = true;
            return true;
        }};

        IEnumerable<Func0<Boolean>> query = Linq.asEnumerable(source).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.asEnumerable(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Where_List_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        List<Func0<Boolean>> source = Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        });

        IEnumerable<Func0<Boolean>> query = Linq.asEnumerable(source).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.asEnumerable(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Where_IReadOnlyCollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Func0<Boolean>> source = Collections.unmodifiableCollection(Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        }));

        IEnumerable<Func0<Boolean>> query = Linq.asEnumerable(source).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.asEnumerable(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Where_ICollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Func0<Boolean>> source = new LinkedList<>(Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        }));

        IEnumerable<Func0<Boolean>> query = Linq.asEnumerable(source).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.asEnumerable(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Where_IEnumerable_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        IEnumerable<Func0<Boolean>> source = Linq.repeat(() -> {
            funcCalled.value = true;
            return true;
        }, 1);

        IEnumerable<Func0<Boolean>> query = source.where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = source.where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void WhereWhere_Array_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Func0<Boolean>[] source = new Func0[]{() -> {
            funcCalled.value = true;
            return true;
        }};

        IEnumerable<Func0<Boolean>> query = Linq.asEnumerable(source).where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.asEnumerable(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void WhereWhere_List_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        List<Func0<Boolean>> source = Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        });

        IEnumerable<Func0<Boolean>> query = Linq.asEnumerable(source).where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.asEnumerable(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void WhereWhere_IReadOnlyCollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Func0<Boolean>> source = Collections.unmodifiableCollection(Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        }));

        IEnumerable<Func0<Boolean>> query = Linq.asEnumerable(source).where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.asEnumerable(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void WhereWhere_ICollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Func0<Boolean>> source = new LinkedList<>(Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        }));

        IEnumerable<Func0<Boolean>> query = Linq.asEnumerable(source).where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.asEnumerable(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void WhereWhere_IEnumerable_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        IEnumerable<Func0<Boolean>> source = Linq.repeat(() -> {
            funcCalled.value = true;
            return true;
        }, 1);

        IEnumerable<Func0<Boolean>> query = source.where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = source.where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Where_Array_ReturnsExpectedValues_True() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(truePredicate);

        assertEquals(source.length, result.count());
        for (int i = 0; i < source.length; i++) {
            assertEquals(Linq.asEnumerable(source).elementAt(i), result.elementAt(i));
        }
    }

    @Test
    public void Where_Array_ReturnsExpectedValues_False() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Boolean> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    public void Where_Array_ReturnsExpectedValues_Complex() {
        int[] source = new int[]{2, 1, 3, 5, 4};
        Func2<Integer, Integer, Boolean> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void Where_List_ReturnsExpectedValues_True() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(truePredicate);

        assertEquals(source.size(), result.count());
        for (int i = 0; i < source.size(); i++) {
            assertEquals(Linq.asEnumerable(source).elementAt(i), result.elementAt(i));
        }
    }

    @Test
    public void Where_List_ReturnsExpectedValues_False() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Func1<Integer, Boolean> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    public void Where_List_ReturnsExpectedValues_Complex() {
        List<Integer> source = Arrays.asList(2, 1, 3, 5, 4);
        Func2<Integer, Integer, Boolean> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void Where_IReadOnlyCollection_ReturnsExpectedValues_True() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(truePredicate);

        assertEquals(source.size(), result.count());
        for (int i = 0; i < source.size(); i++) {
            assertEquals(Linq.asEnumerable(source).elementAt(i), result.elementAt(i));
        }
    }

    @Test
    public void Where_IReadOnlyCollection_ReturnsExpectedValues_False() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    public void Where_IReadOnlyCollection_ReturnsExpectedValues_Complex() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(2, 1, 3, 5, 4));
        Func2<Integer, Integer, Boolean> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void Where_ICollection_ReturnsExpectedValues_True() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(truePredicate);

        assertEquals(source.size(), result.count());
        for (int i = 0; i < source.size(); i++) {
            assertEquals(Linq.asEnumerable(source).elementAt(i), result.elementAt(i));
        }
    }

    @Test
    public void Where_ICollection_ReturnsExpectedValues_False() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    public void Where_ICollection_ReturnsExpectedValues_Complex() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(2, 1, 3, 5, 4));
        Func2<Integer, Integer, Boolean> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void Where_IEnumerable_ReturnsExpectedValues_True() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerable<Integer> result = source.where(truePredicate);

        assertEquals(source.count(), result.count());
        for (int i = 0; i < source.count(); i++) {
            assertEquals(source.elementAt(i), result.elementAt(i));
        }
    }

    @Test
    public void Where_IEnumerable_ReturnsExpectedValues_False() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Func1<Integer, Boolean> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = source.where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    public void Where_IEnumerable_ReturnsExpectedValues_Complex() {
        IEnumerable<Integer> source = Linq.asEnumerable(new LinkedList<>(Arrays.asList(2, 1, 3, 5, 4)));
        Func2<Integer, Integer, Boolean> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = source.where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void Where_EmptyEnumerable_ReturnsNoElements() {
        IEnumerable<Integer> source = Linq.empty();
        ref<Boolean> wasSelectorCalled = ref.init(false);

        IEnumerable<Integer> result = source.where(value -> {
            wasSelectorCalled.value = true;
            return true;
        });

        assertEquals(0, result.count());
        assertFalse(wasSelectorCalled.value);
    }

    @Test
    public void Where_EmptyEnumerable_ReturnsNoElementsWithIndex() {
        assertEmpty(Linq.<Integer>empty().where((e, i) -> true));
    }

    @Test
    public void Where_Array_CurrentIsDefaultOfTAfterEnumeration() {
        int[] source = new int[]{1};
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.asEnumerable(source).where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void Where_List_CurrentIsDefaultOfTAfterEnumeration() {
        List<Integer> source = Collections.singletonList(1);
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.asEnumerable(source).where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void Where_IReadOnlyCollection_CurrentIsDefaultOfTAfterEnumeration() {
        Collection<Integer> source = Collections.unmodifiableCollection(Collections.singletonList(1));
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.asEnumerable(source).where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void Where_ICollection_CurrentIsDefaultOfTAfterEnumeration() {
        Collection<Integer> source = new LinkedList<>(Collections.singletonList(1));
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.asEnumerable(source).where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void Where_IEnumerable_CurrentIsDefaultOfTAfterEnumeration() {
        IEnumerable<Integer> source = Linq.repeat(1, 1);
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = source.where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void WhereWhere_Array_ReturnsExpectedValues() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void WhereWhere_List_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void WhereWhere_IReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void WhereWhere_ICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void WhereWhere_IEnumerable_ReturnsExpectedValues() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = source.where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    public void WhereSelect_Array_ReturnsExpectedValues() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void WhereSelectSelect_Array_ReturnsExpectedValues() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void WhereSelect_List_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void WhereSelectSelect_List_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void WhereSelect_IReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void WhereSelectSelect_IReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void WhereSelect_ICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void WhereSelectSelect_ICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void WhereSelect_IEnumerable_ReturnsExpectedValues() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = source.where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void WhereSelectSelect_IEnumerable_ReturnsExpectedValues() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = source.where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    public void SelectWhere_Array_ReturnsExpectedValues() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    public void SelectWhere_List_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    public void SelectWhere_IReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    public void SelectWhere_ICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    public void SelectWhere_IEnumerable_ReturnsExpectedValues() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Func1<Integer, Boolean> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = source.select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    public void Where_PredicateThrowsException() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Boolean> predicate = value ->
        {
            if (value == 1) {
                throw new InvalidOperationException();
            }
            return true;
        };

        IEnumerator<Integer> enumerator = Linq.asEnumerable(source).where(predicate).enumerator();

        // Ensure the first MoveNext call throws an exception
        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());

        // Ensure Current is set to the default value of type T
        Integer currentValue = enumerator.current();
        assertEquals(null, currentValue);

        // Ensure subsequent MoveNext calls succeed
        assertTrue(enumerator.moveNext());
        assertEquals(2, enumerator.current());
    }

    @Test
    public void Where_SourceThrowsOnCurrent() {
        IEnumerable<Integer> source = new ThrowsOnCurrentEnumerator();
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = source.where(truePredicate).enumerator();

        // Ensure the first MoveNext call throws an exception
        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());

        // Ensure subsequent MoveNext calls succeed
        assertTrue(enumerator.moveNext());
        assertEquals(2, enumerator.current());
    }

    @Test
    public void Where_SourceThrowsOnMoveNext() {
        IEnumerable<Integer> source = new ThrowsOnMoveNext();
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = source.where(truePredicate).enumerator();

        // Ensure the first MoveNext call throws an exception
        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());

        // Ensure Current is set to the default value of type T
        Integer currentValue = enumerator.current();
        assertEquals(null, currentValue);

        // Ensure subsequent MoveNext calls succeed
        assertTrue(enumerator.moveNext());
        assertEquals(2, enumerator.current());
    }

    @Test
    public void Where_SourceThrowsOnGetEnumerator() {
        IEnumerable<Integer> source = new ThrowsOnGetEnumerator();
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = source.where(truePredicate).enumerator();

        // Ensure the first MoveNext call throws an exception
        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());

        // Ensure Current is set to the default value of type T
        Integer currentValue = enumerator.current();
        assertEquals(null, currentValue);

        // Ensure subsequent MoveNext calls succeed
        assertTrue(enumerator.moveNext());
        assertEquals(1, enumerator.current());
    }

    @Test
    public void Select_ResetEnumerator_ThrowsException() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        IEnumerator<Integer> enumerator = Linq.asEnumerable(source).where(value -> true).enumerator();

        // The full .NET Framework throws a NotImplementedException.
        // See https://github.com/dotnet/corefx/pull/2959.
        assertThrows(NotSupportedException.class, () -> enumerator.reset());
    }

    @Test
    public void Where_SourceThrowsOnConcurrentModification() {
        List<Integer> source = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Boolean> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.asEnumerable(source).where(truePredicate).enumerator();

        assertTrue(enumerator.moveNext());
        assertEquals(1, enumerator.current());

        source.add(6);
        assertThrows(ConcurrentModificationException.class, () -> enumerator.moveNext());
    }

    @Test
    public void Where_GetEnumeratorReturnsUniqueInstances() {
        int[] source = new int[]{1, 2, 3, 4, 5};

        IEnumerable<Integer> result = Linq.asEnumerable(source).where(value -> true);

        try (IEnumerator<Integer> enumerator1 = result.enumerator();
             IEnumerator<Integer> enumerator2 = result.enumerator()) {
            assertSame(result, enumerator1);
            assertNotSame(enumerator1, enumerator2);
        }
    }

    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE)
                .select(x -> x);

        assertEquals(q.where(TestCase::IsEven), q.where(TestCase::IsEven));

    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", null, "SoS", Empty})
                .select(x -> x);

        assertEquals(q.where(TestCase::IsNullOrEmpty), q.where(TestCase::IsNullOrEmpty));

    }

    @Test
    public void SingleElementPredicateFalse() {
        int[] source = {3};
        assertEmpty(Linq.asEnumerable(source).where(TestCase::IsEven));
    }

    @Test
    public void PredicateFalseForAll() {
        int[] source = {9, 7, 15, 3, 27};
        assertEmpty(Linq.asEnumerable(source).where(TestCase::IsEven));
    }

    @Test
    public void PredicateTrueFirstOnly() {
        int[] source = {10, 9, 7, 15, 3, 27};
        assertEquals(Linq.asEnumerable(source).take(1), Linq.asEnumerable(source).where(TestCase::IsEven));
    }

    @Test
    public void PredicateTrueLastOnly() {
        int[] source = {9, 7, 15, 3, 27, 20};
        assertEquals(Linq.asEnumerable(source).skip(source.length - 1), Linq.asEnumerable(source).where(TestCase::IsEven));
    }

    @Test
    public void PredicateTrueFirstThirdSixth() {
        int[] source = {20, 7, 18, 9, 7, 10, 21};
        int[] expected = {20, 18, 10};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).where(TestCase::IsEven));
    }

    @Test
    public void RunOnce() {
        int[] source = {20, 7, 18, 9, 7, 10, 21};
        int[] expected = {20, 18, 10};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).runOnce().where(TestCase::IsEven));
    }

    @Test
    public void SourceAllNullsPredicateTrue() {
        Integer[] source = {null, null, null, null};
        assertEquals(Linq.asEnumerable(source), Linq.asEnumerable(source).where(num -> true));
    }

    @Test
    public void SourceEmptyIndexedPredicate() {
        assertEmpty(Linq.<Integer>empty().where((e, i) -> i % 2 == 0));
    }

    @Test
    public void SingleElementIndexedPredicateTrue() {
        int[] source = {2};
        assertEquals(Linq.asEnumerable(source), Linq.asEnumerable(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    public void SingleElementIndexedPredicateFalse() {
        int[] source = {3};
        assertEmpty(Linq.asEnumerable(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    public void IndexedPredicateFalseForAll() {
        int[] source = {9, 7, 15, 3, 27};
        assertEmpty(Linq.asEnumerable(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    public void IndexedPredicateTrueFirstOnly() {
        int[] source = {10, 9, 7, 15, 3, 27};
        assertEquals(Linq.asEnumerable(source).take(1), Linq.asEnumerable(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    public void IndexedPredicateTrueLastOnly() {
        int[] source = {9, 7, 15, 3, 27, 20};
        assertEquals(Linq.asEnumerable(source).skip(source.length - 1), Linq.asEnumerable(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    public void IndexedPredicateTrueFirstThirdSixth() {
        int[] source = {20, 7, 18, 9, 7, 10, 21};
        int[] expected = {20, 18, 10};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    public void SourceAllNullsIndexedPredicateTrue() {
        Integer[] source = {null, null, null, null};
        assertEquals(Linq.asEnumerable(source), Linq.asEnumerable(source).where((num, index) -> true));
    }

    @Test
    public void PredicateSelectsFirst() {
        int[] source = {-40, 20, 100, 5, 4, 9};
        assertEquals(Linq.asEnumerable(source).take(1), Linq.asEnumerable(source).where((e, i) -> i == 0));
    }

    @Test
    public void PredicateSelectsLast() {
        int[] source = {-40, 20, 100, 5, 4, 9};
        assertEquals(Linq.asEnumerable(source).skip(source.length - 1), Linq.asEnumerable(source).where((e, i) -> i == source.length - 1));
    }

    @Test
    public void IndexOverflows() {
        IEnumerable<Integer> infiniteWhere = new FastInfiniteEnumerator<Integer>().where((e, i) -> true);
        try (IEnumerator<Integer> en = infiniteWhere.enumerator()) {
            assertThrows(ArithmeticException.class, () ->
            {
                while (en.moveNext()) {
                }
            });
        }
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).where(i -> true);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateArray() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).toArray().where(i -> true);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateList() {
        IEnumerable<Integer> iterator = Linq.asEnumerable(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).where(i -> true);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateIndexed() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).where((e, i) -> true);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateWhereSelect() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).where(i -> true).select(i -> i);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateWhereSelectArray() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).toArray().where(i -> true).select(i -> i);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateWhereSelectList() {
        IEnumerable<Integer> iterator = Linq.asEnumerable(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).where(i -> true).select(i -> i);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ToCollection() {
        for (Object[] objects : this.ToCollectionData()) {
            this.ToCollection((IEnumerable<Integer>) objects[0]);
        }
    }

    private void ToCollection(IEnumerable<Integer> source) {
        for (IEnumerable<Integer> equivalent : Arrays.asList(source.where(s -> true), source.where(s -> true).select(s -> s))) {
            assertEquals(source, equivalent);
            assertEquals(source, equivalent.toArray());
            assertEquals(source, Linq.asEnumerable(equivalent.toList()));
            assertEquals(source.count(), equivalent.count()); // Count may be optimized. The above asserts do not imply this will pass.

            try (IEnumerator<Integer> en = equivalent.enumerator()) {
                for (int i = 0; i < equivalent.count(); i++) {
                    assertTrue(en.moveNext());
                }

                assertFalse(en.moveNext()); // No more items, this should dispose.
                assertEquals(null, en.current()); // Reset to default value

                assertFalse(en.moveNext()); // Want to be sure MoveNext after disposing still works.
                assertEquals(null, en.current());
            }
        }
    }

    private IEnumerable<Object[]> ToCollectionData() {
        IEnumerable<Integer> seq = this.GenerateRandomSequnce(0xdeadbeef, 10);
        return Linq.asEnumerable(TestCase.<Integer>IdentityTransforms())
                .select(t -> t.apply(seq))
                .select(seq2 -> new Object[]{seq2});
    }

    private IEnumerable<Integer> GenerateRandomSequnce(int seed, int count) {
        Random random = new Random(seed);

        //note: C# Random same seed generate same sequence, java does not. so call toArray().
        return Linq.range(0, count).select(i -> random.nextInt()).toArray();
    }

    @Test
    public void testWhere() {
        List<String> names = Linq.asEnumerable(emps)
                .where(employee -> employee.deptno < 15)
                .select(a -> a.name)
                .toList();
        assertEquals("[Fred, Eric, Janet]", names.toString());

        List<String> names2 = Linq.asEnumerable(emps)
                .where(employee -> employee.deptno < 15)
                .where(employee -> employee.name.length() == 4)
                .select(a -> a.name)
                .toList();
        assertEquals("[Fred, Eric]", names2.toString());

        List<Integer> even = Linq.range(1, 10)
                .where(n -> n % 2 == 0)
                .toList();
        assertEquals("[2, 4, 6, 8, 10]", even.toString());

        Integer[] numbs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Integer> even2 = Linq.asEnumerable(numbs)
                .where(n -> n % 2 == 0)
                .toList();
        assertEquals("[2, 4, 6, 8, 10]", even2.toString());
    }

    @Test
    public void testWhereIndexed() {
        // Returns every other employee.
        List<String> names = Linq.asEnumerable(emps)
                .where((employee, n) -> n % 2 == 0)
                .select(a -> a.name)
                .toList();
        assertEquals("[Fred, Eric]", names.toString());
    }

    @Test
    public void testSelect() {
        List<String> names = Linq.asEnumerable(emps)
                .select(emp -> emp.name)
                .toList();
        assertEquals("[Fred, Bill, Eric, Janet]", names.toString());

        List<Character> names2 = Linq.asEnumerable(emps)
                .select(emp -> emp.name)
                .select(name -> name.charAt(0))
                .toList();
        assertEquals("[F, B, E, J]", names2.toString());
    }
}
