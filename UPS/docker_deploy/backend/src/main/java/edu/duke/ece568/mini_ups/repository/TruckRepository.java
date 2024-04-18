package edu.duke.ece568.mini_ups.repository;

import edu.duke.ece568.mini_ups.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Integer>{
    
}
