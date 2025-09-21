package shop.maeum.domain.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class GlobalAuthEntryPoint(

): AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.let { res ->
            res.status = HttpServletResponse.SC_UNAUTHORIZED
            res.contentType = "application/json;charset=UTF-8"

            val jsonResponse = """
                {
                    "error": "UNAUTHORIZED",
                    "message": "로그인 후 사용하여 주십시오"
                }
            """.trimIndent()

            res.writer.write(jsonResponse)
            res.writer.flush()
        }
    }
}