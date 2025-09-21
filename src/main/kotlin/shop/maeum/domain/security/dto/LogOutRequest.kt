package shop.maeum.domain.security.dto

data class LogOutRequest (
    val accessToken: String?,
    val refreshToken: String?
)