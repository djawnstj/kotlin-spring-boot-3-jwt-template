package com.djawnstj.jwt.user.entity

import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class Role(
    private val permissions: Set<Permission>
) {
    USER(setOf()),
    ADMIN(
        setOf(
            Permission.ADMIN_READ,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_DELETE,
            Permission.ADMIN_CREATE,
            Permission.MANAGER_READ,
            Permission.MANAGER_UPDATE,
            Permission.MANAGER_DELETE,
            Permission.MANAGER_CREATE
        )
    );

    fun getAuthorities(): List<SimpleGrantedAuthority> =
        permissions.map { SimpleGrantedAuthority(it.permission) }
            .toMutableList()
            .let {
                it.add(SimpleGrantedAuthority("ROLE_$name"))
                it.toList()
            }
}