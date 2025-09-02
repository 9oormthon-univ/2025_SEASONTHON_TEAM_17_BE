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

        val id: String = claims.id.replace("\"", "")
        val authorities: List<GrantedAuthority> = obtainAuthorities(claims)

        if (authorities.isEmpty()) throw UsernameNotFoundException("Invalid token")
        return JwtAuthenticationToken(
            JwtAuthentication(id, token),
            authorities,
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