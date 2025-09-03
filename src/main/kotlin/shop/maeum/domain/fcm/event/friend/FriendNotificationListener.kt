package shop.maeum.domain.friend.event

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import shop.maeum.domain.fcm.api.dto.FcmSendDto
import shop.maeum.domain.fcm.application.FcmService
import shop.maeum.domain.fcm.event.friend.FriendAcceptedEvent
import shop.maeum.domain.fcm.event.friend.FriendRequestedEvent

@Component
class FriendNotificationListener(
    private val fcmService: FcmService
) {

    @Async
    @EventListener
    fun handleFriendRequested(event: FriendRequestedEvent) {
        val dto = FcmSendDto(
            body = "${event.fromMemberNickname}님이 친구 요청을 보냈습니다."
        )
        fcmService.sendMessageToUser(event.toMemberId, dto)
    }

    @Async
    @EventListener
    fun handleFriendAccepted(event: FriendAcceptedEvent) {
        val dto = FcmSendDto(
            body = "${event.toMemberNickname}님이 친구 요청을 수락했습니다."
        )
        fcmService.sendMessageToUser(event.fromMemberId, dto)
    }
}
