package com.cts.edusphere.config.security;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

public record UserPrincipal(UUID userId, String name, Collection<? extends GrantedAuthority> authorities) {

}
