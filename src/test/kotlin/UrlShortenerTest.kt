import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given
import kotlin.test.assertEquals

private const val urlStart = "http://sho.com/"

@ExtendWith(MockitoExtension::class)
class UrlShortenerTest(
    @Mock private val randomGenerator: RandomGenerator
) {
    
    private val urlShortener = UrlShortener(randomGenerator)
    
    @Test
    fun `should return a short url from a long url`() {
        
        val randomAlphanumeric = "rtY24R"
        given(randomGenerator.randomAlphaNumeric()).willReturn(randomAlphanumeric)
        
        val shortUrl = urlShortener.createShortUrl("very long url")
        assertEquals("$urlStart$randomAlphanumeric", shortUrl)
    }
}