package shop.maeum.domain.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component
import shop.maeum.domain.jwt.component.JwtComponent
import shop.maeum.domain.redis.service.BlackListRedisService
import shop.maeum.domain.security.dto.LogOutRequest

@Component
class JwtLogoutHandler(
    private val jwtComponent: JwtComponent,
    private val redisService: BlackListRedisService,
    private val objectMapper: ObjectMapper
): LogoutHandler{


    override fun logout(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        request?.let{ req ->
            val body = objectMapper.readValue(req.inputStream, LogOutRequest::class.java)
            listOf(body?.accessToken, body?.refreshToken).forEach { token ->
                if (token != null) {
                    val (type, exp) = jwtComponent.getBlackListInfo(token)
                    val ttl = exp.time - System.currentTimeMillis()
                    redisService.setBlackList(token, type, ttl)
                }
            }
        }
    }
}