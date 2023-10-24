package org.carparkinglot;

import org.carparkinglot.exception.CarNotFoundException;
import org.carparkinglot.exception.CarParkingFullException;
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

        return addOrRemoveCar(carNumber, spot.getId(), reservedAt, false);
    }

    public void endParking(String carNumber, String priceLevel, LocalDateTime endTime) {
        ParkingSpot parkingSpot = repository.findByCar(carNumber).orElseThrow(CarNotFoundException::new);

        addOrRemoveCar(carNumber, parkingSpot.getId(), parkingSpot.getReservedAt(), true);
    }

    public String printReceipt(String car, LocalDateTime endTime) {
        ParkingSpot parkingSpot = repository.findByCar(car).orElseThrow(CarNotFoundException::new);

        return ParkingReceipt.fromTimeAndParkingSpot(endTime, parkingSpot).print();
    }

    private ParkingSpot addOrRemoveCar(String car, Integer psId, LocalDateTime startTime, boolean isRemove) {
        if (isRemove) {
            return removeCar(psId);
        }

        return addCar(car, psId, startTime);
    }

    private ParkingSpot addCar(String car, Integer psId, LocalDateTime startTime) {
        ParkingSpot spot = repository.findById(psId);

        if (spot == null) {
            return null;
        }

        spot.setCarNumber(car);
        spot.setReservedAt(startTime);

        return repository.save(spot);
    }

    private ParkingSpot removeCar(Integer psId) {
        ParkingSpot spot = repository.findById(psId);

        if (spot == null) {
            throw new CarNotFoundException();
        }

        spot.setCarNumber(null);
        spot.setReservedAt(null);

        return repository.save(spot);
    }
}
