package edu.duke.ece568.mini_ups.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.duke.ece568.mini_ups.entity.Item;
import jakarta.transaction.Transactional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

    @Modifying
    @Transactional
    @Query("DELETE FROM Item i")
    void deleteAllItems();
}
