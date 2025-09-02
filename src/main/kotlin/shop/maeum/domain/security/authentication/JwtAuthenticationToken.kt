package shop.maeum.domain.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken : AbstractAuthenticationToken {
    private val principal: JwtAuthentication

    constructor(principal: JwtAuthentication) : super(null) {
        this.principal  = principal
        super.setAuthenticated(true)
    }

    constructor(principal: JwtAuthentication, authorities: Collection<GrantedAuthority>) : super(authorities) {
        this.principal = principal
        super.setAuthenticated(true)
    }

    override fun getCredentials(): String = ""

    override fun getPrincipal(): JwtAuthentication = principal

    override fun setAuthenticated(isAuthenticated: Boolean) {
        super.setAuthenticated(false)
    }
}