package shop.maeum.global.dto

data class CursorPageResDto<T, C>(
    val data: List<T>,
    val nextCursor: C?,
    val hasNext: Boolean
)