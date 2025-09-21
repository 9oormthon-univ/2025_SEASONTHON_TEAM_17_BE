package shop.maeum.domain.oauth.application

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import shop.maeum.domain.jwt.component.JwtComponent
import shop.maeum.domain.member.application.MemberFacadeService
import shop.maeum.domain.oauth.api.dto.JwtLoginDto
import shop.maeum.domain.oauth.api.dto.response.OAuthLoginResDto
import shop.maeum.domain.oauth.client.KakaoAuthApiClient
import shop.maeum.domain.oauth.client.KakaoMemberInfoApiClient
import shop.maeum.domain.oauth.client.dto.response.KakaoMemberInfoDto
import shop.maeum.domain.oauth.client.dto.response.KakaoOAuth2AccessTokenDto

@Service
class AuthService(

    private val kakaoApiClient: KakaoAuthApiClient,
    private val kakaoMemberInfoApiClient: KakaoMemberInfoApiClient,
    private val memberFacadeService: MemberFacadeService,
    private val jwtComponent: JwtComponent,

    @Value("\${login.kakao.client-id}")
    private val clientId: String,
    @Value("\${login.kakao.redirect-uri}")
    private val redirectUri: String,

) {

    fun getKakaoOAuthRedirectURL(): String {
        return "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=${clientId}" +
                "&redirect_uri=${redirectUri}" +
                "&response_type=code"
    }

    fun loginByKakao(code: String): JwtLoginDto {
        val response : KakaoOAuth2AccessTokenDto = kakaoApiClient.getKakaoAccessToken(
            clientId = clientId,
            redirectUri = redirectUri,
            code = code
        )
        println(response)

        val memberInfo: KakaoMemberInfoDto = kakaoMemberInfoApiClient
            .getKakaoMemberInfo(response.getBearerToken())

        val loginInfo = memberFacadeService.signUpOrLogin(memberInfo)

        val tokenPair = jwtComponent.createTokenPair(
            loginInfo.member.id!!,
            loginInfo.member.email,
            loginInfo.member.memberRole.value
        )

        return JwtLoginDto(
            tokenPair = tokenPair,
            status = loginInfo.status,
            message = loginInfo.message
        )
    }

    fun reissueToken(refreshToken: String): OAuthLoginResDto {
        jwtComponent.isRefreshToken(refreshToken)
        val claims = jwtComponent.verify(refreshToken)
        val tokenPair = jwtComponent.createTokenPair(
            claims.id,
            claims.email,
            claims.roles[0]
        )
        return OAuthLoginResDto(
            accessToken = tokenPair.accessToken,
            refreshToken = tokenPair.refreshToken
        )
    }

}