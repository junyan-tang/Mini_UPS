package edu.duke.ece568.mini_ups.service;

public class DestStruct {
    public final Long packageId;
    public final Integer newX;
    public final Integer newY;
    public DestStruct(Long packageId, Integer newX, Integer newY) {
        this.packageId = packageId;
        this.newX = newX;
        this.newY = newY;
    }
}
