package com.javaadvancedg9.JavaAdvancedG9.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javaadvancedg9.JavaAdvancedG9.enumtype.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends AbstractEntity implements Serializable, UserDetails {

    private String username;

    private String fullname;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phone;

    private String email;

    private String address;

    private Integer role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

