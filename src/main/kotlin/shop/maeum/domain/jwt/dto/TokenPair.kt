package shop.maeum.domain.jwt.dto

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
