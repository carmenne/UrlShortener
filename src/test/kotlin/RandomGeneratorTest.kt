import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RandomGeneratorTest {
    
    private val randomGenerator = RandomGeneratorImpl()
    
    @Test
    fun `should return a random string of 6 alphanumeric characters by default`() {
        val randomString = randomGenerator.randomAlphaNumeric()
        assertEquals(6, randomString.length)
    }
}