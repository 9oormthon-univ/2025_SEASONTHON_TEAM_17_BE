package shop.maeum.domain.diary.api.dto.response

data class DiaryTodayResDto(
    val written: Boolean,
    val diaryId: Long? = null,
    val title: String? = null,
    val preview: String? = null
)
