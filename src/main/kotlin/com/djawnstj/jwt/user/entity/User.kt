package com.djawnstj.jwt.user.entity

import com.djawnstj.jwt.common.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
class User(
    val loginId: String,
    val password: String,
    val name: String,
    @Enumerated(EnumType.STRING)
    val role: Role,
): BaseEntity(), UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = this.role.getAuthorities()

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.loginId

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = this.deletedAt == null
}