package com.javaadvancedg9.JavaAdvancedG9.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javaadvancedg9.JavaAdvancedG9.enumtype.Gender;
// Không cần import enum Role ở đây nữa nếu chỉ dùng Roles entity
import com.javaadvancedg9.JavaAdvancedG9.enumtype.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "user") // Tên bảng là "user"
public class User extends AbstractEntity implements Serializable, UserDetails {

    @Column(unique = true, nullable = false)
    private String username;

    private String fullname;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(insertable = false, updatable = false, columnDefinition = " default 'USER'")
    private Role role;

    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_name", referencedColumnName = "name")
    // "name" là tên cột PK trong bảng "role"
    private Roles roleName; // Tên trường này tham chiếu đến Roles entity

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TinTuc> tintucs;

    // Các trường trạng thái tài khoản cho UserDetails
    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roleName == null || roleName.getName() == null) {
            return Collections.emptyList();
        }
        String authorityString = "ROLE_" + roleName.getName().name(); // .name() để lấy String từ Enum
        return List.of(new SimpleGrantedAuthority(authorityString));
    }


    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}