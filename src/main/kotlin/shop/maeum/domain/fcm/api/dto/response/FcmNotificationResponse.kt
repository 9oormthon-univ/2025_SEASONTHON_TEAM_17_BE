package shop.maeum.domain.fcm.api.dto.response

import shop.maeum.domain.fcm.domain.FcmNotification
import java.time.LocalDateTime

data class FcmNotificationResponse(
    val id: Long,
    val body: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(notification: FcmNotification): FcmNotificationResponse {
            return FcmNotificationResponse(
                id = notification.id!!,
                body = notification.body,
                createdAt = notification.createdAt!!
            )
        }
    }
}
