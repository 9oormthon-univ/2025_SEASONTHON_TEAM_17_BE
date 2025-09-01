package shop.maeum.domain.diary.api.dto.response

import shop.maeum.domain.diary.domain.Diary

data class DiarySummaryResDto(
    val diaryId: Long,
    val title: String,
    val preview: String,
    val privacySetting: String,
    val createdAt: String
) {
    companion object {
        fun fromEntity(diary: Diary): DiarySummaryResDto {
            return DiarySummaryResDto(
                diaryId = diary.id!!,
                title = diary.title,
                preview = diary.content.take(30), // 앞 30자만 잘라서 미리보기 했음
                privacySetting = diary.privacySetting.name,
                createdAt = diary.createdAt.toString()
            )
        }
    }
}