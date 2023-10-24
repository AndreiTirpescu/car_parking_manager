package org.carparkinglot.repository;

import org.carparkinglot.model.ParkingSpot;

import java.util.List;
import java.util.Optional;


public interface IParkingSpotRepository {
    List<ParkingSpot> findAllByPriceLevel(String priceLevel);
    ParkingSpot findById(Integer id);
    Optional<ParkingSpot> findByCarIsNullAndReservedAtIsNullAndPriceLevel(String priceLevel);
    ParkingSpot save(ParkingSpot parkingSpot);
    Optional<ParkingSpot> findByCar(String car);
}
