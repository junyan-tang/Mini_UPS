package edu.duke.ece568.mini_ups.repository;

import edu.duke.ece568.mini_ups.entity.Package;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long>{
    // List<Package> findByUserId(long userId);
    List<Package> findAllByUserUsername(String username);
    // @Transactional
    // @Query("select p from Package p where p.userId = :userId and p.status = :status")
    // int updateStatus(@Param("packageId") long packageId, @Param("status") String status);

}
