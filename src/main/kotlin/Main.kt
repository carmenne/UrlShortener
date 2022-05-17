import kotlin.reflect.KFunction2

val inputArgumentsFormat = """
            Give arguments in the formats:
            - 0 longUrl [seoKeyword] [ttl], seoKeyword and ttl are optional
            - 1 shortUrl
        """.trimIndent()

fun main(args: Array<String>) {
    
    val urlShortener = UrlShortener(RandomGeneratorImpl())
    when {
        args.size < 2 -> println(inputArgumentsFormat)
        args[0] == "0" -> display(args, urlShortener, ::createShortUrl)
        args[0] == "1" -> display(args, urlShortener, ::fetchLongUrl)
        else -> println("Argument on the first position can only be 0 - to make a short url given a long url " +
                "or 1 - to fetch a long url given a short url")
    }
}

/*
Fetches a long url based on a given short url when the args are in format: 1 shortUrl
 */
fun fetchLongUrl(args: Array<String>, urlShortener: UrlShortener): String? {
    return if (args.size == 2) {
        urlShortener.fetchLongUrl(args[1])?.url
    } else {
        println(inputArgumentsFormat)
        null
    }
}

/*
Creates a short url given a long url (first position), followed by an optional seoKeyword and an optional ttl
in the format 0 longUrl [seoKeyword] [ttl]
 */
private fun createShortUrl(args: Array<String>, urlShortener: UrlShortener): String? {
    return when {
        args.size == 2 -> urlShortener.createShortUrl(longUrl = args[1])
        args.size == 3 && isSeoKeyword(args[2]) -> urlShortener.createShortUrlWithKeyword(longUrl = args[1],
            seoKeyword = args[2])
        args.size == 3 && isTtl(args[2]) -> urlShortener.createShortUrl(longUrl = args[1],
            ttl = args[2].toLong())
        args.size == 4 && isSeoKeyword(args[2]) -> urlShortener.createShortUrlWithKeyword(longUrl = args[1],
            seoKeyword = args[2],
            ttl = args[3].toLong())
        else -> {
            println(inputArgumentsFormat)
            return null
        }
    }
}

private fun display(
    args: Array<String>,
    urlShortener: UrlShortener,
    router: KFunction2<Array<String>, UrlShortener, String?>
) {
    try {
        val result = router(args, urlShortener)
        if (result != null) {
            println(result)
        }
    } catch (exception: CollisionDetected) {
        println("Could not generate a short url at the moment. Please try again later")
    }
}

private fun isSeoKeyword(arg: String) = arg.toLongOrNull() == null
private fun isTtl(arg: String) = arg.toLongOrNull() != null