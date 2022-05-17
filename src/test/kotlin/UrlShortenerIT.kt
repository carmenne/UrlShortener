import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

var i = 1
private const val urlStart = "http://sho.com/"

class RandomGeneratorDummy : RandomGenerator {
    override fun randomAlphaNumeric(length: Int): String {
        return if (i++ % 3 > 0) "DfE456" else "AbC123"
    }
}

class UrlShortenerIT {
    
    class HappyScenario {
        private val urlShortener = UrlShortener(RandomGeneratorImpl())
        
        @Test
        fun `should return a shortUrl`() {
            val shortUrl = urlShortener.createShortUrl("very long url")
            assertTrue { shortUrl.startsWith(urlStart) }
            assertEquals(urlStart.length + 6, shortUrl.length)
            
        }
    }
    
    class UnhappyScenario {
        private val urlShortener = UrlShortener(RandomGeneratorDummy())
        
        @Test
        fun `should return a new shortUrl if the previous one already exists`() {
            val shortUrl = urlShortener.createShortUrl("very long url")
            assertEquals("${urlStart}DfE456", shortUrl)
            
            val subsequentShortUrl = urlShortener.createShortUrl("very long url")
            assertEquals("${urlStart}AbC123", subsequentShortUrl)
        }
        
        @Test
        fun `should throw exception if the chosen seoKeyword is already chosen by another user`() {
            val shortUrlWithSeoKeyword = urlShortener.createShortUrlWithKeyword("very long url", "WzY789")
            assertEquals("${urlStart}WzY789", shortUrlWithSeoKeyword)
            
            
            assertThrows<CollisionDetected> {
                urlShortener.createShortUrlWithKeyword("very long url", "WzY789")
            }
            
        }
    }
}