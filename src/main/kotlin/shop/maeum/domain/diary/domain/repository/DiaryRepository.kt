package shop.maeum.domain.diary.domain.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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

    @Query("""
    SELECT d
    FROM Diary d
    WHERE d.member.id = :memberId
      AND (:cursor IS NULL OR d.id < :cursor)
    ORDER BY d.id DESC
""")
    fun findAllByMemberWithCursor(
        @Param("memberId") memberId: String,
        @Param("cursor") cursor: Long?,
        pageable: Pageable
    ): List<Diary>

    @Query("""
    SELECT d
    FROM Diary d
    WHERE d.member.email = :email
      AND d.privacySetting = 'PUBLIC'
      AND (:cursor IS NULL OR d.id < :cursor)
    ORDER BY d.id DESC
""")
    fun findAllPublicByMemberEmailWithCursor(
        @Param("email") email: String,
        @Param("cursor") cursor: Long?,
        pageable: Pageable
    ): List<Diary>

    @Modifying
    @Query("UPDATE Diary d SET d.createdAt = :createdAt WHERE d.id = :diaryId")
    fun updateCreatedAt(
        @Param("diaryId") diaryId: Long,
        @Param("createdAt") createdAt: LocalDateTime
    ): Int
}