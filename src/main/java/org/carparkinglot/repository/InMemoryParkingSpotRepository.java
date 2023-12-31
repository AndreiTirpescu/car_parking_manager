package org.carparkinglot.repository;

import org.carparkinglot.model.ParkingSpot;

import java.util.ArrayList;
import java.util.List;

public class InMemoryParkingSpotRepository implements IParkingSpotRepository {

    private final List<ParkingSpot> parkingSpots;

    public InMemoryParkingSpotRepository() {
        this.parkingSpots = new ArrayList<>();
    }

    public InMemoryParkingSpotRepository(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    @Override
    public List<ParkingSpot> findAllByPriceLevel(String priceLevel) {
        return parkingSpots.stream().filter(spot -> spot.getPriceLevel().equals(priceLevel)).toList();
    }

    @Override
    public ParkingSpot findById(Integer id) {
        return parkingSpots.stream().filter(spot -> spot.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public ParkingSpot save(ParkingSpot parkingSpot) {
        parkingSpots.stream().filter(spot -> spot.getId().equals(parkingSpot.getId()))
                .findFirst()
                .ifPresentOrElse(
                        spot -> {
                            spot.setCarNumber(parkingSpot.getCarNumber());
                            spot.setReservedAt(parkingSpot.getReservedAt());
                            spot.setPriceLevel(parkingSpot.getPriceLevel());
                        }, () -> parkingSpots.add(parkingSpot));

        return parkingSpot;
    }

    @Override
    public ParkingSpot findByCar(String car) {
        return parkingSpots.stream().filter(parkingSpot -> parkingSpot.getCarNumber().equals(car)).findFirst()
                .orElse(null);
    }
}
