package edu.duke.ece568.mini_ups.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Truck {
    @Id
    private Integer truckId;
    private Integer currentX;
    private Integer currentY;
    private String status;   // IDLE, TRAVELLING, LOADING, DELIVERING,ARRIVE WAREHOUSE
    private Integer warehouseId;

    @OneToMany(mappedBy = "truck")
    private List<Package> packages;

    public Integer getTruckId() {
        return truckId;
    }
    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }
    public Integer getCurrentX() {
        return currentX;
    }
    public void setCurrentX(Integer currentX) {
        this.currentX = currentX;
    }
    public Integer getCurrentY() {
        return currentY;
    }
    public void setCurrentY(Integer currentY) {
        this.currentY = currentY;
    }
    public String getStatus() {
        return status;
    }
    public Integer getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}