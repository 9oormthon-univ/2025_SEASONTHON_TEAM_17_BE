package shop.maeum.domain.oauth.api

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import shop.maeum.domain.oauth.api.dto.request.ReissueRequestDto
import shop.maeum.domain.oauth.api.dto.response.OAuthLoginResDto
import shop.maeum.domain.oauth.application.AuthService
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) : AuthControllerDocs {

    @GetMapping("/login")
    override fun kakaoOAuth(
        response: HttpServletResponse
    ) {
        val redirectUrl = authService.getKakaoOAuthRedirectURL()
        response.sendRedirect(redirectUrl)
    }

    @GetMapping("/code/kakao")
    override fun kakaoOAuthCallback(@RequestParam code : String): RspTemplate<OAuthLoginResDto> {
        val loginByKakao = authService.loginByKakao(code)
        return RspTemplate(
            loginByKakao.status,
            loginByKakao.message,
            OAuthLoginResDto(
                loginByKakao.tokenPair.accessToken,
                loginByKakao.tokenPair.refreshToken
            )
        )
    }

    @PostMapping("/reissue")
    override fun reissueToken(@RequestBody request : ReissueRequestDto): RspTemplate<OAuthLoginResDto> {
        return RspTemplate(
            HttpStatus.CREATED,
            "토큰 재발급 성공",
            authService.reissueToken(request.refreshToken)
        )
    }
}
