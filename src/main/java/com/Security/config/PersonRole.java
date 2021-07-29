package com.Security.config;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.Security.config.PersonPermission.*;

public enum PersonRole {

    USER(Sets.newHashSet(USER_READ)),
    ADMIN(Sets.newHashSet(ADMIN_READ, ADMIN_WRITE));

    private final Set <PersonPermission> personPermission;

    public Set<PersonPermission> getPersonPermission() {
        return personPermission;
    }

    private PersonRole(Set<PersonPermission> personPermission) {
        this.personPermission = personPermission;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {

        Set<SimpleGrantedAuthority> personPermission = getPersonPermission().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());

        personPermission.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return personPermission;
    }
}
