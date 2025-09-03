package shop.maeum.domain.fcm.api

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import shop.maeum.domain.fcm.api.dto.request.FcmTokenLoginRequest
import shop.maeum.domain.fcm.api.dto.request.FcmTokenLogoutRequest
import shop.maeum.domain.fcm.application.MemberFcmTokenService
import shop.maeum.global.template.RspTemplate

@RestController
@RequestMapping("/api/v1/fcm/token")
class MemberFcmTokenController(
    private val memberFcmTokenService: MemberFcmTokenService
) {

    @PostMapping("/login")
    fun saveFcmToken(@RequestBody request: FcmTokenLoginRequest): RspTemplate<Void> {
        memberFcmTokenService.saveOrActivateFcmToken(request.token, request.deviceInfo)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "로그인 시 FCM 토큰 처리 완료"
        )
    }

    @PostMapping("/logout")
    fun deactivateFcmToken(@RequestBody request: FcmTokenLogoutRequest): RspTemplate<Void> {
        memberFcmTokenService.deactivateFcmToken(request.token)
        return RspTemplate(
            httpStatus = HttpStatus.OK,
            message = "로그아웃 시 FCM 토큰 비활성화 완료"
        )
    }
}
