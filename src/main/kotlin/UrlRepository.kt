import java.util.concurrent.ConcurrentHashMap

// singleton
object UrlRepository {
    fun findByKey(shortUrl: String): LongUrl? = urlStore.get(shortUrl)
    
    fun insert(shortUrl: String, longUrl: LongUrl) = urlStore.putIfAbsent(shortUrl, longUrl)
    fun delete(shortUrl: String) = urlStore.remove(shortUrl)
    
    // key = shortUrl, value = LongUrl
    private val urlStore = ConcurrentHashMap<String, LongUrl>()
}