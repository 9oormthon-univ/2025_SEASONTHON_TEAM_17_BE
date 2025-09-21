package shop.maeum.domain.jwt.component

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.authentication.BadCredentialsException
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
        val accessToken = createToken(claims, accessJwtDetails, "access")
        val refreshToken = createToken(claims, refreshJwtDetails, "refresh")
        print("accessToken: $accessToken")
        print("refreshToken: $refreshToken")
        return TokenPair(accessToken, refreshToken)
    }

    fun createToken(claims: Claims, jwtDetails: JwtDetails, type: String) : String {
        val now = Date()
        val builder: JWTCreator.Builder = JWT.create()
        builder.withIssuer(jwtDetails.issuer)
        builder.withClaim("type", type)
        builder.withIssuedAt(now)
        builder.withExpiresAt(jwtDetails.getExpirationDate(now))
        return builder.sign(jwtHashAlgorithm)
    }

    fun verify(token: String?): Claims {
        try{
           return Claims(jwtVerifier.verify(token))
        }catch (e: Exception){
            throw BadCredentialsException("유효하지 않은 토큰입니다", e)
        }
    }

    fun isRefreshToken(token: String) : Boolean {
        val result = jwtVerifier.verify(token).getClaim("type").asString() == "refresh"
        require(result) { "유효하지 않은 리프레시 토큰입니다." }
        return true
    }

    fun getBlackListInfo(token: String): Pair<String, Date> {
        val decodedJWT = jwtVerifier.verify(token)
        return Pair(
            decodedJWT.getClaim("type").asString(),
            decodedJWT.expiresAt
        )
    }

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