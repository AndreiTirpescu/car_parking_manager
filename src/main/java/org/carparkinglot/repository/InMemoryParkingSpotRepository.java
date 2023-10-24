package org.carparkinglot.repository;

import org.carparkinglot.model.ParkingSpot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<ParkingSpot> findById(Integer id) {
        return parkingSpots.stream().filter(spot -> spot.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<ParkingSpot> findByCarIsNullAndReservedAtIsNullAndPriceLevel(String priceLevel) {
        return parkingSpots.stream().filter(
                        spot -> spot.getCarNumber() == null
                                && spot.getReservedAt() == null
                                && spot.getPriceLevel().equals(priceLevel)
                )
                .findFirst();
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
    public Optional<ParkingSpot> findByCar(String car) {
        return parkingSpots.stream().filter(parkingSpot -> parkingSpot.getCarNumber().equals(car)).findFirst();
    }
}
