package shop.maeum.global.dto

import org.springframework.data.domain.Page

data class PageInfoResDto(
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Long
) {
    companion object {
        fun <T> from(entityPage: Page<T>): PageInfoResDto {
            return PageInfoResDto(
                currentPage = entityPage.number,
                totalPages = entityPage.totalPages,
                totalItems = entityPage.totalElements
            )
        }
    }
}
