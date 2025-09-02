package shop.maeum.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import shop.maeum.domain.member.entity.Member

interface MemberRepository : JpaRepository<Member, String> {
    fun findByEmail(email: String): Member?
}