package com.pk.SimpleToDos.repository;


import com.pk.SimpleToDos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query to check if a user with the given username exists
    boolean existsByUsername(String username);
    User findByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findUsername(@Param("username") String username);

}