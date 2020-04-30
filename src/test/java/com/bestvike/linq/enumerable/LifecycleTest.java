package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Action1;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple4;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 许崇雷 on 2019-09-09.
 */
class LifecycleTest extends TestCase {
    private static final AtomicInteger s_nextValue = new AtomicInteger(42);

    private static boolean ShortCircuits(Operation... ops) {
        return Linq.of(ops).any(o -> o.shortCircuits);
    }

    private static IEnumerable<Source> Sources() {
        return Linq.of(0, 1, 2)
                .select(size -> new Source(String.format("Enumerable%d", size), NumberRangeGuaranteedNotCollectionType(0, size)));
    }

    private static IEnumerable<Unary> UnaryOperations() {
        List<Unary> argsList = new ArrayList<>();
        argsList.add(new Unary("append", e -> e.append(s_nextValue.incrementAndGet())));
        argsList.add(new Unary("asEnumerable", e -> e.asEnumerable()));
        argsList.add(new Unary("cast", e -> e.cast(Integer.class)));
        argsList.add(new Unary("distinct", e -> e.distinct()));
        argsList.add(new Unary("distinctBy", e -> e.distinctBy(i -> i)));
        argsList.add(new Unary("defaultIfEmpty", e -> e.defaultIfEmpty(0)));
        argsList.add(new Unary("groupBy", e -> e.groupBy(i -> i).select(g -> g.getKey())));
        argsList.add(new Unary("groupBy", e -> e.groupBy(i -> i, i -> i).select(g -> g.getKey())));
        argsList.add(new Unary("ofType", e -> e.ofType(Integer.class)));
        argsList.add(new Unary("orderBy", e -> e.orderBy(i -> i)));
        argsList.add(new Unary("orderByDescending", e -> e.orderByDescending(i -> i)));
        argsList.add(new Unary("prepend", e -> e.prepend(s_nextValue.incrementAndGet())));
        argsList.add(new Unary("reverse", e -> e.reverse()));
        argsList.add(new Unary("select", e -> e.select(i -> i)));
        argsList.add(new Unary("select", e -> e.select((i, index) -> i)));
        argsList.add(new Unary("selectMany", e -> e.selectMany(i -> Linq.of(new int[]{i}))));
        argsList.add(new Unary("selectMany", e -> e.selectMany((i, index) -> Linq.of(new int[]{i}))));
        argsList.add(new Unary("shuffle", e -> e.shuffle()));
        argsList.add(new Unary("skip", e -> e.skip(1)));
        argsList.add(new Unary("skipWhile", e -> e.skipWhile(i -> true)));
        argsList.add(new Unary("skipWhile", e -> e.skipWhile(i -> false)));
        argsList.add(new Unary("skipLast", e -> e.skipLast(1)));
        argsList.add(new Unary("take", e -> e.take(Integer.MAX_VALUE - 1)));
        argsList.add(new Unary("takeLast", e -> e.takeLast(Integer.MAX_VALUE - 1)));
        argsList.add(new Unary("takeWhile", e -> e.takeWhile(i -> true)));
        argsList.add(new Unary("takeWhile", e -> e.takeWhile(i -> false), true));
        argsList.add(new Unary("thenBy", e -> e.orderBy(i -> i).thenBy(i -> i)));
        argsList.add(new Unary("thenByDescending", e -> e.orderByDescending(i -> i).thenByDescending(i -> i)));
        argsList.add(new Unary("where", e -> e.where(i -> true)));
        argsList.add(new Unary("where", e -> e.where((i, index) -> false)));
        argsList.add(new Unary("identity", e -> e));
        return Linq.of(argsList);
    }

    private static IEnumerable<Binary> BinaryOperations() {
        List<Binary> argsList = new ArrayList<>();
        argsList.add(new Binary("concat", (e1, e2) -> e1.concat(e2)));
        argsList.add(new Binary("except", (e1, e2) -> e1.except(e2)));
        argsList.add(new Binary("exceptBy", (e1, e2) -> e1.exceptBy(e2, i -> i)));
        argsList.add(new Binary("groupJoin", (e1, e2) -> e1.groupJoin(e2, i -> i, i -> i, (i, e3) -> i), true));
        argsList.add(new Binary("intersect", (e1, e2) -> e1.intersect(e2)));
        argsList.add(new Binary("intersectBy", (e1, e2) -> e1.intersectBy(e2, i -> i)));
        argsList.add(new Binary("join", (e1, e2) -> e1.join(e2, i -> i, i -> i, (i1, i2) -> i1), true));
        argsList.add(new Binary("leftJoin", (e1, e2) -> e1.leftJoin(e2, i -> i, i -> i, (i1, i2) -> i1), true));
        argsList.add(new Binary("rightJoin", (e1, e2) -> e1.rightJoin(e2, i -> i, i -> i, (i1, i2) -> i1 == null ? 0 : i1), true));
        argsList.add(new Binary("fullJoin", (e1, e2) -> e1.fullJoin(e2, i -> i, i -> i, (i1, i2) -> i1 == null ? 0 : i1), true));
        argsList.add(new Binary("crossJoin", (e1, e2) -> e1.crossJoin(e2, (i1, i2) -> i1), true));
        argsList.add(new Binary("union", (e1, e2) -> e1.union(e2)));
        argsList.add(new Binary("unionBy", (e1, e2) -> e1.unionBy(e2, i -> i)));
        argsList.add(new Binary("zip", (e1, e2) -> e1.zip(e2).select(i -> i.getItem1()), true));
        argsList.add(new Binary("zip", (e1, e2) -> e1.zip(e2, (i, j) -> i), true));
        return Linq.of(argsList);
    }

    private static IEnumerable<Sink> Sinks() {
        List<Sink> argsList = new ArrayList<>();
        argsList.add(new Sink("all", e -> e.all(i -> true)));
        argsList.add(new Sink("aggregate", e -> e.aggregate(0, (i, j) -> i + j)));
        argsList.add(new Sink("aggregate", e -> e.aggregate(0, (i, j) -> i + j, i -> i)));
        argsList.add(new Sink("aggregate", e -> e.aggregate((i, j) -> i + j)));
        argsList.add(new Sink("averageInt", e -> e.averageInt()));
        argsList.add(new Sink("averageInt", e -> e.averageInt(i -> i)));
        argsList.add(new Sink("any", e -> e.any(), true));
        argsList.add(new Sink("any", e -> e.any(i -> false)));
        argsList.add(new Sink("contains", e -> e.contains(-1)));
        argsList.add(new Sink("contains", e -> e.contains(0), true));
        argsList.add(new Sink("count", e -> e.count()));
        argsList.add(new Sink("count", e -> e.count(i -> true)));
        argsList.add(new Sink("elementAt", e -> e.elementAt(0), true));
        argsList.add(new Sink("elementAtOrDefault", e -> e.elementAtOrDefault(0), true));
        argsList.add(new Sink("findIndex", e -> e.findIndex(i -> i == -1)));
        argsList.add(new Sink("findLastIndex", e -> e.findLastIndex(i -> i == -1)));
        argsList.add(new Sink("first", e -> e.first(), true));
        argsList.add(new Sink("first", e -> e.first(i -> false)));
        argsList.add(new Sink("firstOrDefault", e -> e.firstOrDefault(), true));
        argsList.add(new Sink("firstOrDefault", e -> e.firstOrDefault(i -> false)));
        argsList.add(new Sink("format", e -> e.format()));
        argsList.add(new Sink("indexOf", e -> e.indexOf(-1)));
        argsList.add(new Sink("joining", e -> e.joining(",")));
        argsList.add(new Sink("last", e -> e.last()));
        argsList.add(new Sink("last", e -> e.last(i -> true)));
        argsList.add(new Sink("lastOrDefault", e -> e.lastOrDefault()));
        argsList.add(new Sink("lastOrDefault", e -> e.lastOrDefault(i -> true)));
        argsList.add(new Sink("lastIndexOf", e -> e.lastIndexOf(-1)));
        argsList.add(new Sink("longCount", e -> e.longCount()));
        argsList.add(new Sink("longCount", e -> e.longCount(i -> true)));
        argsList.add(new Sink("maxInt", e -> e.maxInt()));
        argsList.add(new Sink("maxInt", e -> e.maxInt(i -> i)));
        argsList.add(new Sink("maxByInt", e -> e.maxByInt(i -> i)));
        argsList.add(new Sink("minInt", e -> e.minInt()));
        argsList.add(new Sink("minInt", e -> e.minInt(i -> i)));
        argsList.add(new Sink("minByInt", e -> e.minByInt(i -> i)));
        argsList.add(new Sink("sequenceEqual", e -> e.sequenceEqual(Linq.range(0, 1)), true));
        argsList.add(new Sink("single", e -> e.single(), true));
        argsList.add(new Sink("single", e -> e.single(i -> false)));
        argsList.add(new Sink("singleOrDefault", e -> e.singleOrDefault()));
        argsList.add(new Sink("singleOrDefault", e -> e.singleOrDefault(i -> true)));
        argsList.add(new Sink("singleOrDefault", e -> e.singleOrDefault(i -> false)));
        argsList.add(new Sink("sumInt", e -> e.sumInt()));
        argsList.add(new Sink("sumInt", e -> e.sumInt(i -> i)));
        argsList.add(new Sink("toArray", e -> e.toArray()));
        argsList.add(new Sink("toArray", e -> e.toArray(Integer.class)));
        argsList.add(new Sink("toCollection", e -> e.toCollection(new ArrayList<>(), ArrayList::add)));
        argsList.add(new Sink("toEnumeration", e -> e.toEnumeration(), true));
        argsList.add(new Sink("toMap", e -> e.toMap(i -> i)));
        argsList.add(new Sink("toMap", e -> e.toMap(i -> i, i -> i)));
        argsList.add(new Sink("toLinkedMap", e -> e.toLinkedMap(i -> i)));
        argsList.add(new Sink("toLinkedMap", e -> e.toLinkedMap(i -> i, i -> i)));
        argsList.add(new Sink("toSet", e -> e.toSet()));
        argsList.add(new Sink("toLinkedSet", e -> e.toLinkedSet()));
        argsList.add(new Sink("toList", e -> e.toList()));
        argsList.add(new Sink("toLinkedList", e -> e.toLinkedList()));
        argsList.add(new Sink("toLookup", e -> e.toLookup(i -> i)));
        argsList.add(new Sink("toLookup", e -> e.toLookup(i -> i, i -> i)));
        argsList.add(new Sink("foreach", e -> {
            for (int item : e) {
            }
        }));
        argsList.add(new Sink("forEach", e -> e.forEach(i -> {
        })));
        argsList.add(new Sink("nop", e -> {
        }, true));
        return Linq.of(argsList);
    }

    @Test
    void UnaryOperations_SourceEnumeratorDisposed() {
        // Using Assert.All instead of a Theory to avoid overloading the system with hundreds of thousands of distinct test cases.
        IEnumerable<Tuple4<Source, Unary, Unary, Sink>> inputs =
                Sources().crossJoin(UnaryOperations().crossJoin(UnaryOperations().crossJoin(Sinks(),
                        Tuple::create),
                        (unary, tuple) -> Tuple.create(unary, tuple.getItem1(), tuple.getItem2())),
                        (source, tuple) -> Tuple.create(source, tuple.getItem1(), tuple.getItem2(), tuple.getItem3()));

        assertAll(inputs, input -> {
            Source source = input.getItem1();
            Unary unary1 = input.getItem2();
            Unary unary2 = input.getItem3();
            Sink sink = input.getItem4();
            LifecycleTrackingEnumerable<Integer> e = new LifecycleTrackingEnumerable<>(source.work);

            // source -> unary1 -> unary2 -> sink
            boolean argError = false;
            try {
                sink.work.apply(unary2.work.apply(unary1.work.apply(e)));
            } catch (ArgumentException | InvalidOperationException exc) {
                argError = true;
            }

            // We expect the source's enumerator should have been constructed 0 or 1 times,
            // once if there's no short-circuiting involved.  Then the enumerator's Dispose
            // should have been invoked the same number of times.
            boolean shortCircuits = argError || ShortCircuits(source, unary1, unary2, sink);
            assertInRange(e.getEnumeratorCtorCalls(), shortCircuits ? 0 : 1, 1);
            if ((unary1.name.equals("asEnumerable") || unary1.name.equals("identity"))
                    && (unary2.name.equals("asEnumerable") || unary2.name.equals("identity"))
                    && sink.name.equals("foreach"))
                return;
            assertEquals(e.getEnumeratorCtorCalls(), e.getEnumeratorDisposeCalls());
        });
    }

    @Test
    void BinaryOperations_SourceEnumeratorsDisposed() {
        // Using Assert.All instead of a Theory to avoid overloading the system with hundreds of thousands of distinct test cases.
        IEnumerable<Tuple4<Source, Unary, Binary, Sink>> inputs =
                Sources().crossJoin(UnaryOperations().crossJoin(BinaryOperations().crossJoin(Sinks(),
                        Tuple::create),
                        (unary, tuple) -> Tuple.create(unary, tuple.getItem1(), tuple.getItem2())),
                        (source, tuple) -> Tuple.create(source, tuple.getItem1(), tuple.getItem2(), tuple.getItem3()));

        assertAll(inputs, input -> {
            Source source = input.getItem1();
            Unary unary = input.getItem2();
            Binary binary = input.getItem3();
            Sink sink = input.getItem4();
            LifecycleTrackingEnumerable<Integer>[] es = new LifecycleTrackingEnumerable[]{
                    new LifecycleTrackingEnumerable<>(source.work),
                    new LifecycleTrackingEnumerable<>(source.work)
            };

            // ((source -> unary), (source -> unary)) -> binary -> sink
            boolean argError = false;
            try {
                sink.work.apply(binary.work.apply(unary.work.apply(es[0]), unary.work.apply(es[1])));
            } catch (ArgumentException | InvalidOperationException exc) {
                argError = true;
            }

            // We expect the source's enumerator should have been constructed 0 or 1 times,
            // once if there's no short-circuiting involved.  Then the enumerator's Dispose
            // should have been invoked the same number of times.
            boolean shortCircuits = argError || ShortCircuits(source, binary, unary, sink);
            if (binary.name.equals("crossJoin"))
                return;
            assertAll(Linq.of(es), e -> {
                assertInRange(e.getEnumeratorCtorCalls(), shortCircuits ? 0 : 1, 1);
                assertEquals(e.getEnumeratorCtorCalls(), e.getEnumeratorDisposeCalls());
            });
        });
    }


    private static final class LifecycleTrackingEnumerable<T> implements IEnumerable<T> {
        private final IEnumerable<T> source;
        private final AtomicInteger enumeratorCtorCalls = new AtomicInteger();
        private final AtomicInteger enumeratorDisposeCalls = new AtomicInteger();

        LifecycleTrackingEnumerable(IEnumerable<T> source) {
            this.source = source;
        }

        int getEnumeratorCtorCalls() {
            return this.enumeratorCtorCalls.intValue();
        }

        int getEnumeratorDisposeCalls() {
            return this.enumeratorDisposeCalls.intValue();
        }

        @Override
        public IEnumerator<T> enumerator() {
            return new Enumerator<>(this);
        }

        private static final class Enumerator<T> extends AbstractEnumerator<T> {
            private final LifecycleTrackingEnumerable<T> enumerable;
            private final IEnumerator<T> enumerator;

            Enumerator(LifecycleTrackingEnumerable<T> enumerable) {
                this.enumerable = enumerable;
                this.enumerable.enumeratorCtorCalls.incrementAndGet();
                this.enumerator = enumerable.source.enumerator();
            }

            @Override
            public boolean moveNext() {
                return this.enumerator.moveNext();
            }

            @Override
            public T current() {
                return this.enumerator.current();
            }

            @Override
            public void close() {
                this.enumerable.enumeratorDisposeCalls.incrementAndGet();
                this.enumerator.close();
            }
        }
    }

    private static abstract class Operation<TWork> {
        final String name;
        final TWork work;
        final boolean shortCircuits;

        Operation(String name, TWork operation, boolean shortCircuits) {
            this.name = name;
            this.work = operation;
            this.shortCircuits = shortCircuits;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private static final class Source extends Operation<IEnumerable<Integer>> {
        Source(String name, IEnumerable<Integer> enumerable) {
            super(name, enumerable, false);
        }

        Source(String name, IEnumerable<Integer> enumerable, boolean shortCircuits) {
            super(name, enumerable, shortCircuits);
        }
    }

    private static final class Unary extends Operation<Func1<IEnumerable<Integer>, IEnumerable<Integer>>> {
        Unary(String name, Func1<IEnumerable<Integer>, IEnumerable<Integer>> unary) {
            super(name, unary, false);
        }

        Unary(String name, Func1<IEnumerable<Integer>, IEnumerable<Integer>> unary, boolean shortCircuits) {
            super(name, unary, shortCircuits);
        }
    }

    private static final class Binary extends Operation<Func2<IEnumerable<Integer>, IEnumerable<Integer>, IEnumerable<Integer>>> {
        Binary(String name, Func2<IEnumerable<Integer>, IEnumerable<Integer>, IEnumerable<Integer>> binary) {
            super(name, binary, false);
        }

        Binary(String name, Func2<IEnumerable<Integer>, IEnumerable<Integer>, IEnumerable<Integer>> binary, boolean shortCircuits) {
            super(name, binary, shortCircuits);
        }
    }

    private static final class Sink extends Operation<Action1<IEnumerable<Integer>>> {
        Sink(String name, Action1<IEnumerable<Integer>> sink) {
            super(name, sink, false);
        }

        Sink(String name, Action1<IEnumerable<Integer>> sink, boolean shortCircuits) {
            super(name, sink, shortCircuits);
        }
    }
}
