package edu.duke.ece568.mini_ups.repository;

import edu.duke.ece568.mini_ups.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Long>{
    
}
