<!--变更日志-->

# v5.0.0 Release notes
`Synchronize changes from .net 5.`

### Features
- Add `size()` and `isEmpty()` methods for `ICollection` interface.

### Changes
- Improve performance of method `ArrayListEnumerable.toArray(Class<TSource> clazz)`.
- Optimize allocation of hash sets to reduce the number of `resize` times. Effected methods `toMap`,`toLinkedMap`,`toSet`,`toLinkedSet`.
- Keep first value for duplicated key. Effected methods `toMap`, `toLinkedMap`.

### Tips
- In order to reduce sorting time, methods `first(predicate)` and `firstOrDefault(predicate)` after `OrderedEnumerable` call the predicate once per element.
So don't call side-effect methods in linq.
see [https://github.com/dotnet/runtime/issues/31554](https://github.com/dotnet/runtime/issues/31554).

# Links
- Download [https://repo1.maven.org/maven2/com/bestvike/linq/5.0.0/](https://repo1.maven.org/maven2/com/bestvike/linq/5.0.0/)
- Source code [https://github.com/timandy/linq/tree/v5.0.0](https://github.com/timandy/linq/tree/v5.0.0)

---

# v3.1.0 Release notes

### Features
- Support custom debug view for any type by `DebuggerDisplay`, `DebuggerTypeProxy` annotation.

### Changes
- Rename `DebugView.getDebuggerDisplay(...)` to `DebugView.getDebuggerDisplayText(...)`.
- Rename `DebugView.getDebuggerTypeProxy(...)` to `DebugView.getDebuggerProxyObject(...)`.
- Modify debugger display for types which is cheap to count the elements.

# Links
- Download [https://repo1.maven.org/maven2/com/bestvike/linq/3.1.0/](https://repo1.maven.org/maven2/com/bestvike/linq/3.1.0/)
- Source code [https://github.com/timandy/linq/tree/v3.1.0](https://github.com/timandy/linq/tree/v3.1.0)

---

# v3.0.0 Release notes
`This is the first stable version available for production.
It is highly recommended to upgrade to this version if you have used a previous version.`

### Bugs
- Fix unsigned compare bugs.
- Fix `Lookup.ApplyResultSelector` bugs.
- Fix `OrderedEnumerableRangeEnumerator` not iterate maxIdx.
- Fix `EnumerablePartition` error assert.
- Fix inner enumerator of `CrossJoinIterator` and `SelectManyIterator` dispose twice.
- Fix not check selector param for `sum` with selector.
- Fix bug with `select(...).count()` on an IPartition.
- Fix bug with `skip`, `take` on an empty IOrderedEnumerable.
- Fix `union` and `unionBy` not close when break iterate.

### Features
- Add `zip` Tuple api.
- Add support compatible stream for `IEnumerable`.
- Add api `toLinkedList`, `toLinkedMap`, `toLinkedSet`, `toCollection` for `IEnumerable`.
- Add api `indexOf`, `lastIndexOf` for `IEnumerable`.
- Add api `findIndex`, `findLastIndex` for `IEnumerable`.
- Add api `format`, `joining` for `IEnumerable` to print values.
- Add api `ofNullable`, `chars`, `words`, `lines`, `split`, `infinite`, `loop`, `enumerate`, `iterate` for `Linq`.
- Add `_indexOf` and `_lastIndexOf` method for `IList`.
- Add `_findIndex()` and `_findLastIndex()` method for `IList`.
- Add `IArrayList` interface which extends `RandomAccess` for random access list and `IList` for other.
- Add `IArray` interface to determine `IArrayList` contains an array or not.
- Add `CultureInfo.setCurrent` to set current locale for string actions in linq.
- Add `StringComparer` for string equals, hashCode and compare actions.
- Add `ValueType` as super class for value type.
- Add support more type cast to IEnumerable like `IEnumerable`, `Iterator` and `Enumeration`.
- Add `Linq.as()` to generate `IEnumerable` from `Object`.
- Add debug view support.
- Add primitive functional interface.
- Add covariant & contravariant support.

### Optimizations
- Remove override runOnce() for `IList`.
- Not copy data when cast primitive array to IEnumerable.
- Not create array when cast singleton to IEnumerable.
- Optimize performance of `takeLast`.
- Optimize `range().select()` and `repeat().select()`.
- Optimize `min`, `max`, `minBy`, `maxBy` performance.
- Optimize `TakeLastIterator` dispose action.
- Optimize `toMap` and `toSet` methods.
- Optimize `indexOf` and `lastIndexOf` in EqualityComparer.
- Optimize `skipLast` performance.
- Optimize performance by use enumerator instead of iterator.
- Implements `IIListProvider` for `DistinctByIterator`, `CrossJoinIterator`, `UnionByIterator`.

### Changes
- Translated all LINQ to Objects API of .net core 3.0.
- Use ThrowHelper to throw Exceptions.
- Rename package `bridge` to `adapter`.
- Rename `Linq.asEnumerable()` to `Linq.of()`.
- Rename `TupleMore` to `TupleN`.
- Rename `Linq.of(CharSequence)` to `Linq.chars(CharSequence)`.
- Update junit version to 5.x.
- Update plugin version to latest.
- Change the year of copyright.

**`Parameters covariant`**
- IEnumerable<T> (T is covariant)
- IEnumerator<T> (T is covariant)
- IGrouping<TKey, TElement> (TKey and TElement are covariant)

**`Parameters contravariant`**
- Comparator<T> (T is contravariant)
- Comparable<T> (T is contravariant)
- IEqualityComparer<T> (T is contravariant)
- IComparison<T> (T is contravariant)
- Action<T1, T2, ... TN> (T1...TN are contravariant)
- Func<T1, T2, ... TResult> (T1...TN are contravariant, TResult is covariant)
- Predicate<T1, T2, ... TN> (T1...TN are contravariant)

# Links
- Download [https://repo1.maven.org/maven2/com/bestvike/linq/3.0.0/](https://repo1.maven.org/maven2/com/bestvike/linq/3.0.0/)
- Source code [https://github.com/timandy/linq/tree/v3.0.0](https://github.com/timandy/linq/tree/v3.0.0)

---

# v2.0.1 Release notes

### Bugs
- Fix `first` `firstOrDefault` bugs.

### Features
- Refactor from .net core 2.
- Performance improvement.

# Links
- Download [https://repo1.maven.org/maven2/com/bestvike/linq/2.0.1/](https://repo1.maven.org/maven2/com/bestvike/linq/2.0.1/)
- Source code [https://github.com/timandy/linq/tree/v2.0.1](https://github.com/timandy/linq/tree/v2.0.1)

---

# v2.0.0 Release notes

### Bugs
- Fix `Comparer.compare()` bugs.

### Features
- Translated all LINQ to Objects API of .net core 2.0.
- Add class `out`, `ref`.
- A lot of refactoring optimization has been done.

### Changes
- Performance improvement.
- Update plugin to latest version.

# Links
- Download [https://repo1.maven.org/maven2/com/bestvike/linq/2.0.0/](https://repo1.maven.org/maven2/com/bestvike/linq/2.0.0/)
- Source code [https://github.com/timandy/linq/tree/v2.0.0](https://github.com/timandy/linq/tree/v2.0.0)

---

# v1.0.1 Release notes

### Bugs
- Fix Buffer count is zero throws NullPointerException.

### Features
- Add API `distinctBy`, `unionBy`, `intersectBy`, `exceptBy`, `minBy`, `maxBy`.
- Support CI.
- Update logo.

# Links
- Download [https://repo1.maven.org/maven2/com/bestvike/linq/1.0.1/](https://repo1.maven.org/maven2/com/bestvike/linq/1.0.1/)
- Source code [https://github.com/timandy/linq/tree/v1.0.1](https://github.com/timandy/linq/tree/v1.0.1)

---

# v1.0.0 Release notes

### Features
- Translated all LINQ to Objects API of .net framework 4.7.
- Add unit test cases.
- Add API `leftJoin`, `rightJoin`, `fullJoin`, `crossJoin`.
- Support cast `CharSequence` to `IEnumerable`.
- Add Tuple definitions.

# Links
- Download [https://repo1.maven.org/maven2/com/bestvike/linq/1.0.0/](https://repo1.maven.org/maven2/com/bestvike/linq/1.0.0/)
- Source code [https://github.com/timandy/linq/tree/v1.0.0](https://github.com/timandy/linq/tree/v1.0.0)
