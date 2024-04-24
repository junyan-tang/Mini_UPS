package edu.duke.ece568.mini_ups.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.duke.ece568.mini_ups.entity.Package;
import jakarta.transaction.Transactional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long>{

    @Query("select p from Package p where p.packageId = :packageId")
    Package findByPackageId(@Param("packageId") long packageId);

    @Query("select p from Package p where p.users.userId = :userId")
    List<Package> findByUsersUserId(@Param("userId") long userId);

    @Query("select p from Package p where p.users.username = :userName")
    List<Package> findByUsersUserName(@Param("userName") String userName);

    @Query("select p from Package p where p.status = :status")
    List<Package> findByStatus(@Param("status") String status);

    @Query("select p from Package p where p.status = :status and p.truck.truckId = :truckId")
    List<Package> findByStatusAndTruckTruckId(@Param("status") String status, @Param("truckId") Integer truckId);

    @Query("select p from Package p where p.status = :status and p.truck.truckId = :truckId and p.currentX = :x and p.currentY = :y")
    List<Package> findByStatusAndTruckTruckIdAndLocation(@Param("status") String status,@Param("truckId") Integer truckId,@Param("x") int x,@Param("y") int y);

    @Query("select p from Package p where p.status = :status and p.truck.truckId = :truckId and p.packageId = :packageId")
    Optional<Package> findByStatusAndTruckIdAndPackageId(@Param("status") String status,@Param("truckId") Integer truckId,@Param("packageId") long packageId);

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
