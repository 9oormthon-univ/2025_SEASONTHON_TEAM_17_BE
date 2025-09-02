package shop.maeum.domain.security.authentication

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtUserDetails(
    private val id: String?,
    private val email: String,
    private val roles: List<String>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?>? = roles.map { SimpleGrantedAuthority(it) }

    override fun getPassword(): String? = ""

    override fun getUsername(): String? = email

    fun getId(): String? = id
}