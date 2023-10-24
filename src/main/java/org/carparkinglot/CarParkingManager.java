package org.carparkinglot;

import org.carparkinglot.exception.CarNotFoundException;
import org.carparkinglot.exception.CarParkingFullException;
import org.carparkinglot.exception.UnknownPriceLevelException;
import org.carparkinglot.model.ParkingSpot;
import org.carparkinglot.repository.IParkingSpotRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        List<ParkingSpot> parkingSpotList = repository.findAllByPriceLevel(priceLevel);

        LocalDateTime startTime = null;
        Integer parkingSpotId = null;
        for (ParkingSpot parkingSpot : parkingSpotList) {
            if (parkingSpot.getCarNumber() != null && parkingSpot.getCarNumber().equals(carNumber)) {
                parkingSpotId = parkingSpot.getId();
                startTime = parkingSpot.getReservedAt();
            }
        }

        if (parkingSpotId != null) {
            addOrRemoveCar(carNumber, parkingSpotId, startTime, true);
        } else {
            throw new CarNotFoundException();
        }
    }

    public String printReceipt(String car, LocalDateTime endTime) {
        ParkingSpot parkingSpot = repository.findByCar(car);
        if (parkingSpot != null) {
            Double price = null;
            if (parkingSpot.getPriceLevel().equals("ENTRY_LEVEL")) {
                long minutes = ChronoUnit.HOURS.between(parkingSpot.getReservedAt(), endTime);
                if (minutes < 1) {
                    minutes = 1;
                }

                price = 0.5 * minutes;
            } else if (parkingSpot.getPriceLevel().equals("MID_LEVEL")) {
                long minutes = ChronoUnit.HOURS.between(parkingSpot.getReservedAt(), endTime);
                if (minutes < 1) {
                    minutes = 1;
                }

                price = 1.5 * minutes;
            } else if (parkingSpot.getPriceLevel().equals("PREMIUM_LEVEL")) {
                long minutes = ChronoUnit.HOURS.between(parkingSpot.getReservedAt(), endTime);
                if (minutes < 1) {
                    minutes = 1;
                }

                price = 3.0 * minutes;
            }
            if (price == null) {
                throw new UnknownPriceLevelException();
            }

            return String.format("Car: %s\nPrice: %s\nPriceLevel: %s\nReservedAt: %s\nLeftAt: %s\n",
                    parkingSpot.getCarNumber(), price, parkingSpot.getPriceLevel(), parkingSpot.getReservedAt(), endTime);

        } else {
            throw new CarNotFoundException();
        }
    }

    private ParkingSpot addOrRemoveCar(String car, Integer psId, LocalDateTime startTime, boolean isRemove) {
        ParkingSpot spot = repository.findById(psId);

        if (spot == null) {
            return null;
        }

        if (isRemove) {
            spot.setCarNumber(null);
            spot.setReservedAt(null);

            return repository.save(spot);
        } else {
            spot.setCarNumber(car);
            spot.setReservedAt(startTime);

            return repository.save(spot);
        }
    }
}
