package shop.maeum.global.dto

data class CursorPageResDto <T>(
    val data: List<T>,
    val nextCursor: Long?,  // 다음 페이지를 위한 커서 (없으면 마지막 페이지)
    val hasNext: Boolean      // 다음 페이지 여부
)