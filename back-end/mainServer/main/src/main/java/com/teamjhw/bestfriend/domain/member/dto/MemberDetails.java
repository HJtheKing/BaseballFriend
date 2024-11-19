package com.teamjhw.bestfriend.domain.member.dto;

import com.teamjhw.bestfriend.entity.MemberRole;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
public class MemberDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String pw;
    private final MemberRole role;

    public MemberDetails(Long id, String email,String pw, MemberRole role) {
        this.id = id;
        this.email = email;
        this.pw = pw;
        this.role = role;
    }

    @Override
    public String getPassword() {
        return this.pw;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getKey()));
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("role", role);

        return map;
    }
}
