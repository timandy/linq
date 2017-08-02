<!--自述文件-->
## Linq
#### Summary
- Linq to Objects for Java.

#### Features
- Implemented all API of Linq to Objects.
- More API supported.

#### API of Linq
- asEnumerable
- singletonEnumerable
- range
- repeat
- empty

#### API of IEnumerable
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
- append

#### API of IOrderedEnumerable
- thenBy
- thenByDescending

#### Tuple classes
- Tuple1
- Tuple2
- Tuple3
- Tuple4
- Tuple5
- Tuple6
- Tuple7
- TupleMore

#### Demos
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
