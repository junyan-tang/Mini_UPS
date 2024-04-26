package edu.duke.ece568.mini_ups.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;


@Entity
public class Package {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    private String status;   // Wait for Pick Up, Out for Delivery, Delivered
    private Integer currentX;
    private Integer currentY;
    private Integer destinationX;
    private Integer destinationY;
    private Date expectedArrivalTime;

    @ManyToOne
    @JoinColumn(name = "truckId", nullable = true)
    private Truck truck;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users users;

    @OneToMany(mappedBy = "packages")
    private List<Item> items;

    public void setTruck(Truck truck) {
        this.truck = truck;
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

    public Integer getDestinationX() {
        return destinationX;
    }

    public void setDestinationX(Integer destinationX) {
        this.destinationX = destinationX;
    }

    public Integer getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(Integer destinationY) {
        this.destinationY = destinationY;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public void setExpectedArrivalTime(Date expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }

    public Users getUser() {
        return users;
    }

    public Truck getTruck() {
        return truck;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setUser(Users users) {
        this.users = users;
    }
}