package edu.duke.ece568.mini_ups.service;

import java.util.List;
import java.util.Optional;
import edu.duke.ece568.mini_ups.entity.Package;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.duke.ece568.mini_ups.repository.PackageRepository;

@Service
public class PackageService {
    @Autowired
    private PackageRepository packageRepository;

    public List<Package> findAllPackages() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return packageRepository.findAllByUserUsername(username);
    }

    public Package findPackageById(Long id) {
        Optional<Package> packageOpt = packageRepository.findById(id);
        if (packageOpt.isPresent()) {
            return packageOpt.get();
        } else {
            return null;
        }
    }
}
