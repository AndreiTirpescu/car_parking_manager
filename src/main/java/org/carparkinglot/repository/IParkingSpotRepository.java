package org.carparkinglot.repository;

import org.carparkinglot.model.ParkingSpot;

import java.util.List;


public interface IParkingSpotRepository {
    List<ParkingSpot> findAllByPriceLevel(String priceLevel);
    ParkingSpot findById(Integer id);
    ParkingSpot save(ParkingSpot parkingSpot);
    ParkingSpot findByCar(String car);
}
