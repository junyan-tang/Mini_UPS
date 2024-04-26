package edu.duke.ece568.mini_ups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import edu.duke.ece568.mini_ups.service.UpsService;

@Component
public class MyAppRunner implements CommandLineRunner {

    private final DatabaseInitializer databaseInitializer;
    private final UpsService upsService;
    
    @Autowired
    public MyAppRunner(DatabaseInitializer databaseInitializer,UpsService upsService) {
        this.databaseInitializer = databaseInitializer;
        this.upsService = upsService;
    }

    @Override
    public void run(String... args) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Hello World");
        databaseInitializer.init();
        upsService.start();
    }
}
