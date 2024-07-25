package org.lumeninvestiga.backend.repositorio.tpi.repositories;

import org.lumeninvestiga.backend.repositorio.tpi.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u JOIN FETCH u.userDetail ud WHERE ud.emailAddress =: email")
    Optional<User> findByEmailAddress(String email);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.userDetail.emailAddress = :email")
    boolean existsByEmailAddress(String email);
    boolean existsByUsername(String username);
}
