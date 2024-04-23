package edu.duke.ece568.mini_ups.repository;

import edu.duke.ece568.mini_ups.entity.Package;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long>{
    @Query("select p from Package p where p.users.userId = :userId")
    List<Package> findByUsersUserId(long userId);

    @Query("select p from Package p where p.users.username = :userName")
    List<Package> findByUsersUserName(String userName);

    @Query("select p from Package p where p.truck.truckId = :truckId")
    List<Package> findByStatus(String status);

    @Modifying
    @Transactional
    @Query("update Package p set p.status = :status where p.packageId = :packageId")
    int updateStatus(@Param("packageId") long packageId, @Param("status") String status);

    // @Modifying
    // @Transactional
    // @Query("update Package p set p.truck.truckId = :truckId where p.packageId = :packageId")
    // int updateTruck(@Param("packageId") long packageId, @Param("truckId") long truckId);

    @Modifying
    @Transactional
    @Query("update Package p set p.currentX = :x, p.currentY = :y where p.packageId = :packageId")
    int updateCurrentLocation(@Param("packageId") long packageId, @Param("x") int x, @Param("y") int y);

    @Modifying
    @Transactional
    @Query("update Package p set p.destinationX = :x, p.destinationY = :y where p.packageId = :packageId")
    int updateDestination(@Param("packageId") long packageId, @Param("x") int x, @Param("y") int y);

}
