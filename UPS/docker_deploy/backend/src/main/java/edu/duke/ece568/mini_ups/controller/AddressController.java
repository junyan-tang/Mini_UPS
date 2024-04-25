package edu.duke.ece568.mini_ups.controller;

import edu.duke.ece568.mini_ups.entity.Package;
import edu.duke.ece568.mini_ups.service.PackageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddressController {
    private final PackageService packageService;

    @Autowired
    public AddressController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping("/package/updateAddress/{id}")
    public String updateAddress(@PathVariable("id") Long id, 
                                @RequestParam("destinationX") Integer newX, 
                                @RequestParam("destinationY") Integer newY) {
        Package currPackage = packageService.findById(id);
        if (currPackage != null) {
            currPackage.setDestinationX(newX);
            currPackage.setDestinationY(newY);
            packageService.save(currPackage);
            return "redirect:/package/details/" + id;
        }
        return "error";
    }

    @GetMapping("/package/modifyAddress/{id}")
    public String modifyAddress(@PathVariable("id") Long id, Model model) {
        Package currPackage = packageService.findById(id);
        if (currPackage != null && !currPackage.getStatus().equals("out for delivery")) {
            model.addAttribute("package", currPackage);
            return "modify_address";
        } else {
            return "redirect:/packages" + id;
        }
    }
}
