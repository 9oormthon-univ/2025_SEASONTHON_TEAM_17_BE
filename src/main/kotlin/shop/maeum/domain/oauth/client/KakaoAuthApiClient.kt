package shop.maeum.domain.oauth.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import shop.maeum.domain.oauth.client.dto.response.KakaoOAuth2AccessTokenDto

@FeignClient(
    name = "kakaoApiClient",
    url = "https://kauth.kakao.com"
)
interface KakaoAuthApiClient {
    @GetMapping("/oauth/authorize")
    fun kakaoOAuth2(
        @RequestParam("client_id") clientId: String,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("response_type") responseType: String = "code"
    )

    @PostMapping(
        "/oauth/token",
        headers = ["Content-Type=application/x-www-form-urlencoded;charset=utf-8"]
    )
    fun getKakaoAccessToken(
        @RequestParam("grant_type") grantType: String = "authorization_code",
        @RequestParam("client_id") clientId: String,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("code") code: String
        ) : KakaoOAuth2AccessTokenDto
}