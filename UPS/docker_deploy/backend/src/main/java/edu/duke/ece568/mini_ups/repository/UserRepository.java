package edu.duke.ece568.mini_ups.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.duke.ece568.mini_ups.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{

    @Query("select u from Users u where u.username = :username")
    Optional<Users> findByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM Users u")
    void deleteAllUsers();
}
