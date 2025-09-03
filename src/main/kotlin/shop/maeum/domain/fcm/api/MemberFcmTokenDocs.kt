package shop.maeum.domain.fcm.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import shop.maeum.domain.fcm.api.dto.request.FcmTokenLoginRequest
import shop.maeum.domain.fcm.api.dto.request.FcmTokenLogoutRequest
import shop.maeum.global.template.RspTemplate

@Tag(name = "FCM Token", description = "FCM 토큰 관리 API")
interface MemberFcmTokenDocs {

    @Operation(
        summary = "FCM 토큰 저장 또는 활성화 (로그인)",
        description = "로그인 시 클라이언트에서 전달한 FCM 토큰을 저장하거나 기존 토큰을 활성화합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "FCM 토큰 저장/활성화 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "FCM 토큰 저장 응답 예시",
                value = """
                {
                  "statusCode": 200,
                  "message": "로그인 시 FCM 토큰 처리 완료",
                  "data": null
                }
                """
            )]
        )]
    )
    fun saveFcmToken(
        @RequestBody(
            required = true,
            description = "FCM 토큰과 디바이스 정보",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = FcmTokenLoginRequest::class),
                examples = [ExampleObject(
                    name = "로그인 시 요청 예시",
                    value = """
                    {
                      "token": "fcm_token_123456",
                      "deviceInfo": "Android / Galaxy S23"
                    }
                    """
                )]
            )]
        )
        request: FcmTokenLoginRequest
    ): RspTemplate<Void>

    @Operation(
        summary = "FCM 토큰 비활성화 (로그아웃)",
        description = "로그아웃 시 클라이언트에서 전달한 FCM 토큰을 비활성화합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "FCM 토큰 비활성화 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "FCM 토큰 비활성화 응답 예시",
                value = """
                {
                  "statusCode": 200,
                  "message": "로그아웃 시 FCM 토큰 비활성화 완료",
                  "data": null
                }
                """
            )]
        )]
    )
    fun deactivateFcmToken(
        @RequestBody(
            required = true,
            description = "비활성화할 FCM 토큰",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = FcmTokenLogoutRequest::class),
                examples = [ExampleObject(
                    name = "로그아웃 시 요청 예시",
                    value = """
                    {
                      "token": "fcm_token_123456"
                    }
                    """
                )]
            )]
        )
        request: FcmTokenLogoutRequest
    ): RspTemplate<Void>
}
