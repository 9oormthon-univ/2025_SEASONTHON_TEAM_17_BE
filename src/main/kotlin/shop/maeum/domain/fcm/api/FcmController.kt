package shop.maeum.domain.fcm.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.fcm.api.dto.FcmSendDto
import shop.maeum.domain.fcm.api.dto.response.FcmNotificationResponses
import shop.maeum.domain.fcm.api.dto.response.FcmSendResponse
import shop.maeum.domain.fcm.application.FcmService
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/fcm")
class FcmController(
    private val fcmService: FcmService
) : FcmDocs{

//    @PostMapping
//    override fun pushMessage(@RequestBody fcmSendDto: FcmSendDto): RspTemplate<FcmSendResponse> {
//        val response = fcmService.sendMessageTo(fcmSendDto)
//        return RspTemplate(
//            httpStatus = HttpStatus.OK,
//            message = "푸시 메시지 전송 성공",
//            data = response
//        )
//    }

    @GetMapping
    override fun getNotification(): RspTemplate<FcmNotificationResponses> {
        val notifications = fcmService.getNotificationsForCurrentUser()
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "사용자 알림 조회 성공",
            data = notifications
        )
    }
}
