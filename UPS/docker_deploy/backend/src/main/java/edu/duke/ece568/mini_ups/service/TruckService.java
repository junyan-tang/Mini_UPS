package edu.duke.ece568.mini_ups.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.entity.Truck;
import edu.duke.ece568.mini_ups.repository.TruckRepository;

@Service
public class TruckService {

    @Autowired
    private TruckRepository truckRepository;

    public Truck findBestAvailableTruck(int destinationX, int destinationY) {
        Pageable limit = PageRequest.of(0, 1);
        List<Truck> idleTrucks = truckRepository.findIdleTrucksSortedByProximity(destinationX, destinationY, limit);
        if (!idleTrucks.isEmpty()) {
            return idleTrucks.get(0);
        }

        List<Truck> nonBusyTrucks = truckRepository.findNonBusyTrucksSortedByProximity(destinationX, destinationY, limit);
        return nonBusyTrucks.isEmpty() ? null : nonBusyTrucks.get(0);
    }

    public void updateStatus(int truckId, String status) {
        truckRepository.updateStatus(truckId, status);
    }

    public void updateLocation(int truckId, int x, int y) {
        truckRepository.updateLocation(truckId, x, y);
    }

    public void save(Truck truck) {
        truckRepository.save(truck);
    }
}
