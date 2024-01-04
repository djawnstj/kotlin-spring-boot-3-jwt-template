package com.djawnstj.jwt.user.entity

import com.djawnstj.jwt.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
class User(
    @Column(unique = true)
    val loginId: String,
    val loginPassword: String,
    var name: String,
    @Enumerated(EnumType.STRING)
    val role: Role,
): BaseEntity(), UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = this.role.getAuthorities()

    override fun getPassword(): String = this.loginPassword

    override fun getUsername(): String = this.loginId

    override fun isAccountNonExpired(): Boolean = this.deletedAt == null

    override fun isAccountNonLocked(): Boolean = this.deletedAt == null

    override fun isCredentialsNonExpired(): Boolean = this.deletedAt == null

    override fun isEnabled(): Boolean = this.deletedAt == null
}