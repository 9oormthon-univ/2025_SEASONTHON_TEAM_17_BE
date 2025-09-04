package shop.maeum.domain.oauth.api.dto.response

data class OAuthLoginResDto (
    val accessToken: String,
    val refreshToken: String
)