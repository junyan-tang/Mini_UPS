package edu.duke.ece568.mini_ups.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.duke.ece568.mini_ups.entity.Truck;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Integer>{
    @Query("SELECT t FROM Truck t WHERE t.status = 'IDLE' ORDER BY (t.currentX - :x) * (t.currentX - :x) + (t.currentY - :y) * (t.currentY - :y) ASC")
    List<Truck> findIdleTrucksSortedByProximity(@Param("x") int x,@Param("y") int y, Pageable pageable);

    @Query("SELECT t FROM Truck t WHERE t.status NOT IN ('TRAVELLING', 'LOADING') ORDER BY (t.currentX - :x) * (t.currentX - :x) + (t.currentY - :y) * (t.currentY - :y) ASC")
    List<Truck> findNonBusyTrucksSortedByProximity(@Param("x") int x,@Param("y") int y, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Truck t SET t.status = :status WHERE t.truckId = :truckId")
    int updateStatus(@Param("truckId") int truckId,@Param("status") String status);

    @Modifying
    @Transactional
    @Query("UPDATE Truck t SET t.currentX = :x, t.currentY = :y WHERE t.truckId = :truckId")
    int updateLocation(@Param("truckId") int truckId,@Param("x") int x,@Param("y") int y);
}
