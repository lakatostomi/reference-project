package com.example.caloriecalculator.service;

import com.example.caloriecalculator.model.Privilege;
import com.example.caloriecalculator.model.Role;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndFetchRoles(email);
        if (user == null) {
            throw  new UsernameNotFoundException("No user has found with this email: " + email);
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<Privilege> privileges = new ArrayList<>();
        List<String> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(role.getName());
            privileges.addAll(role.getPrivileges());
        }
        for (Privilege privilege : privileges) {
            authorities.add(privilege.getName());
        }
        return authorities;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String s : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(s));
        }
        return grantedAuthorities;
    }
}
