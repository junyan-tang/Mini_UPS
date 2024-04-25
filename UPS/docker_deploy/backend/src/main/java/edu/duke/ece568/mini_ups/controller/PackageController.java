package edu.duke.ece568.mini_ups.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import edu.duke.ece568.mini_ups.entity.Package;
import org.springframework.web.bind.annotation.GetMapping;

import edu.duke.ece568.mini_ups.service.PackageService;

@Controller
public class PackageController {
    @Autowired
    private PackageService packageService;

    @GetMapping("/packages")
    public String listPackages(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Package> packages = packageService.findByUserName(username);
        model.addAttribute("packages", packages);
        return "packages";
    }

    @GetMapping("/package/details/{id}")
    public String packageDetails(@PathVariable("id") Long id, Model model) {
        Package currPackage = packageService.findById(id);
        if (currPackage == null) {
            return "error";
        }
        model.addAttribute("package", currPackage);
        return "package_detail";
    }

    @GetMapping("/track")
    public String trackPackage(@RequestParam("packageId") String packageId) {
        return "redirect:/package/details/" + packageId;
    }
}
