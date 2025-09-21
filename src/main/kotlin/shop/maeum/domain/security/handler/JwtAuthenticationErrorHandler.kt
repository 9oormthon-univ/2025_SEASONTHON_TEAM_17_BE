package shop.maeum.domain.security.handler

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationErrorHandler(
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (ex: AuthenticationException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.characterEncoding = "UTF-8"
            response.contentType = "application/json"
            response.writer.write(
                """
                    {
                        "error": "403",
                        "message":${ex.message?: "로그인 후 이용하여 주십시오."}"
                    }
                """.trimIndent()
            )
        }
    }
}