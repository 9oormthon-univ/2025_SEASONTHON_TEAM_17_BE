package shop.maeum.domain.fcm.api.dto.request

data class FcmTokenLoginRequest(
    val token: String,
    val deviceInfo: String
)
