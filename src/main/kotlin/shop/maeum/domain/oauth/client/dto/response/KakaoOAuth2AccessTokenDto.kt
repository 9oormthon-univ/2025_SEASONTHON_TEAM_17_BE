package shop.maeum.domain.oauth.client.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoOAuth2AccessTokenDto(
    val tokenType: String,
    val accessToken: String,
    val expireIn: Int,
    val refreshToken: String,
    val refreshTokenExpireIn: Int,
) {
    fun getBearerToken(): String = "Bearer $accessToken"
}
