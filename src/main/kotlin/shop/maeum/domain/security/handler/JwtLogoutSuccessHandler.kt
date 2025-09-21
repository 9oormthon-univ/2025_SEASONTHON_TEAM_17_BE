package shop.maeum.domain.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component

@Component
class JwtLogoutSuccessHandler(
    private val objectMapper: ObjectMapper,
): LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val result = mapOf(
            "status" to 200,
            "message" to "로그아웃이 정상적으로 완료되었습니다."
        )

        response?.apply {
            contentType = "application/json"
            characterEncoding = "UTF-8"
            status = HttpServletResponse.SC_OK
            writer.write(objectMapper.writeValueAsString(result))
            writer.flush()
        }

    }

}