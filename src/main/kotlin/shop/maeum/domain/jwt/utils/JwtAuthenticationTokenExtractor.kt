package shop.maeum.domain.jwt.utils

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import shop.maeum.domain.jwt.component.JwtComponent
import shop.maeum.domain.security.authentication.JwtAuthentication
import shop.maeum.domain.security.authentication.JwtAuthenticationToken

class JwtAuthenticationTokenExtractor(
    private val jwtComponent: JwtComponent,
) {
    fun extractJwtAuthenticationToken(token: String): JwtAuthenticationToken {
        val claims: JwtComponent.Claims = jwtComponent.verify(token)

        val id = claims.id.replace("\"", "")
        val email = claims.email.replace("\"", "")
        val roles = claims.roles.map { it.replace("\"", "") }

        val authorities = roles.map { role -> SimpleGrantedAuthority("ROLE_$role") }

        if (authorities.isEmpty()) throw UsernameNotFoundException("Invalid token")

        return JwtAuthenticationToken(
            JwtAuthentication(id, email),
            authorities
        )
    }


    private fun obtainAuthorities(claims: JwtComponent.Claims): List<GrantedAuthority> {
        val roles: Array<String> = claims.roles
        return if (roles.isEmpty()) {
            emptyList()
        } else {
            roles.map { role -> SimpleGrantedAuthority(role) }
        }
    }
}