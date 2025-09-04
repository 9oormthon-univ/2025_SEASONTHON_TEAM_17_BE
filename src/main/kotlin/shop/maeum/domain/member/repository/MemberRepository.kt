package shop.maeum.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import shop.maeum.domain.member.entity.Member

interface MemberRepository : JpaRepository<Member, String> {
    fun findByEmail(email: String): Member?

    @Query("""
    SELECT m FROM Member m
    WHERE m.id > :cursor OR :cursor IS NULL
      AND (LOWER(m.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(m.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')))
    ORDER BY m.id ASC
""")
    fun searchByKeywordWithCursor(
        @Param("keyword") keyword: String,
        @Param("cursor") cursor: Long?,
        @Param("limit") limit: Int
    ): List<Member>

    fun existsByNickname(nickname: String): Boolean
    fun existsByEmail(email: String): Boolean

}