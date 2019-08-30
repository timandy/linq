package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Func1;
import com.bestvike.function.IndexPredicate2;
import com.bestvike.function.Predicate0;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.NotSupportedException;
import com.bestvike.ref;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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
class WhereTest extends TestCase {
    private static IEnumerable<Integer> GenerateRandomSequnce(int seed, int count) {
        Random random = new Random(seed);

        //note: C# Random same seed generate same sequence, java does not. so call toArray().
        return Linq.range(0, count).select(i -> random.nextInt()).toArray();
    }

    private static IEnumerable<Object[]> ToCollectionData() {
        IEnumerable<Integer> seq = GenerateRandomSequnce(0xdeadbeef, 10);
        return Linq.of(TestCase.<Integer>IdentityTransforms())
                .select(t -> t.apply(seq))
                .select(seq2 -> new Object[]{seq2});
    }

    @Test
    void Where_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> source = null;
        Predicate1<Integer> simplePredicate = (value) -> true;
        IndexPredicate2<Integer> complexPredicate = (value, index) -> true;

        assertThrows(NullPointerException.class, () -> source.where(simplePredicate));
        assertThrows(NullPointerException.class, () -> source.where(complexPredicate));
    }

    @Test
    void Where_PredicateIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> source = Linq.range(1, 10);
        Predicate1<Integer> simplePredicate = null;
        IndexPredicate2<Integer> complexPredicate = null;

        assertThrows(ArgumentNullException.class, () -> source.where(simplePredicate));
        assertThrows(ArgumentNullException.class, () -> source.where(complexPredicate));
    }

    @Test
    void Where_Array_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Predicate0[] source = new Predicate0[]{() -> {
            funcCalled.value = true;
            return true;
        }};

        IEnumerable<Predicate0> query = Linq.of(source).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.of(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void Where_List_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        List<Predicate0> source = Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        });

        IEnumerable<Predicate0> query = Linq.of(source).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.of(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void Where_IReadOnlyCollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Predicate0> source = Collections.unmodifiableCollection(Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        }));

        IEnumerable<Predicate0> query = Linq.of(source).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.of(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void Where_ICollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Predicate0> source = new LinkedList<>(Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        }));

        IEnumerable<Predicate0> query = Linq.of(source).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.of(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void Where_IEnumerable_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        IEnumerable<Predicate0> source = Linq.repeat(() -> {
            funcCalled.value = true;
            return true;
        }, 1);

        IEnumerable<Predicate0> query = source.where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = source.where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void WhereWhere_Array_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Predicate0[] source = new Predicate0[]{() -> {
            funcCalled.value = true;
            return true;
        }};

        IEnumerable<Predicate0> query = Linq.of(source).where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.of(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void WhereWhere_List_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        List<Predicate0> source = Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        });

        IEnumerable<Predicate0> query = Linq.of(source).where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.of(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void WhereWhere_IReadOnlyCollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Predicate0> source = Collections.unmodifiableCollection(Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        }));

        IEnumerable<Predicate0> query = Linq.of(source).where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.of(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void WhereWhere_ICollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Predicate0> source = new LinkedList<>(Collections.singletonList(() -> {
            funcCalled.value = true;
            return true;
        }));

        IEnumerable<Predicate0> query = Linq.of(source).where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = Linq.of(source).where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void WhereWhere_IEnumerable_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        IEnumerable<Predicate0> source = Linq.repeat(() -> {
            funcCalled.value = true;
            return true;
        }, 1);

        IEnumerable<Predicate0> query = source.where(value -> value.apply()).where(value -> value.apply());
        assertFalse(funcCalled.value);

        query = source.where((value, index) -> value.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    void Where_Array_ReturnsExpectedValues_True() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerable<Integer> result = Linq.of(source).where(truePredicate);

        assertEquals(source.length, result.count());
        for (int i = 0; i < source.length; i++) {
            assertEquals(Linq.of(source).elementAt(i), result.elementAt(i));
        }
    }

    @Test
    void Where_Array_ReturnsExpectedValues_False() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Predicate1<Integer> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = Linq.of(source).where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    void Where_Array_ReturnsExpectedValues_Complex() {
        int[] source = new int[]{2, 1, 3, 5, 4};
        IndexPredicate2<Integer> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = Linq.of(source).where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void Where_List_ReturnsExpectedValues_True() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerable<Integer> result = Linq.of(source).where(truePredicate);

        assertEquals(source.size(), result.count());
        for (int i = 0; i < source.size(); i++) {
            assertEquals(Linq.of(source).elementAt(i), result.elementAt(i));
        }
    }

    @Test
    void Where_List_ReturnsExpectedValues_False() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Predicate1<Integer> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = Linq.of(source).where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    void Where_List_ReturnsExpectedValues_Complex() {
        List<Integer> source = Arrays.asList(2, 1, 3, 5, 4);
        IndexPredicate2<Integer> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = Linq.of(source).where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void Where_IReadOnlyCollection_ReturnsExpectedValues_True() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerable<Integer> result = Linq.of(source).where(truePredicate);

        assertEquals(source.size(), result.count());
        for (int i = 0; i < source.size(); i++) {
            assertEquals(Linq.of(source).elementAt(i), result.elementAt(i));
        }
    }

    @Test
    void Where_IReadOnlyCollection_ReturnsExpectedValues_False() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = Linq.of(source).where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    void Where_IReadOnlyCollection_ReturnsExpectedValues_Complex() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(2, 1, 3, 5, 4));
        IndexPredicate2<Integer> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = Linq.of(source).where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void Where_ICollection_ReturnsExpectedValues_True() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerable<Integer> result = Linq.of(source).where(truePredicate);

        assertEquals(source.size(), result.count());
        for (int i = 0; i < source.size(); i++) {
            assertEquals(Linq.of(source).elementAt(i), result.elementAt(i));
        }
    }

    @Test
    void Where_ICollection_ReturnsExpectedValues_False() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = Linq.of(source).where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    void Where_ICollection_ReturnsExpectedValues_Complex() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(2, 1, 3, 5, 4));
        IndexPredicate2<Integer> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = Linq.of(source).where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void Where_IEnumerable_ReturnsExpectedValues_True() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerable<Integer> result = source.where(truePredicate);

        assertEquals(source.count(), result.count());
        for (int i = 0; i < source.count(); i++) {
            assertEquals(source.elementAt(i), result.elementAt(i));
        }
    }

    @Test
    void Where_IEnumerable_ReturnsExpectedValues_False() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Predicate1<Integer> falsePredicate = (value) -> false;

        IEnumerable<Integer> result = source.where(falsePredicate);

        assertEquals(0, result.count());
    }

    @Test
    void Where_IEnumerable_ReturnsExpectedValues_Complex() {
        IEnumerable<Integer> source = Linq.of(new LinkedList<>(Arrays.asList(2, 1, 3, 5, 4)));
        IndexPredicate2<Integer> complexPredicate = (value, index) -> (value == index);

        IEnumerable<Integer> result = source.where(complexPredicate);

        assertEquals(2, result.count());
        assertEquals(1, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void Where_EmptyEnumerable_ReturnsNoElements() {
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
    void Where_EmptyEnumerable_ReturnsNoElementsWithIndex() {
        assertEmpty(Linq.<Integer>empty().where((e, i) -> true));
    }

    @Test
    void Where_Array_CurrentIsDefaultOfTAfterEnumeration() {
        int[] source = new int[]{1};
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.of(source).where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    void Where_List_CurrentIsDefaultOfTAfterEnumeration() {
        List<Integer> source = Collections.singletonList(1);
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.of(source).where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    void Where_IReadOnlyCollection_CurrentIsDefaultOfTAfterEnumeration() {
        Collection<Integer> source = Collections.unmodifiableCollection(Collections.singletonList(1));
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.of(source).where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    void Where_ICollection_CurrentIsDefaultOfTAfterEnumeration() {
        Collection<Integer> source = new LinkedList<>(Collections.singletonList(1));
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.of(source).where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    void Where_IEnumerable_CurrentIsDefaultOfTAfterEnumeration() {
        IEnumerable<Integer> source = Linq.repeat(1, 1);
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = source.where(truePredicate).enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    void WhereWhere_Array_ReturnsExpectedValues() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void WhereWhere_List_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void WhereWhere_IReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void WhereWhere_ICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void WhereWhere_IEnumerable_ReturnsExpectedValues() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;

        IEnumerable<Integer> result = source.where(evenPredicate).where(evenPredicate);

        assertEquals(2, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
    }

    @Test
    void WhereSelect_Array_ReturnsExpectedValues() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void WhereSelectSelect_Array_ReturnsExpectedValues() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void WhereSelect_List_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void WhereSelectSelect_List_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void WhereSelect_IReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void WhereSelectSelect_IReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void WhereSelect_ICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void WhereSelectSelect_ICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void WhereSelect_IEnumerable_ReturnsExpectedValues() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = source.where(evenPredicate).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void WhereSelectSelect_IEnumerable_ReturnsExpectedValues() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = source.where(evenPredicate).select(i -> i).select(addSelector);

        assertEquals(2, result.count());
        assertEquals(3, result.elementAt(0));
        assertEquals(5, result.elementAt(1));
    }

    @Test
    void SelectWhere_Array_ReturnsExpectedValues() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    void SelectWhere_List_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    void SelectWhere_IReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    void SelectWhere_ICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = Linq.of(source).select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    void SelectWhere_IEnumerable_ReturnsExpectedValues() {
        IEnumerable<Integer> source = Linq.range(1, 5);
        Predicate1<Integer> evenPredicate = (value) -> value % 2 == 0;
        Func1<Integer, Integer> addSelector = (value) -> value + 1;

        IEnumerable<Integer> result = source.select(addSelector).where(evenPredicate);

        assertEquals(3, result.count());
        assertEquals(2, result.elementAt(0));
        assertEquals(4, result.elementAt(1));
        assertEquals(6, result.elementAt(2));
    }

    @Test
    void Where_PredicateThrowsException() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Predicate1<Integer> predicate = value -> {
            if (value == 1) {
                throw new InvalidOperationException();
            }
            return true;
        };

        IEnumerator<Integer> enumerator = Linq.of(source).where(predicate).enumerator();

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
    void Where_SourceThrowsOnCurrent() {
        IEnumerable<Integer> source = new ThrowsOnCurrentEnumerator();
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = source.where(truePredicate).enumerator();

        // Ensure the first MoveNext call throws an exception
        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());

        // Ensure subsequent MoveNext calls succeed
        assertTrue(enumerator.moveNext());
        assertEquals(2, enumerator.current());
    }

    @Test
    void Where_SourceThrowsOnMoveNext() {
        IEnumerable<Integer> source = new ThrowsOnMoveNext();
        Predicate1<Integer> truePredicate = (value) -> true;

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
    void Where_SourceThrowsOnGetEnumerator() {
        IEnumerable<Integer> source = new ThrowsOnGetEnumerator();
        Predicate1<Integer> truePredicate = (value) -> true;

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
    void Select_ResetEnumerator_ThrowsException() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        IEnumerator<Integer> enumerator = Linq.of(source).where(value -> true).enumerator();

        // The full .NET Framework throws a NotImplementedException.
        // See https://github.com/dotnet/corefx/pull/2959.
        assertThrows(NotSupportedException.class, () -> enumerator.reset());
    }

    @Test
    void Where_SourceThrowsOnConcurrentModification() {
        List<Integer> source = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Predicate1<Integer> truePredicate = (value) -> true;

        IEnumerator<Integer> enumerator = Linq.of(source).where(truePredicate).enumerator();

        assertTrue(enumerator.moveNext());
        assertEquals(1, enumerator.current());

        source.add(6);
        assertThrows(ConcurrentModificationException.class, () -> enumerator.moveNext());
    }

    @Test
    void Where_GetEnumeratorReturnsUniqueInstances() {
        int[] source = new int[]{1, 2, 3, 4, 5};

        IEnumerable<Integer> result = Linq.of(source).where(value -> true);

        try (IEnumerator<Integer> enumerator1 = result.enumerator();
             IEnumerator<Integer> enumerator2 = result.enumerator()) {
            assertSame(result, enumerator1);
            assertNotSame(enumerator1, enumerator2);
        }
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE)
                .select(x -> x);

        assertEquals(q.where(TestCase::IsEven), q.where(TestCase::IsEven));

    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", null, "SoS", Empty})
                .select(x -> x);

        assertEquals(q.where(TestCase::IsNullOrEmpty), q.where(TestCase::IsNullOrEmpty));

    }

    @Test
    void SingleElementPredicateFalse() {
        int[] source = {3};
        assertEmpty(Linq.of(source).where(TestCase::IsEven));
    }

    @Test
    void PredicateFalseForAll() {
        int[] source = {9, 7, 15, 3, 27};
        assertEmpty(Linq.of(source).where(TestCase::IsEven));
    }

    @Test
    void PredicateTrueFirstOnly() {
        int[] source = {10, 9, 7, 15, 3, 27};
        assertEquals(Linq.of(source).take(1), Linq.of(source).where(TestCase::IsEven));
    }

    @Test
    void PredicateTrueLastOnly() {
        int[] source = {9, 7, 15, 3, 27, 20};
        assertEquals(Linq.of(source).skip(source.length - 1), Linq.of(source).where(TestCase::IsEven));
    }

    @Test
    void PredicateTrueFirstThirdSixth() {
        int[] source = {20, 7, 18, 9, 7, 10, 21};
        int[] expected = {20, 18, 10};
        assertEquals(Linq.of(expected), Linq.of(source).where(TestCase::IsEven));
    }

    @Test
    void RunOnce() {
        int[] source = {20, 7, 18, 9, 7, 10, 21};
        int[] expected = {20, 18, 10};
        assertEquals(Linq.of(expected), Linq.of(source).runOnce().where(TestCase::IsEven));
    }

    @Test
    void SourceAllNullsPredicateTrue() {
        Integer[] source = {null, null, null, null};
        assertEquals(Linq.of(source), Linq.of(source).where(num -> true));
    }

    @Test
    void SourceEmptyIndexedPredicate() {
        assertEmpty(Linq.<Integer>empty().where((e, i) -> i % 2 == 0));
    }

    @Test
    void SingleElementIndexedPredicateTrue() {
        int[] source = {2};
        assertEquals(Linq.of(source), Linq.of(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    void SingleElementIndexedPredicateFalse() {
        int[] source = {3};
        assertEmpty(Linq.of(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    void IndexedPredicateFalseForAll() {
        int[] source = {9, 7, 15, 3, 27};
        assertEmpty(Linq.of(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    void IndexedPredicateTrueFirstOnly() {
        int[] source = {10, 9, 7, 15, 3, 27};
        assertEquals(Linq.of(source).take(1), Linq.of(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    void IndexedPredicateTrueLastOnly() {
        int[] source = {9, 7, 15, 3, 27, 20};
        assertEquals(Linq.of(source).skip(source.length - 1), Linq.of(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    void IndexedPredicateTrueFirstThirdSixth() {
        int[] source = {20, 7, 18, 9, 7, 10, 21};
        int[] expected = {20, 18, 10};
        assertEquals(Linq.of(expected), Linq.of(source).where((e, i) -> e % 2 == 0));
    }

    @Test
    void SourceAllNullsIndexedPredicateTrue() {
        Integer[] source = {null, null, null, null};
        assertEquals(Linq.of(source), Linq.of(source).where((num, index) -> true));
    }

    @Test
    void PredicateSelectsFirst() {
        int[] source = {-40, 20, 100, 5, 4, 9};
        assertEquals(Linq.of(source).take(1), Linq.of(source).where((e, i) -> i == 0));
    }

    @Test
    void PredicateSelectsLast() {
        int[] source = {-40, 20, 100, 5, 4, 9};
        assertEquals(Linq.of(source).skip(source.length - 1), Linq.of(source).where((e, i) -> i == source.length - 1));
    }

    @Test
    void IndexOverflows() {
        IEnumerable<Integer> infiniteWhere = new FastInfiniteEnumerator<Integer>().where((e, i) -> true);
        try (IEnumerator<Integer> en = infiniteWhere.enumerator()) {
            assertThrows(ArithmeticException.class, () -> {
                while (en.moveNext()) {
                }
            });
        }
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).where(i -> true);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateArray() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).toArray().where(i -> true);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateList() {
        IEnumerable<Integer> iterator = Linq.of(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).where(i -> true);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateIndexed() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).where((e, i) -> true);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateWhereSelect() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).where(i -> true).select(i -> i);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateWhereSelectArray() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).toArray().where(i -> true).select(i -> i);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateWhereSelectList() {
        IEnumerable<Integer> iterator = Linq.of(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).where(i -> true).select(i -> i);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @ParameterizedTest
    @MethodSource("ToCollectionData")
    void ToCollection(IEnumerable<Integer> source) {
        for (IEnumerable<Integer> equivalent : Arrays.asList(source.where(s -> true), source.where(s -> true).select(s -> s))) {
            assertEquals(source, equivalent);
            assertEquals(source, equivalent.toArray());
            assertEquals(source, Linq.of(equivalent.toArray(Integer.class)));
            assertEquals(source, Linq.of(equivalent.toList()));
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

    @Test
    void testWhere() {
        List<String> names = Linq.of(emps)
                .where(employee -> employee.deptno < 15)
                .select(a -> a.name)
                .toList();
        assertEquals("[Fred, Eric, Janet]", names.toString());

        List<String> names2 = Linq.of(emps)
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
        List<Integer> even2 = Linq.of(numbs)
                .where(n -> n % 2 == 0)
                .toList();
        assertEquals("[2, 4, 6, 8, 10]", even2.toString());
    }

    @Test
    void testWhereIndexed() {
        // Returns every other employee.
        List<String> names = Linq.of(emps)
                .where((employee, n) -> n % 2 == 0)
                .select(a -> a.name)
                .toList();
        assertEquals("[Fred, Eric]", names.toString());
    }

    @Test
    void testSelect() {
        List<String> names = Linq.of(emps)
                .select(emp -> emp.name)
                .toList();
        assertEquals("[Fred, Bill, Eric, Janet]", names.toString());

        List<Character> names2 = Linq.of(emps)
                .select(emp -> emp.name)
                .select(name -> name.charAt(0))
                .toList();
        assertEquals("[F, B, E, J]", names2.toString());
    }
}
