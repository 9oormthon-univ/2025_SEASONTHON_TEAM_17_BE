package shop.maeum.domain.fcm.event.emotion

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import shop.maeum.domain.fcm.api.dto.FcmSendDto
import shop.maeum.domain.fcm.application.FcmService

@Component
class EmotionNotificationListener(
    private val fcmService: FcmService
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleEmotionLiked(event: EmotionLikedEvent) {
        println("🔥 EmotionLikedEvent received: $event")

        val dto = FcmSendDto(
            body = "${event.likerNickname}님이 '${event.emotionType}' 감정에 공감했어요."
        )

        fcmService.sendMessageToUser(event.diaryAuthorId, dto)
    }

}
