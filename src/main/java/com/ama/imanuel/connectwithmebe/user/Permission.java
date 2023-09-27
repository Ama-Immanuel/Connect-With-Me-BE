package com.ama.imanuel.connectwithmebe.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_CREATE("admin:create"),

    GROUP_LEADER_READ("group-leader:read"),
    GROUP_LEADER_UPDATE("group-leader:update"),
    GROUP_LEADER_DELETE("group-leader:delete"),
    GROUP_LEADER_CREATE("group-leader:create"),

    MEMBER_READ("member:post"),
    MEMBER_UPDATE("member:read"),
    MEMBER_DELETE("member:delete"),
    MEMBER_CREATE("member:create");

    @Getter
    private final String permission;
}
