package edu.duke.ece568.mini_ups.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import edu.duke.ece568.mini_ups.repository.PackageRepository;

@Controller
public class PackageController {
    @Autowired
    private PackageRepository packageRepository;
}
