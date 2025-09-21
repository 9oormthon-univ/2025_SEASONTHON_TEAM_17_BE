package shop.maeum.domain.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import shop.maeum.domain.jwt.utils.JwtAuthenticationTokenExtractor
import shop.maeum.domain.redis.service.BlackListRedisService
import shop.maeum.domain.security.authentication.JwtAuthenticationToken
import java.net.URLDecoder
import java.util.regex.Pattern

class JwtAuthenticationTokenFilter(
    private val jwtAuthenticationTokenExtractor: JwtAuthenticationTokenExtractor,
    private val whiteList : Array<String>,
    private val redisService: BlackListRedisService
) : OncePerRequestFilter() {
    private val bearerRegex: Pattern = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE)
    private val headerKey: String = "Authorization"

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return whiteList.any { excludePath ->
            path.startsWith(excludePath.replace("/**", ""))
        }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if(!isAlreadyAuthenticated()) {
            try {
                val authorizationToken: String = obtainAuthorizationToken(request)
                val authentication: JwtAuthenticationToken =
                    jwtAuthenticationTokenExtractor.extractJwtAuthenticationToken(authorizationToken)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            } catch (e: AuthenticationException) {
                SecurityContextHolder.clearContext()
                throw e
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun isAlreadyAuthenticated(): Boolean = SecurityContextHolder.getContext().authentication != null

    private fun obtainAuthorizationToken(request: HttpServletRequest): String {

        var token: String = request.getHeader(headerKey) ?:
        throw UsernameNotFoundException("토큰이 비어있습니다")
        token = URLDecoder.decode(token, "UTF-8") ?: throw UsernameNotFoundException("Invalid token")
        require(redisService.isBlacklisted(token)) { throw BadCredentialsException("유효하지 않은 접근입니다") }

        try {
            token = URLDecoder.decode(token, "UTF-8")
            val parts: Array<String> = token.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.size == 2) {
                val scheme = parts[0]
                val credentials = parts[1]
                require(bearerRegex.matcher(scheme).matches())
                {throw BadCredentialsException("인증 타입이 올바르지 않습니다")}
                return credentials
            } else {
                throw UsernameNotFoundException("Invalid token")
            }
        } catch (_: UnsupportedOperationException) {
            throw UsernameNotFoundException("Invalid token")
        }
    }

}