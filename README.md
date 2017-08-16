<!--自述文件-->
# Linq

[![Build Status](https://travis-ci.org/timandy/linq.svg?branch=master)](https://travis-ci.org/timandy/linq)
[![Codecov](https://codecov.io/gh/timandy/linq/branch/master/graph/badge.svg)](https://codecov.io/gh/timandy/linq)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.bestvike/linq/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.bestvike/linq)
[![GitHub release](https://img.shields.io/github/release/timandy/linq.svg)](https://github.com/timandy/linq/releases)
[![License](https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Linq to Objects for Java.

## Features
- Implemented all API of Linq to Objects.
- More API supported.
- Tuple supported.

![bestvike](logo.jpg "济南百思为科信息工程有限公司")

## API of Linq
- asEnumerable
- singletonEnumerable
- range
- repeat
- empty

## API of IEnumerable
- where
- select
- selectMany
- take
- takeWhile
- skip
- skipWhile
- join
- groupJoin
- orderBy
- orderByDescending
- groupBy
- concat
- zip
- distinct
- union
- intersect
- except
- reverse
- sequenceEqual
- toArray
- toList
- toMap
- toLookup
- defaultIfEmpty
- ofType
- cast
- first
- firstOrDefault
- last
- lastOrDefault
- single
- singleOrDefault
- elementAt
- elementAtOrDefault
- any
- all
- count
- longCount
- contains
- aggregate
- sum
- min
- max
- average
- leftJoin
- rightJoin
- fullJoin
- crossJoin
- distinctBy
- unionBy
- intersectBy
- exceptBy
- minBy
- maxBy
- append

## API of IOrderedEnumerable
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
    <version>1.0.1</version>
</dependency>
```

## Gradle
```
compile 'com.bestvike:linq:1.0.1'
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
Copyright 2017 济南百思为科信息工程有限公司

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
