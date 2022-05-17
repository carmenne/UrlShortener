interface RandomGenerator {
    fun randomAlphaNumeric(length: Int = 6): String
}

class RandomGeneratorImpl : RandomGenerator {
    override fun randomAlphaNumeric(length: Int): String {
        val possibleValues = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length).map { possibleValues.random() }.joinToString("")
    }
}