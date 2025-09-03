package shop.maeum.domain.fcm.api.dto.response

import shop.maeum.domain.fcm.api.dto.FcmSendDto

data class FcmSendResponse(
    val body: String,
    val successCount: Int
) {
    companion object {
        fun of(dto: FcmSendDto, successCount: Int): FcmSendResponse {
            return FcmSendResponse(
                body = dto.body,
                successCount = successCount
            )
        }
    }
}
