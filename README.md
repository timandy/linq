<!--自述文件-->
# LINQ to Objects (Java)

[![Build Status](https://travis-ci.org/timandy/linq.svg?branch=master)](https://travis-ci.org/timandy/linq)
[![Codecov](https://codecov.io/gh/timandy/linq/branch/master/graph/badge.svg)](https://codecov.io/gh/timandy/linq)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.bestvike/linq/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.bestvike/linq)
[![GitHub release](https://img.shields.io/github/release/timandy/linq.svg)](https://github.com/timandy/linq/releases)
[![License](https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

The term "LINQ to Objects" refers to the use of LINQ queries with any `IEnumerable<T>`.
You can use LINQ to query any enumerable collections such as `Primitive Array`, `Object Array`, `List`, `Collection` or `Iterable` and so on.
The collection may be user-defined or may be returned by a JDK API.

In a basic sense, LINQ to Objects represents a new approach to collections.
In the old way, you had to write complex `foreach` loops that specified how to retrieve data from a collection.
In the LINQ approach, you write declarative code that describes what you want to retrieve.

In addition, LINQ queries offer three main advantages over traditional `foreach` loops:
1. They are more concise and readable, especially when filtering multiple conditions.
2. They provide powerful filtering, ordering, and grouping capabilities with a minimum of application code.
3. They can be ported to other data sources with little or no modification.

LINQ queries also have some advantages over stream API:
1. Support `foreach` loops therefore you can break loop at any time.
2. IEnumerable can be traversed repeatedly.
3. LINQ is very easy to use, like `ToCollection`, `LeftJoin` and so on.
4. LINQ is faster than stream API in most cases.

In general, the more complex the operation you want to perform on the data, the more benefit you will realize by using LINQ instead of traditional iteration techniques.

## Features
- Implemented all API of LINQ to Objects.
- More API supported.
- Tuple supported.
- Cast IEnumerable to stream or parallel stream supported.
- Android supported.
- Java 8 & 11 supported.
- More than 2000 test cases passed.

![bestvike](logo.jpg "济南百思为科信息工程有限公司")

## Navigation
![linq](linq.svg "LINQ to Objects")

## API of Linq
- empty
- singleton
- asEnumerable
- range
- repeat

## API of IEnumerable
- `forEach`
- `stream`
- `parallelStream`
- aggregate
- all
- any
- append
- asEnumerable
- average
- cast
- concat
- contains
- count
- `crossJoin`
- defaultIfEmpty
- distinct
- `distinctBy`
- elementAt
- elementAtOrDefault
- except
- `exceptBy`
- `findIndex`
- `findLastIndex`
- first
- firstOrDefault
- `format`
- `fullJoin`
- groupBy
- groupJoin
- `indexOf`
- intersect
- `intersectBy`
- join
- last
- `lastIndexOf`
- lastOrDefault
- `leftJoin`
- longCount
- max
- `maxBy`
- min
- `minBy`
- ofType
- orderBy
- orderByDescending
- prepend
- reverse
- `rightJoin`
- `runOnce`
- select
- selectMany
- sequenceEqual
- `shuffle`
- single
- singleOrDefault
- skip
- skipLast
- skipWhile
- sum
- take
- takeLast
- takeWhile
- toArray
- `toLinkedMap`
- `toLinkedSet`
- toList
- toLookup
- toMap
- toSet
- union
- `unionBy`
- where
- zip

## API of IGrouping extends IEnumerable
- getKey

## API of ILookup extends IEnumerable
- getCount
- get
- containsKey

## API of IOrderedEnumerable extends IEnumerable
- thenBy
- thenByDescending

## Tuple classes
- Tuple1
- Tuple2
- Tuple3
- Tuple4
- Tuple5
- Tuple6
- Tuple7
- TupleMore

## Debug View (IntelliJ IDEA)
1. Open setting dialog `File | Settings | Build, Execution, Deployment | Debugger | Data Views | Java Type Renderers`.
2. Click `Add` button.
3. Input `Renderer name`, like `IterableView`.
4. Input `java.lang.Iterable` for `Apply renderer to objects of type (fully-qualified name):`.
5. Select `Use following expression:` and input `DebugView.getDebuggerDisplay(this)` for `When rendering a node`. If got error, press `Alt + Enter` to import class.
6. Select `Use list of expression:`, input `**RESULT VIEW**` in `Name` column and input `DebugView.getDebuggerTypeProxy(this)` in `Expression` column. If got error, press `Alt + Enter` to import class. Suggest check `On-demand` to enumerate sequence when needed.
7. Check `Append default children` if you want to watch default fields value of `Iterable` instance.
8. Save settings, enjoy!

`Result view should be used with caution because of possible side-effects.`

## Maven
```
<dependency>
    <groupId>com.bestvike</groupId>
    <artifactId>linq</artifactId>
    <version>2.0.1</version>
</dependency>
```

## Gradle
```
compile 'com.bestvike:linq:2.0.1'
```

## Demos

- Join not empty elements.
```
String result = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
        .where(x -> x != null && x.length() > 0)
        .aggregate((x, y) -> x + ", " + y);

System.out.println(result);
----
!@#$%^, C, AAA, Calling Twice, SoS
```

- Determine all positive numbers is even or not.
```
boolean result = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
        .where(x -> x > 0)
        .all(x -> x % 2 == 0);

System.out.println(result);
----
false
```

- Determine any positive number is even or not.
```
boolean result = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
        .where(x -> x > 0)
        .any(x -> x % 2 == 0);

System.out.println(result);
----
true
```

- Append one element after tail and prepend two elements before head.
```
String result = Linq.range(3, 2).append(5).prepend(2).prepend(1).format();

System.out.println(result);
----
[1, 2, 3, 4, 5]
```

- Compute average of double sequence.
```
double result = Linq.asEnumerable(5, -10, 15, 40, 28).averageInt();

System.out.println(result);
----
15.6
```

`More demos to be added.`

## *License*
LINQ to Objects (Java) is released under the Apache License, Version 2.0.
```
Copyright 2019 济南百思为科信息工程有限公司

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
