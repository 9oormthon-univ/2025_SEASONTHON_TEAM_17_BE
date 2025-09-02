package shop.maeum.domain.security.authentication

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import shop.maeum.domain.member.application.MemberService
import shop.maeum.domain.member.entity.Member

@Component
class JwtUserDetailService(
    private val memberService: MemberService
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails? {
        val member : Member = memberService.getByEmail(email)
        return JwtUserDetails(
            id = member.id,
            email = member.email,
            roles = listOf(member.memberRole.value)
        )
    }
}