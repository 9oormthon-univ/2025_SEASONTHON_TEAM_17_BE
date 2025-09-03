package shop.maeum.domain.fcm.api.dto.response

import shop.maeum.domain.fcm.domain.FcmNotification


data class FcmNotificationResponses(
    val notifications: List<FcmNotificationResponse>
) {
    companion object {
        fun from(list: List<FcmNotification>): FcmNotificationResponses {
            return FcmNotificationResponses(
                notifications = list.map { FcmNotificationResponse.from(it) }
            )
        }
    }
}
