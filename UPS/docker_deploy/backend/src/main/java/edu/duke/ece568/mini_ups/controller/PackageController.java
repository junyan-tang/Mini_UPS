package edu.duke.ece568.mini_ups.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.duke.ece568.mini_ups.service.PackageService;

@Controller
public class PackageController {
    @Autowired
    private PackageService packageService;

    @GetMapping("/packages")
    @PreAuthorize("hasRole('USER')")
    public String listPackages(Model model) {
        model.addAttribute("packages", packageService.findAllPackages());
        return "packages";
    }

    @GetMapping("/package/details/{id}")
    @PreAuthorize("hasRole('USER')")
    public String packageDetails(@PathVariable Long id, Model model) {
        model.addAttribute("package", packageService.findById(id));
        return "package-details";
    }

    @GetMapping("/track")
    public ModelAndView trackPackage(@RequestParam("packageId") String packageId) {
        ModelAndView modelAndView = new ModelAndView("redirect:/package/details/" + packageId);
        return modelAndView;
    }
}
