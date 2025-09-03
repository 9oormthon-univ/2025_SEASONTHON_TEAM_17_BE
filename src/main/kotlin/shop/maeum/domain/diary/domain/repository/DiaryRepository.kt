package shop.maeum.domain.diary.domain.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import shop.maeum.domain.diary.domain.Diary
import java.time.LocalDateTime

interface DiaryRepository : JpaRepository<Diary, Long> {
    fun findAllByMemberIdOrderByCreatedAtDesc(
        memberId: String, pageable: Pageable
    ): Page<Diary>

    fun findAllByMemberIdAndCreatedAtBetween(
        memberId: String, createdAt: LocalDateTime, createdAt2: LocalDateTime
    ): List<Diary>

    fun findFirstByMemberIdAndCreatedAtBetween(
        memberId: String, createdAt: LocalDateTime, createdAt2: LocalDateTime
    ): Diary?

    fun existsByMemberIdAndCreatedAtBetween(
        memberId: String, createdAt: LocalDateTime, createdAt2: LocalDateTime
    ): Boolean

}