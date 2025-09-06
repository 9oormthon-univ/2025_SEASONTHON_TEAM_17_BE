package shop.maeum.domain.member.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import shop.maeum.domain.member.entity.Member

interface MemberRepository : JpaRepository<Member, String> {
    fun findByEmail(email: String): Member?

    @Query("""
    SELECT m FROM Member m
    WHERE (:cursor IS NULL OR m.id > :cursor)
      AND m.id <> :currentMemberId
      AND (
        LOWER(m.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(m.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY m.id ASC
""")
    fun searchMembersWithCursor(
        @Param("keyword") keyword: String,
        @Param("currentMemberId") currentMemberId: String,
        @Param("cursor") cursor: Long?,
        pageable: Pageable
    ): List<Member>

    fun existsByNickname(nickname: String): Boolean
    fun existsByEmail(email: String): Boolean

}