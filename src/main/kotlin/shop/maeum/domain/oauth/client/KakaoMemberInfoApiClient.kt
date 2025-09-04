package shop.maeum.domain.oauth.client

import feign.Headers
import org.antlr.runtime.Token
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import shop.maeum.domain.oauth.client.dto.response.KakaoMemberInfoDto

@FeignClient(
    name = "kakaoMemberInfoApiClient",
    url = "https://kapi.kakao.com/v2/"
)
interface KakaoMemberInfoApiClient {
    @GetMapping("/user/me")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=UTF-8")
    fun getKakaoMemberInfo(
        @RequestHeader("Authorization") authToken: String
    ): KakaoMemberInfoDto
}