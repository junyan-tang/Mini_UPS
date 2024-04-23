package edu.duke.ece568.mini_ups.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.duke.ece568.mini_ups.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
    
}
