<!--变更日志-->
# Change Log

## v3.0.0
- Translated all LINQ to Objects API of .net core 3.0.
- Change the year of copyright.
- Not copy data when cast primitive array to IEnumerable.
- Not create array when cast singleton to IEnumerable.
- use ThrowHelper to throw Exceptions.
- Improve performance of `takeLast`.
- Add zip Tuple API.
- IEnumerable support port to stream.
- Update plugin version to latest.
- Rename package `bridge` to `adapter`.
- Add `IArray` interface to determine `IList` contains an array or not.
- Add method to change default Comparer.
- Add change log file.

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
