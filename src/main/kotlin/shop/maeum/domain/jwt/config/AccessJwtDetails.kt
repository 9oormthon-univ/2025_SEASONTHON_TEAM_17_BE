package shop.maeum.domain.jwt.config

import org.springframework.beans.factory.annotation.Value

class AccessJwtDetails : JwtDetails {

    @Value("\${jwt.issuer}")
    override lateinit var issuer: String

    @Value("\${jwt.client-secret}")
    override lateinit var clientSecret: String

    @Value("\${jwt.access-token-expiration:3600}")
    override var expiration: Int = 0

}