package shop.maeum.domain.jwt.config

import org.springframework.beans.factory.annotation.Value
import java.util.Date

class RefreshJwtDetails : JwtDetails {

    @Value("\${jwt.issuer}")
    override lateinit var issuer: String

    @Value("\${jwt.client-secret}")
    override lateinit var clientSecret: String

    @Value("\${jwt.refresh-token-expiration:36000}")
    override var expiration: Int = 0
}