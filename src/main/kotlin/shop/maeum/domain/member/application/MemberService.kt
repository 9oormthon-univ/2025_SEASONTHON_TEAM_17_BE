package shop.maeum.domain.member.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.member.entity.Member
import shop.maeum.domain.member.repository.MemberRepository

@Service
@Transactional(readOnly = true)
class MemberService (
    private val memberRepository: MemberRepository
){
    fun getByEmail(email: String): Member {
        // TODO: 임시 예외처리 -> 세부 예외처리 필요
        return memberRepository.findByEmail(email)?: throw IllegalArgumentException("Member with email $email not found")
    }

    @Transactional
    fun saveMember(member: Member): Member
    = memberRepository.save(member)

    fun findAllMember(): List<Member>
    = memberRepository.findAll()

    fun getById(id: String): Member {
        return memberRepository.findById(id).orElseThrow { IllegalArgumentException("Member with id $id not found") }
    }
}