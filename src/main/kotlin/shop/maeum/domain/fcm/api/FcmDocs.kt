package shop.maeum.domain.fcm.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import shop.maeum.domain.fcm.api.dto.FcmSendDto
import shop.maeum.domain.fcm.api.dto.response.FcmNotificationResponses
import shop.maeum.domain.fcm.api.dto.response.FcmSendResponse
import shop.maeum.global.template.RspTemplate

@Tag(name = "FCM", description = "Firebase Cloud Messaging 관련 API")
interface FcmDocs {

    @Operation(
        summary = "푸시 메시지 전송",
        description = "현재 로그인한 사용자의 등록된 디바이스에 푸시 메시지를 전송합니다. successCount는 성공적으로 전송된 디바이스 수를 나타냅니다. 같은 유저가 폰으로 혹은 아이패드로 접속할 수도 있으니.."
    )
    @ApiResponse(
        responseCode = "200",
        description = "푸시 메시지 전송 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "푸시 메시지 전송 응답 예시",
                value = """
                {
                  "statusCode": 200,
                  "message": "푸시 메시지 전송 성공",
                  "data": {
                    "title": "공지사항",
                    "body": "새로운 업데이트가 있습니다.",
                    "successCount": 1
                  }
                }
                """
            )]
        )]
    )
    fun pushMessage(
        @RequestBody(
            required = true,
            description = "FCM 푸시 메시지 요청 DTO",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = FcmSendDto::class),
                examples = [ExampleObject(
                    name = "푸시 메시지 요청 예시",
                    value = """
                    {
                      "body": "새로운 업데이트가 있습니다."
                    }
                    """
                )]
            )]
        )
        fcmSendDto: FcmSendDto
    ): RspTemplate<FcmSendResponse>

    @Operation(
        summary = "사용자 알림 조회",
        description = "현재 로그인한 사용자의 알림을 최신순으로 최대 5개까지 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "알림 조회 성공",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = RspTemplate::class),
            examples = [ExampleObject(
                name = "알림 조회 응답 예시",
                value = """
                {
                  "statusCode": 200,
                  "message": "사용자 알림 조회 성공",
                  "data": {
                    "notifications": [
                      {
                        "id": 1,
                        "body": "서비스 점검 안내",
                        "createdAt": "2025-09-03T14:30:00"
                      },
                      {
                        "id": 2,
                        "body": "가을맞이 쿠폰이 도착했어요!",
                        "createdAt": "2025-09-02T12:00:00"
                      }
                    ]
                  }
                }
                """
            )]
        )]
    )
    fun getNotification(): RspTemplate<FcmNotificationResponses>
}
