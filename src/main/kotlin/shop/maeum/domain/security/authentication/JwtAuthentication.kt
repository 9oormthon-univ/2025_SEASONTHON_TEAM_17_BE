package shop.maeum.domain.security.authentication

import org.springframework.security.core.Authentication

class JwtAuthentication (
    val id: String,
    val email: String,
)
