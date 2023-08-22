package com.sparta.petnexus.common.security.info;


import com.sparta.petnexus.user.entity.UserRoleEnum;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;


public interface OAuth2UserInfo extends OAuth2User {

    default Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = UserRoleEnum.USER;
        String authority = role.getAuthority();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        return Collections.singleton(simpleGrantedAuthority);
    }

    String getName();

    String getEmail();

}
