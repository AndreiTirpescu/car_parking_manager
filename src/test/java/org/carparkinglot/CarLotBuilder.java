package org.carparkinglot;

import org.carparkinglot.model.ParkingSpot;
import org.carparkinglot.repository.IParkingSpotRepository;
import org.carparkinglot.repository.InMemoryParkingSpotRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class CarLotBuilder {
    private List<ParkingSpot> parkingSpotList = new ArrayList<>();

    public CarLotBuilder withEmptySpot(String priceLevel) {
        parkingSpotList.add(
                new ParkingSpot(parkingSpotList.size() + 1, priceLevel, null, null)
        );

        return this;
    }

    public CarLotBuilder withOccupiedSpot(String priceLevel, String carNumber, LocalDateTime reservedAt) {
        parkingSpotList.add(
                new ParkingSpot(parkingSpotList.size() + 1, priceLevel, carNumber, reservedAt)
        );

        return this;
    }

    public IParkingSpotRepository build() {
        return new InMemoryParkingSpotRepository(parkingSpotList);
    }
}
