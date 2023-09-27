package com.ama.imanuel.connectwithmebe.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    USER(
            Collections.emptySet()
    ),
    ADMIN(Set.of(
            Permission.ADMIN_READ,
            Permission.ADMIN_CREATE,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_DELETE,
            Permission.MEMBER_READ,
            Permission.MEMBER_CREATE,
            Permission.MEMBER_DELETE,
            Permission.MEMBER_UPDATE,
            Permission.GROUP_LEADER_READ,
            Permission.GROUP_LEADER_CREATE,
            Permission.GROUP_LEADER_UPDATE,
            Permission.GROUP_LEADER_DELETE
    )),
    GROUP_LEADER(Set.of(
            Permission.GROUP_LEADER_READ,
            Permission.GROUP_LEADER_CREATE,
            Permission.GROUP_LEADER_UPDATE,
            Permission.GROUP_LEADER_DELETE
    )),
    MEMBER(Set.of(
            Permission.MEMBER_READ,
            Permission.MEMBER_CREATE,
            Permission.MEMBER_DELETE,
            Permission.MEMBER_UPDATE
    ));


    @Getter
    final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
