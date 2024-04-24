package edu.duke.ece568.mini_ups.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.entity.Package;
import edu.duke.ece568.mini_ups.repository.PackageRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class PackageService {
    @Autowired
    private PackageRepository packageRepository;

    public void save(Package p) {
        packageRepository.save(p);
    }

    public Package findById(Long id) {
        return packageRepository.findById(id).orElse(null);
    }

    public List<Package> findByUserId(Long userId) {
        return packageRepository.findByUsersUserId(userId);
    }

    public List<Package> findByUserName(String userName) {
        return packageRepository.findByUsersUserName(userName);
    } 
    
    public List<Package> findByStatusAndTruckId(String status, Integer truckId) {
        return packageRepository.findByStatusAndTruckTruckId(status, truckId);
    }

    public List<Package> findByStatusAndTruckIdAndLocation(String status, Integer truckId, int x, int y) {
        return packageRepository.findByStatusAndTruckTruckIdAndLocation(status, truckId, x, y);
    }

    public Optional<Package> findByStatusAndTruckIdAndPackageId(String status, Integer truckId, long packageId) {
        return packageRepository.findByStatusAndTruckIdAndPackageId(status, truckId, packageId);
    }

    public int updateStatus(Long packageId, String status) {
        return packageRepository.updateStatus(packageId, status);
    }

    public int updateCurrentLocation(Long packageId, int x, int y) {
        return packageRepository.updateCurrentLocation(packageId, x, y);
    }

    public int updateDestination(Long packageId, int x, int y) {
        return packageRepository.updateDestination(packageId, x, y);
    }

    public List<Package> findAllPackages() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return packageRepository.findByUsersUserName(username);
    }

}
