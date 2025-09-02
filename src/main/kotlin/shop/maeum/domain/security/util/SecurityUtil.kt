package shop.maeum.domain.security.util

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import shop.maeum.domain.security.authentication.JwtAuthenticationToken

@Component
object SecurityUtil {
    fun getCurrentEmail(): String {
        val auth = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        return auth.principal.email
    }
}
