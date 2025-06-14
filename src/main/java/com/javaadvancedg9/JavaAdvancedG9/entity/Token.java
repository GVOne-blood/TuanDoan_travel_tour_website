package com.javaadvancedg9.JavaAdvancedG9.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "token")
public class Token extends AbstractEntity {

    @Column(name = "username", nullable = false, unique = true)
    String username;
    String ACCESS_TOKEN;
    String REFRESH_TOKEN;
    String RESET_TOKEN;

}
