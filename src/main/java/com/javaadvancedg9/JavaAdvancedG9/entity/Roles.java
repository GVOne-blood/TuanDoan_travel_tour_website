package com.javaadvancedg9.JavaAdvancedG9.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor @Setter @Getter
@Entity
@Table(name = "role")
public class Roles {

    @Id
    @Enumerated(EnumType.STRING)
    private com.javaadvancedg9.JavaAdvancedG9.enumtype.Role name;

    private String permission;

    @OneToMany(mappedBy = "roleName", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users;
}
