<!--变更日志-->
# Change Log

## v3.0.0
- Fix unsigned compare bugs.
- Fix `Lookup.ApplyResultSelector` bugs.
- Fix `OrderedEnumerableRangeEnumerator` not iterate maxIdx.
- Fix `EnumerablePartition` error assert.
- Fix inner enumerator of `CrossJoinIterator` and `SelectManyIterator` dispose twice.
- Fix not check selector param for `sum` with selector.
- Add zip Tuple api.
- Add support port to stream for `IEnumerable`.
- Add api `toLinkedMap`, `toLinkedSet` for `IEnumerable`.
- Add api `indexOf`, `lastIndexOf` for `IEnumerable`.
- Add api `findIndex`, `findLastIndex` for `IEnumerable`.
- Add api `format` for `IEnumerable` to print values.
- Add `IArray` interface to determine `IList` contains an array or not.
- Add `CultureInfo.setCurrent` to set current locale for string actions in linq.
- Add `StringComparer` for string equals, hashCode and compare actions.
- Add `ValueType` as super class for value type.
- Add debug view support.
- Remove override runOnce() for IList.
- Not copy data when cast primitive array to IEnumerable.
- Not create array when cast singleton to IEnumerable.
- Optimize performance of `takeLast`.
- Optimize `range().select()` and `repeat().select()`.
- Optimize `min`, `max`, `minBy`, `maxBy` performance.
- Optimize `TakeLastIterator` dispose action.
- Optimize `toMap` and `toSet` methods.
- Optimize `indexOf` and `lastIndexOf` in EqualityComparer.
- Implements `IIListProvider` for `DistinctByIterator`, `CrossJoinIterator`, `UnionByIterator`.
- Translated all LINQ to Objects API of .net core 3.0.
- Use ThrowHelper to throw Exceptions.
- Rename package `bridge` to `adapter`.
- Update plugin version to latest.
- Change the year of copyright.

## v2.0.1
- Add test cases.
- Fix bugs of `first`, `firstOrDefault`.

## v2.0.0
- Translated all LINQ to Objects API of .net core 2.0.
- Fix `Comparer.compare()` bugs.
- Add class `out`, `ref`.
- A lot of refactoring optimization has been done.
- Update plugin to latest version.

## v1.0.1
- Support CI.
- Update logo.
- Add API `distinctBy`, `unionBy`, `intersectBy`, `minBy`, `maxBy`.

## v1.0.0
- Translated all LINQ to Objects API of .net framework 4.7.
- Add unit test cases.
- Add API `leftJoin`, `rightJoin`, `fullJoin`, `crossJoin`.
- Support cast `CharSequence` to `IEnumerable`.
- Add Tuple definitions.
