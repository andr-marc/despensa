package com.andremarc.pantry.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class UserAuthorityUtil {

    public static boolean userHasAuthority(String authority) {
        UserDetails userDetails = principal();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.contains(grantedAuthority.getAuthority())) {
                return true;
            }
        }

        return false;
    }

    public static UserDetails principal() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
