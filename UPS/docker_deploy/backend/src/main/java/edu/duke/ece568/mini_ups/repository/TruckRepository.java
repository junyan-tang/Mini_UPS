package edu.duke.ece568.mini_ups.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.duke.ece568.mini_ups.entity.Truck;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Integer>{
    @Query("SELECT t FROM Truck t WHERE t.status = 'IDLE' ORDER BY (t.currentX - :x) * (t.currentX - :x) + (t.currentY - :y) * (t.currentY - :y) ASC")
    List<Truck> findIdleTrucksSortedByProximity(int x, int y, Pageable pageable);

    @Query("SELECT t FROM Truck t WHERE t.status NOT IN ('TRAVELLING', 'LOADING') ORDER BY (t.currentX - :x) * (t.currentX - :x) + (t.currentY - :y) * (t.currentY - :y) ASC")
    List<Truck> findNonBusyTrucksSortedByProximity(int x, int y, Pageable pageable);
}
