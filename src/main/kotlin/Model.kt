import java.time.Instant

data class LongUrl(val url: String, val ttl: Long?, val createdAt: Instant) {
    fun stillInUse(): Boolean = (ttl == null) || createdAt.plusSeconds(ttl).isAfter(Instant.now())
}

class CollisionDetected(url: String?) : Exception("There's already a shortened url with the value $url")
