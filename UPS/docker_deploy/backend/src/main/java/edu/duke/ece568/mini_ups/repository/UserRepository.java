package edu.duke.ece568.mini_ups.repository;

import edu.duke.ece568.mini_ups.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{
    
}
