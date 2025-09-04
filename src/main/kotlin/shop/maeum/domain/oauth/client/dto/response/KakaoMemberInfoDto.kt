package shop.maeum.domain.oauth.client.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoMemberInfoDto (
    val id: Long,
    val kakaoAccount: KakaoAccount?,
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class KakaoAccount(
        val profile: Profile?,
        val email: String?,
    ) {
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
        data class Profile(
            val profileImageUrl: String?,
        )
    }
}
