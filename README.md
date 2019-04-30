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

![bestvike](logo.jpg "济南百思为科信息工程有限公司")

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
- any
- all
- append
- prepend
- asEnumerable
- average
- ofType
- cast
- concat
- contains
- count
- longCount
- defaultIfEmpty
- distinct
- `distinctBy`
- elementAt
- elementAtOrDefault
- except
- `exceptBy`
- first
- firstOrDefault
- groupBy
- groupJoin
- intersect
- `intersectBy`
- join
- `leftJoin`
- `rightJoin`
- `fullJoin`
- `crossJoin`
- last
- lastOrDefault
- toLookup
- max
- `maxBy`
- min
- `minBy`
- orderBy
- orderByDescending
- reverse
- select
- selectMany
- sequenceEqual
- single
- singleOrDefault
- skip
- skipWhile
- skipLast
- sum
- take
- takeWhile
- takeLast
- toArray
- toList
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
- average
```
double avg = Linq.range(0, 10).skip(2).take(4).averageInt();
System.out.println(avg);
----
3.5
```
- crossJoin
```
String[] users = {"Fred", "Bill"};
String[] subjects = {"English", "Information"};
String cross = Linq.asEnumerable(users)
        .crossJoin(Linq.asEnumerable(subjects), (user, subject) -> String.format("%s's %s score is 0", user, subject))
        .toList()
        .toString();
System.out.println(cross);
----
[Fred's English score is 0, Fred's Information score is 0, Bill's English score is 0, Bill's Information score is 0]
```
- distinctBy
```
Tuple2[] tuples = {Tuple.create("1", "Fred"), Tuple.create("1", "Bill"), Tuple.create("2", "Eric")};
Linq.asEnumerable(tuples)
        .distinctBy(Tuple2::getItem1)
        .forEach(System.out::println);
----
(1, Fred)
(2, Eric)
```

## *License*
Linq is released under the Apache License, Version 2.0.
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
