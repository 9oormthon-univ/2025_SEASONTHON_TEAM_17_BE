package shop.maeum.domain.jwt.config

import java.util.Date

interface JwtDetails {
    var issuer: String
    var clientSecret: String
    var expiration: Int

    fun getExpirationInMillis(current: Long): Long = current + expiration * 1000L * 60 * 60 * 24

    fun getExpirationDate(current: Date): Date = Date(getExpirationInMillis(current.time))
}