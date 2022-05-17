import java.time.Instant
import java.util.Timer
import kotlin.concurrent.schedule

class UrlShortener(private val randomGenerator: RandomGenerator) {
    
    fun createShortUrl(longUrl: String, ttl: Long = TTL): String {
        var trial = 0
        while (true) {
    
            trial++
            val shortUrl = randomGenerator.randomAlphaNumeric()
            
            if (persistAndScheduleForRemoval(shortUrl, longUrl, ttl)) return "$urlStart$shortUrl"
    
            if (trial == NUMBER_Of_TRIALS) {
                throw CollisionDetected(shortUrl)
            }
        }
    }
    
    fun createShortUrlWithKeyword(longUrl: String, seoKeyword: String, ttl: Long = TTL): String {
        if (persistAndScheduleForRemoval(seoKeyword, longUrl, ttl)) {
            return "$urlStart$seoKeyword"
        }
        throw CollisionDetected("$urlStart$seoKeyword")
    }
    
    fun fetchLongUrl(shortUrl: String) = UrlRepository.findByKey(shortUrl)
    
    private fun persistAndScheduleForRemoval(
        shortUrl: String,
        longUrl: String,
        ttl: Long
    ): Boolean {
        val insertedShortUrl =
            UrlRepository.insert(shortUrl, LongUrl(url = longUrl, ttl = ttl, createdAt = Instant.now()))?.url
        
        if (insertedShortUrl == null) {
            scheduleRemoval(shortUrl, ttl)
            return true
        }
        return false
    }
    
    private fun scheduleRemoval(shortUrl: String, ttl: Long) =
        Timer().schedule(ttl * 1000) { UrlRepository.delete(shortUrl) }
    
    companion object {
        private const val TTL = 864_000L // 10 days * 24 hours * 60 minutes * 60 seconds
        private const val urlStart = "http://sho.com/"
        private const val NUMBER_Of_TRIALS = 5
    }
}