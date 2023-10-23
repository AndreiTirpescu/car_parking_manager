package org.carparkinglot.model;

import java.time.LocalDateTime;

public class ParkingSpot {
    private Integer id;

    private String priceLevel;

    private String carNumber;

    private LocalDateTime reservedAt;

    public ParkingSpot(Integer id, String priceLevel, String carNumber, LocalDateTime reservedAt) {
        this.id = id;
        this.priceLevel = priceLevel;
        this.carNumber = carNumber;
        this.reservedAt = reservedAt;
    }

    public Integer getId() {
        return id;
    }

    public String getPriceLevel() {
        return priceLevel;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setReservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }
}
