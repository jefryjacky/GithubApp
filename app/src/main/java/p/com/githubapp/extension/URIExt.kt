package p.com.githubapp.extension

import java.net.URI

fun URI.queryParam(key: String): String {
    if(query.isNullOrBlank()) return ""
    val keyValues = query.split("&")
    keyValues.forEach {
        val keyValue = it.split("=")
        if (keyValue.first() == key) return keyValue.last()
    }
    return ""
}