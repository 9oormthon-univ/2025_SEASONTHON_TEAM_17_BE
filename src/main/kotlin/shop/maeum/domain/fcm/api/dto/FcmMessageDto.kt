package shop.maeum.domain.fcm.api.dto

data class FcmMessageDto(
    val validateOnly: Boolean,
    val message: Message
) {
    data class Message(
        val notification: Notification,
        val token: String
    )

    data class Notification(
        val body: String
    )
}
