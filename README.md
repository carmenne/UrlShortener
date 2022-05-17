#### Run the application
The application is a console application.
In order to run it, open it in an IDE (e.g. IntelliJ) and run the app,
with the args in the following format:
 - `0 longUrl [shortUrl] [ttl]` in order to create a short url given a long one,
 - `1 shortUrl` in order to fetch the long url given the short one

##### Create a shortUrl
The expected format is: `0 longUrl [shortUrl] [ttl]`, where shortUrl and ttl are optional.
If both shortUrl and ttl are given, it is expected that the order is respected (hortUrl should be before ttl)

##### Fetch a longUrl
The expected format is: `1 shortUrl`

##### Random Key Generation
The random key generation algorithm [randomly chooses](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/random.html) 6 times a character from the
possible set of accepted characters: [A-Za-z0-9]
This means that there can be collisions. 
Collision detection is performed by checking if the randomly generated key is present in
the database. If so, the algorithm continues trying until a new key is generated.

##### Concurrency
The storage chosen is a ConcurrentHashMap in memory. This collection supports
[putIfAbsent](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html#putIfAbsent-K-V-) method which is an atomic operation that puts the value in the map
only if it is present. This means there cannot be 2 different threads at the same time
which are inserting the same value in the database.
In a real scenario (real database), handling the concurrency will depend on the chosen technology.
If a RDMS is chosen, a similar behaviour can be achieved if shortUrl key is a primary key
(inserting a duplicate key will bail because of the database constraint)
If a NoSql is chosen, a technology supporting putIfAbsent may be selected. If that's not the case protection against concurrency can be achieved by having a lock that guards the critical section:
```pseudocode
    val lock = ReentrantLock()
    ...
    lock.lock()
    try {
        if (!database.contains(value) {
            database.insert(value)
        }
    } finally {
        lock.unlock()
    }
```