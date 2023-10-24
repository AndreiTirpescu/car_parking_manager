package org.carparkinglot;

import org.carparkinglot.exception.CarNotFoundException;
import org.carparkinglot.exception.CarParkingFullException;
import org.carparkinglot.exception.ParkingLotNotFoundException;
import org.carparkinglot.model.ParkingSpot;
import org.carparkinglot.repository.IParkingSpotRepository;

import java.time.LocalDateTime;

public class CarParkingManager {
    private final IParkingSpotRepository repository;

    public CarParkingManager(IParkingSpotRepository repository) {
        this.repository = repository;
    }

    public ParkingSpot parkCar(String carNumber, String priceLevel, LocalDateTime reservedAt) {
        ParkingSpot spot = repository.findByCarIsNullAndReservedAtIsNullAndPriceLevel(priceLevel)
                .orElseThrow(CarParkingFullException::new);

        return addCar(carNumber, spot.getId(), reservedAt);
    }

    public void endParking(String carNumber) {
        ParkingSpot parkingSpot = repository.findByCar(carNumber).orElseThrow(CarNotFoundException::new);

        removeCar(parkingSpot.getId());
    }

    public String printReceipt(String car, LocalDateTime endTime) {
        ParkingSpot parkingSpot = repository.findByCar(car).orElseThrow(CarNotFoundException::new);

        return ParkingReceipt.fromTimeAndParkingSpot(endTime, parkingSpot).print();
    }

    private ParkingSpot addCar(String car, Integer psId, LocalDateTime startTime) {
        ParkingSpot spot = repository.findById(psId).orElseThrow(ParkingLotNotFoundException::new);
        spot.occupy(car, startTime);

        return repository.save(spot);
    }

    private void removeCar(Integer psId) {
        ParkingSpot spot = repository.findById(psId).orElseThrow(CarNotFoundException::new);
        spot.free();

        repository.save(spot);
    }
}
