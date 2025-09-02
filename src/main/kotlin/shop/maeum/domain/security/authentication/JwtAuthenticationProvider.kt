package shop.maeum.domain.security.authentication

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.util.ClassUtils

class JwtAuthenticationProvider(
    private val jwtUserDetailService: UserDetailsService
) : AuthenticationProvider {

    override fun supports(authentication: Class<*>?): Boolean =
        ClassUtils.isAssignable(JwtAuthenticationToken::class.java, authentication!!)

    override fun authenticate(authentication: Authentication?): Authentication?
    {
        val principal = authentication?.principal as? JwtAuthentication
            ?: throw UsernameNotFoundException("Invalid principal")

        val jwtUserDetails = jwtUserDetailService.loadUserByUsername(principal.email)

        return JwtAuthenticationToken(
            JwtAuthentication(
                id = principal.id,
                email = jwtUserDetails.username
            ),
            jwtUserDetails?.authorities ?: emptyList()
        )
    }

}