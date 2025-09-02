package shop.maeum.domain.jwt.component

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.stereotype.Component
import shop.maeum.domain.jwt.config.JwtDetails
import shop.maeum.domain.jwt.dto.TokenPair
import java.util.Date

@Component
class JwtComponent(
    private val accessJwtDetails: JwtDetails,
    private val refreshJwtDetails: JwtDetails
) {
    private val jwtHashAlgorithm: Algorithm = Algorithm.HMAC256(accessJwtDetails.clientSecret)
    private val jwtVerifier = JWT.require(jwtHashAlgorithm).withIssuer(accessJwtDetails.issuer).build()

    fun createTokenPair(
        id: String,
        email: String,
        role: String,
    ): TokenPair {
        val claims = Claims.of(id, email, role)
        return createTokenPair(claims)
    }

    fun createTokenPair(claims: Claims): TokenPair {
        val accessToken = createAccessToken(claims)
        val refreshToken = createRefreshToken(claims)
        return TokenPair(accessToken, refreshToken)
    }

    fun createAccessToken(claims: Claims): String {
        val now = Date()
        val builder: JWTCreator.Builder = JWT.create()
        builder.withIssuer(accessJwtDetails.issuer)
        builder.withIssuedAt(now)
        builder.withExpiresAt(accessJwtDetails.getExpirationDate(now))
        builder.withClaim("id", claims.id.toString())
        builder.withClaim("email", claims.email.toString())
        builder.withArrayClaim("roles", claims.roles)
        return builder.sign(jwtHashAlgorithm)
    }

    fun createRefreshToken(claims: Claims): String {
        val now = Date()
        val builder: JWTCreator.Builder = JWT.create()
        builder.withIssuer(refreshJwtDetails.issuer)
        builder.withIssuedAt(now)
        builder.withExpiresAt(refreshJwtDetails.getExpirationDate(now))
        builder.withClaim("id", claims.id.toString())
        builder.withClaim("email", claims.email.toString())
        builder.withArrayClaim("roles", claims.roles)
        return builder.sign(jwtHashAlgorithm)
    }

    fun verify(token: String?): Claims = Claims(jwtVerifier.verify(token))

    class Claims {
        lateinit var id: String
        lateinit var email: String
        lateinit var roles: Array<String>
        lateinit var issuedAt: Date
        lateinit var expiration: Date

        private constructor()

        internal constructor(decodedJWT: DecodedJWT){
            this.id = decodedJWT.getClaim("id").asString()
            this.email = decodedJWT.getClaim("email").asString()
            this.roles = decodedJWT.getClaim("roles").asArray(String::class.java) ?: arrayOf()
            this.issuedAt = decodedJWT.issuedAt
            this.expiration = decodedJWT.expiresAt
        }

        companion object {
            fun of(
                id: String,
                email: String,
                role: String
            ): Claims {
                val claims = Claims()
                claims.id = id
                claims.email = email
                claims.roles = arrayOf(role)
                return claims
            }
        }
    }
}