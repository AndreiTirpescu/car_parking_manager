package org.carparkinglot;

import org.carparkinglot.model.ParkingSpot;

import java.time.LocalDateTime;

public record ParkingReceipt(LocalDateTime endTime, String carNumber, String priceLevel, LocalDateTime reservedAt,
                             Double price) {

    public static ParkingReceipt fromTimeAndParkingSpot(LocalDateTime leaveAtTime, ParkingSpot parkingSpot) {
        return new ParkingReceipt(leaveAtTime, parkingSpot.getCarNumber(), parkingSpot.getPriceLevel(),
                parkingSpot.getReservedAt(), parkingSpot.priceForTime(leaveAtTime));
    }

    String print() {

        return String.format("Car: %s\nPrice: %s\nPriceLevel: %s\nReservedAt: %s\nLeftAt: %s\n", carNumber(), price(), priceLevel(), reservedAt(), endTime());
    }
}