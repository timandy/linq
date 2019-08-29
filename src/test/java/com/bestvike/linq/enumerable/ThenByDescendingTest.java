package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.Comparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IOrderedEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * Created by 许崇雷 on 2019-06-06.
 */
class ThenByDescendingTest extends TestCase {
    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Tuple2<Integer, Integer>> q = Linq.of(new int[]{1, 6, 0, -1, 3})
                .selectMany(x -> Linq.of(new int[]{55, 49, 9, -100, 24, 25}), (x1, x2) -> Tuple.create(x1, x2));

        assertEquals(
                q.orderByDescending(e -> e.getItem2()).thenByDescending(f -> f.getItem1()),
                q.orderByDescending(e -> e.getItem2()).thenByDescending(f -> f.getItem1())
        );
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<Tuple2<Integer, String>> q = Linq.of(new int[]{55, 49, 9, -100, 24, 25, -1, 0})
                .selectMany(x -> Linq.of("!@#$%^", "C", "AAA", "", null, "Calling Twice", "SoS", Empty).where(a -> !IsNullOrEmpty(a)), (x1, x2) -> Tuple.create(x1, x2));

        assertEquals(q.orderBy(e -> e.getItem1()).thenByDescending(f -> f.getItem2()),
                q.orderBy(e -> e.getItem1()).thenByDescending(f -> f.getItem2()));
    }

    @Test
    void SourceEmpty() {
        int[] source = {};
        assertEmpty(Linq.of(source).orderBy(e -> e).thenByDescending(e -> e));
    }

    @Test
    void AscendingKeyThenDescendingKey() {
        UserAddress[] source = new UserAddress[]{
                new UserAddress("Jim", "Minneapolis", "USA"),
                new UserAddress("Tim", "Seattle", "USA"),
                new UserAddress("Philip", "Orlando", "USA"),
                new UserAddress("Chris", "London", "UK"),
                new UserAddress("Rob", "Kent", "UK")
        };
        UserAddress[] expected = new UserAddress[]{
                new UserAddress("Chris", "London", "UK"),
                new UserAddress("Rob", "Kent", "UK"),
                new UserAddress("Tim", "Seattle", "USA"),
                new UserAddress("Philip", "Orlando", "USA"),
                new UserAddress("Jim", "Minneapolis", "USA")
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e.Country).thenByDescending(e -> e.City));
    }

    @Test
    void DescendingKeyThenDescendingKey() {
        UserAddress[] source = new UserAddress[]{
                new UserAddress("Jim", "Minneapolis", "USA"),
                new UserAddress("Tim", "Seattle", "USA"),
                new UserAddress("Philip", "Orlando", "USA"),
                new UserAddress("Chris", "London", "UK"),
                new UserAddress("Rob", "Kent", "UK")
        };
        UserAddress[] expected = new UserAddress[]{
                new UserAddress("Tim", "Seattle", "USA"),
                new UserAddress("Philip", "Orlando", "USA"),
                new UserAddress("Jim", "Minneapolis", "USA"),
                new UserAddress("Chris", "London", "UK"),
                new UserAddress("Rob", "Kent", "UK")
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderByDescending(e -> e.Country).thenByDescending(e -> e.City));
    }

    @Test
    void OrderIsStable() {
        String[] source = split("Because I could not stop for Death -\nHe kindly stopped for me -\nThe Carriage held but just Ourselves -\nAnd Immortality.", new char[]{' ', '\n', '\r', '-'}, true);
        String[] expected = new String[]{
                "stopped", "kindly", "could", "stop", "held", "just", "not", "for", "for", "but", "me",
                "Immortality.", "Ourselves", "Carriage", "Because", "Death", "The", "And", "He", "I"
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(word -> Character.isUpperCase(word.charAt(0))).thenByDescending(word -> word.length()));
    }

    @Test
    void OrderIsStableCustomComparer() {
        String[] source = split("Because I could not stop for Death -\nHe kindly stopped for me -\nThe Carriage held but just Ourselves -\nAnd Immortality.", new char[]{' ', '\n', '\r', '-'}, true);
        String[] expected = new String[]{
                "me", "not", "for", "for", "but", "stop", "held", "just", "could", "kindly", "stopped",
                "I", "He", "The", "And", "Death", "Because", "Carriage", "Ourselves", "Immortality."
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(word -> Character.isUpperCase(word.charAt(0))).thenByDescending(word -> word.length(), Comparer.create((Integer w1, Integer w2) -> w2.compareTo(w1))));
    }

    @Test
    void RunOnce() {
        String[] source = split("Because I could not stop for Death -\nHe kindly stopped for me -\nThe Carriage held but just Ourselves -\nAnd Immortality.", new char[]{' ', '\n', '\r', '-'}, true);
        String[] expected = new String[]{
                "me", "not", "for", "for", "but", "stop", "held", "just", "could", "kindly", "stopped",
                "I", "He", "The", "And", "Death", "Because", "Carriage", "Ourselves", "Immortality."
        };

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().orderBy(word -> Character.isUpperCase(word.charAt(0))).thenByDescending(word -> word.length(), Comparer.create((Integer w1, Integer w2) -> w2.compareTo(w1))));
    }

    @Test
    void NullSource() {
        IOrderedEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.thenByDescending(i -> i));
    }

    @Test
    void NullKeySelector() {
        Func1<Date, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().orderBy(e -> e).thenByDescending(keySelector));
    }

    @Test
    void NullSourceComparer() {
        IOrderedEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.thenByDescending(i -> i, null));
    }

    @Test
    void NullKeySelectorComparer() {
        Func1<Date, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().orderBy(e -> e).thenByDescending(keySelector, null));
    }

    @Test
    void SortsLargeAscendingEnumerableCorrectly() {
        this.SortsLargeAscendingEnumerableCorrectly(1);
        this.SortsLargeAscendingEnumerableCorrectly(2);
        this.SortsLargeAscendingEnumerableCorrectly(3);
    }

    private void SortsLargeAscendingEnumerableCorrectly(int thenBys) {
        final int Items = 100_000;
        IEnumerable<Integer> expected = NumberRangeGuaranteedNotCollectionType(0, Items);

        IEnumerable<Integer> unordered = expected.select(i -> i);
        IOrderedEnumerable<Integer> ordered = unordered.orderBy(x -> 0);
        switch (thenBys) {
            case 1:
                ordered = ordered.thenByDescending(i -> -i);
                break;
            case 2:
                ordered = ordered.thenByDescending(i -> 0).thenByDescending(i -> -i);
                break;
            case 3:
                ordered = ordered.thenByDescending(i -> 0).thenByDescending(i -> 0).thenByDescending(i -> -i);
                break;
        }

        assertEquals(expected, ordered);
    }

    @Test
    void SortsLargeDescendingEnumerableCorrectly() {
        this.SortsLargeDescendingEnumerableCorrectly(1);
        this.SortsLargeDescendingEnumerableCorrectly(2);
        this.SortsLargeDescendingEnumerableCorrectly(3);
    }

    private void SortsLargeDescendingEnumerableCorrectly(int thenBys) {
        final int Items = 100_000;
        IEnumerable<Integer> expected = NumberRangeGuaranteedNotCollectionType(0, Items);

        IEnumerable<Integer> unordered = expected.select(i -> Items - i - 1);
        IOrderedEnumerable<Integer> ordered = unordered.orderBy(x -> 0);
        switch (thenBys) {
            case 1:
                ordered = ordered.thenByDescending(i -> -i);
                break;
            case 2:
                ordered = ordered.thenByDescending(i -> 0).thenByDescending(i -> -i);
                break;
            case 3:
                ordered = ordered.thenByDescending(i -> 0).thenByDescending(i -> 0).thenByDescending(i -> -i);
                break;
        }

        assertEquals(expected, ordered);
    }

    @Test
    void SortsLargeRandomizedEnumerableCorrectly() {
        this.SortsLargeRandomizedEnumerableCorrectly(1);
        this.SortsLargeRandomizedEnumerableCorrectly(2);
        this.SortsLargeRandomizedEnumerableCorrectly(3);
    }

    private void SortsLargeRandomizedEnumerableCorrectly(int thenBys) {
        final int Items = 100_000;
        Random r = new Random(42);

        Integer[] randomized = Linq.range(0, Items).select(i -> r.nextInt()).toArray(Integer.class);

        IOrderedEnumerable<Integer> orderedEnumerable = Linq.of(randomized).orderBy(x -> 0);
        switch (thenBys) {
            case 1:
                orderedEnumerable = orderedEnumerable.thenByDescending(i -> -i);
                break;
            case 2:
                orderedEnumerable = orderedEnumerable.thenByDescending(i -> 0).thenByDescending(i -> -i);
                break;
            case 3:
                orderedEnumerable = orderedEnumerable.thenByDescending(i -> 0).thenByDescending(i -> 0).thenByDescending(i -> -i);
                break;
        }
        Array<Integer> ordered = orderedEnumerable.toArray();

        Arrays.sort(randomized);
        assertEquals(Linq.of(randomized), orderedEnumerable);
    }


    private static class UserAddress extends ValueType {
        private final String Name;
        private final String City;
        private final String Country;

        private UserAddress(String name, String city, String country) {
            this.Name = name;
            this.City = city;
            this.Country = country;
        }
    }
}
