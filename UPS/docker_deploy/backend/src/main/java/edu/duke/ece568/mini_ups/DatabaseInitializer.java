package edu.duke.ece568.mini_ups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.entity.Users;
import edu.duke.ece568.mini_ups.repository.ItemRepository;
import edu.duke.ece568.mini_ups.repository.PackageRepository;
import edu.duke.ece568.mini_ups.repository.TruckRepository;
import edu.duke.ece568.mini_ups.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class DatabaseInitializer {
    
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PackageRepository packageRepository;
    private final TruckRepository truckRepository;

    @Autowired
    public DatabaseInitializer(UserRepository userRepository, ItemRepository itemRepository, PackageRepository packageRepository, TruckRepository truckRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.packageRepository = packageRepository;
        this.truckRepository = truckRepository;
    }
    
    // @PostConstruct
    @Transactional
    public void init(){
        itemRepository.deleteAllItems();
        packageRepository.deleteAllPackages();
        userRepository.deleteAllUsers();        
        truckRepository.deleteAllTrucks();
        
        Users user = new Users();
        user.setUsername("user1");
        user.setPassword("password");
        user.setEmail("junyan.tang@duke.edu");
        userRepository.save(user);

        System.out.println("Database initialized");
    }
}
