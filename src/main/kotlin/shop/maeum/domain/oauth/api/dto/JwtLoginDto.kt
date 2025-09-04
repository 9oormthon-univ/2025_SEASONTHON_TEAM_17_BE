package shop.maeum.domain.oauth.api.dto

import org.springframework.http.HttpStatus
import shop.maeum.domain.jwt.dto.TokenPair

data class JwtLoginDto(
    val tokenPair: TokenPair,
    val status: HttpStatus,
    val message: String
)
