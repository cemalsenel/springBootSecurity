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

    // method-based authenteication işlemi için rolebirleştirme metodu
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        // Kisi'nin  izinlerinleri alıp SimpleGrantedAuthority class'ına çevirerek permission adında bir Set'e kaydettik.
        Set<SimpleGrantedAuthority> permission = getPersonPermission().stream()
                .map(per -> new SimpleGrantedAuthority(per.getPermission())).collect(Collectors.toSet());

        // permission Set'i içerisindeki iznileri "ROLE_" sabit kelimesi ile birleştirir.
        permission.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permission;
    }


}
