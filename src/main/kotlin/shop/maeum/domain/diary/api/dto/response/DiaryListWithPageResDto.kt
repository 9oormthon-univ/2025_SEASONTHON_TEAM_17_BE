package shop.maeum.domain.diary.api.dto.response

import shop.maeum.global.dto.PageInfoResDto

data class DiaryListWithPageResDto(
    val diaries: List<DiarySummaryResDto>,
    val pageInfo: PageInfoResDto
)
