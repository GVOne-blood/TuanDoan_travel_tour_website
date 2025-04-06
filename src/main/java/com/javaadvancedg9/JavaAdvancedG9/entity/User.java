package com.javaadvancedg9.JavaAdvancedG9.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javaadvancedg9.JavaAdvancedG9.enumtype.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends AbstractEntity {

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
}

