package com.javaadvancedg9.JavaAdvancedG9.repository;

import com.javaadvancedg9.JavaAdvancedG9.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByUsername(String username);
    void deleteByUsername(String username);

}
