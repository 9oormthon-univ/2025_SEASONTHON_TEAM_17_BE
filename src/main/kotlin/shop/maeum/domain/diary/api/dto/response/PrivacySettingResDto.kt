package shop.maeum.domain.diary.api.dto.response

import shop.maeum.domain.diary.domain.Diary
import shop.maeum.domain.diary.domain.PrivacySetting
import shop.maeum.domain.diary.exception.DiaryNotFoundException

data class PrivacySettingResDto(
    val diaryId: Long,
    val privacySetting: PrivacySetting
) {
    companion object {
        fun fromEntity(diary: Diary): PrivacySettingResDto {
            val id = diary.id ?: throw DiaryNotFoundException()
            return PrivacySettingResDto(
                diaryId = id,
                privacySetting = diary.privacySetting
            )
        }
    }
}
