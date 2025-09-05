package shop.maeum.domain.diary.api.dto.response

import shop.maeum.domain.diary.domain.Diary
import shop.maeum.domain.diary.domain.PrivacySetting

data class PrivacySettingResDto(
    val diaryId: Long,
    val privacySetting: PrivacySetting
) {
    companion object {
        fun fromEntity(diary: Diary): PrivacySettingResDto =
            PrivacySettingResDto(
                diaryId = diary.id!!,
                privacySetting = diary.privacySetting
            )
    }
}
