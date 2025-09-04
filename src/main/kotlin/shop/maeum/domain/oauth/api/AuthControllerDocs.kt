package shop.maeum.domain.oauth.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.RequestParam
import shop.maeum.domain.oauth.api.dto.response.OAuthLoginResDto
import shop.maeum.global.template.RspTemplate

@Tag(name = "OAuth", description = "소셜 로그인 관련 API")
interface AuthControllerDocs {

    @Operation(
        summary = "카카오 소셜 로그인 요청",
        description = """
        카카오 OAuth2 로그인 요청을 시작합니다.  
        사용자가 이 API를 호출하면 302 Redirect 응답을 내려주며,  
        사용자가 [동의하고 계속하기] 선택, 로그인 진행 화면으로 이동합니다.  
    """
    )
    @ApiResponse(
        responseCode = "302",
        description = "카카오 로그인 페이지로 리다이렉트",
        content = [
            Content(
                mediaType = "text/html",
                examples = [
                    ExampleObject(
                        name = "Redirect Example",
                        value = """
                                Redirecting to: https://kauth.kakao.com/oauth/authorize?client_id=...&redirect_uri=...&response_type=code
                            """
                    )
                ]
            )
        ]
    )
    fun kakaoOAuth(response: HttpServletResponse)

    @Operation(
        summary = "카카오 OAuth 로그인 콜백",
        description = "카카오 로그인 후 발급된 authorization code를 @RequestParam에 넣어 요청하면, 사용자 정보 조회 후 JWT 토큰(access, refresh)을 반환합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "로그인 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "카카오 로그인 성공 응답 예시",
                value = """
            {
              "statusCode": 200,
              "message": "로그인에 성공했습니다.",
              "data": {
                "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4..."
              }
            }
            """
            )]
        )]
    )
    fun kakaoOAuthCallback(
        @RequestParam(
            name = "code",
            required = true
        )
        code: String
    ): RspTemplate<OAuthLoginResDto>
}