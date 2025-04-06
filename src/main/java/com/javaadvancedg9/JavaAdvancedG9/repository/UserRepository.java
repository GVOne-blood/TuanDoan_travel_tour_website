package com.javaadvancedg9.JavaAdvancedG9.repository;

import java.util.Optional;

import com.javaadvancedg9.JavaAdvancedG9.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u " +
            "WHERE ( :phone IS NULL OR :phone = '' OR u.phone LIKE %:phone% ) " +
            "AND ( :email IS NULL OR :email = '' OR u.email LIKE %:email% ) " +
            "AND ( :fullname IS NULL OR :fullname = '' OR u.fullname LIKE %:fullname% ) " +
            "AND u.role = 1 " +
            " ORDER BY u.id desc")
    Page<User> findAll(@Param("phone") String phone, @Param("email") String email, @Param("fullname") String fullname, Pageable pageable);

    @Query(value = "select u from User u where u.id = :id")
    Optional<User> findById(@Param("id") Long id);

    @Query(value = "select u from User u where u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query(value = "select u from User u where u.email = :email")
    User getUserByEmail(@Param("email") String email);


}
