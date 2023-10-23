package org.carparkinglot;

import org.carparkinglot.exception.CarNotFoundException;
import org.carparkinglot.exception.CarParkingFullException;
import org.carparkinglot.model.ParkingSpot;
import org.carparkinglot.repository.IParkingSpotRepository;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class CarParkingManagerTest {
    @Test
    public void givenACarWhenParkingAndSpotAvailableThenCarShouldBeParked() {
        IParkingSpotRepository repository = new CarLotBuilder().withEmptySpot("ENTRY_LEVEL")
                .build();

        String carNumber = "CRPLT1";
        CarParkingManager carParkingManager = new CarParkingManager(repository);

        LocalDateTime expectedReservedAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        ParkingSpot spot = carParkingManager.parkCar(carNumber, "ENTRY_LEVEL", expectedReservedAt);

        Assert.assertEquals(Integer.valueOf(1), spot.getId());
        Assert.assertEquals("ENTRY_LEVEL", spot.getPriceLevel());
        Assert.assertEquals(expectedReservedAt, spot.getReservedAt());
        Assert.assertEquals(carNumber, spot.getCarNumber());
    }

    @Test
    public void givenAFullParkingLotWhenTryingToParkACarThenCarParkingFullExceptionIsThrown() {
        IParkingSpotRepository repository = new CarLotBuilder()
                .withOccupiedSpot("ENTRY_LEVEL", "CARPLT1", LocalDateTime.of(2023, 1, 1, 14, 0))
                .build();

        CarParkingManager parkingManager = new CarParkingManager(repository);

        String carNumber = "CARPLT2";
        LocalDateTime parkAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        Assert.assertThrows(
                CarParkingFullException.class,
                () -> parkingManager.parkCar(carNumber, "ENTRY_LEVEL", parkAt)
        );
    }

    @Test
    public void givenACarParkingLotWhenLeavingTheParkingLotAndCarInItThenParkingLotShouldNotContainCar() {
        String carNumber = "CARPLT2";
        LocalDateTime parkAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        LocalDateTime leaveAt = LocalDateTime.of(2023, 1, 1, 14, 14);
        String level = "ENTRY_LEVEL";

        IParkingSpotRepository repository = new CarLotBuilder()
                .withOccupiedSpot("ENTRY_LEVEL", "CARPLT1", parkAt)
                .withOccupiedSpot(level, carNumber, parkAt)
                .build();

        CarParkingManager parkingManager = new CarParkingManager(repository);

        parkingManager.endParking(carNumber, level, leaveAt);

        ParkingSpot emptySpot = repository.findById(2);
        Assert.assertNull(emptySpot.getReservedAt());
        Assert.assertNull(emptySpot.getCarNumber());
    }

    @Test
    public void givenACarParkingLotWhenLeavingTheParkingLotAndCarNotInItThenExceptionWillBeThrown() {
        LocalDateTime parkAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        LocalDateTime leaveAt = LocalDateTime.of(2023, 1, 1, 14, 14);

        IParkingSpotRepository repository = new CarLotBuilder()
                .withOccupiedSpot("ENTRY_LEVEL", "CARPLT1", parkAt)
                .build();

        CarParkingManager parkingManager = new CarParkingManager(repository);

        Assert.assertThrows(CarNotFoundException.class,
                () -> parkingManager.endParking("CRPLT2", "ENTRY_LEVEL", leaveAt)
        );
    }

    @Test
    public void givenACarGettingReadyToLeaveWhenPrintReceiptCalledAndEntryLevelExpectsCorrectReceipt() {
        LocalDateTime parkAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        LocalDateTime leaveAt = LocalDateTime.of(2023, 1, 1, 16, 0);

        IParkingSpotRepository repository = new CarLotBuilder()
                .withOccupiedSpot("ENTRY_LEVEL", "CARPLT1", parkAt)
                .build();

        CarParkingManager parkingManager = new CarParkingManager(repository);

        String expectedReceipt = String.format(
                "Car: %s\nPrice: %s\nPriceLevel: %s\nReservedAt: %s\nLeftAt: %s\n",
                "CARPLT1", "1.0", "ENTRY_LEVEL", parkAt, leaveAt
        );

        String resultedReceipt = parkingManager.printReceipt("CARPLT1", leaveAt);

        Assert.assertEquals(expectedReceipt, resultedReceipt);
    }

    @Test
    public void givenACarGettingReadyToLeaveWhenPrintReceiptCalledAndEntryLevelAndLessThanAnHourExpectsCorrectReceiptBilledWithOneHour() {
        LocalDateTime parkAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        LocalDateTime leaveAt = LocalDateTime.of(2023, 1, 1, 14, 5);

        IParkingSpotRepository repository = new CarLotBuilder()
                .withOccupiedSpot("ENTRY_LEVEL", "CARPLT1", parkAt)
                .build();

        CarParkingManager parkingManager = new CarParkingManager(repository);

        String expectedReceipt = String.format(
                "Car: %s\nPrice: %s\nPriceLevel: %s\nReservedAt: %s\nLeftAt: %s\n",
                "CARPLT1", "0.5", "ENTRY_LEVEL", parkAt, leaveAt
        );

        String resultedReceipt = parkingManager.printReceipt("CARPLT1", leaveAt);

        Assert.assertEquals(expectedReceipt, resultedReceipt);
    }

    @Test
    public void givenACarGettingReadyToLeaveWhenPrintReceiptCalledAndMidLevelExpectsCorrectReceipt() {
        LocalDateTime parkAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        LocalDateTime leaveAt = LocalDateTime.of(2023, 1, 1, 16, 0);

        IParkingSpotRepository repository = new CarLotBuilder()
                .withOccupiedSpot("MID_LEVEL", "CARPLT1", parkAt)
                .build();

        CarParkingManager parkingManager = new CarParkingManager(repository);

        String expectedReceipt = String.format(
                "Car: %s\nPrice: %s\nPriceLevel: %s\nReservedAt: %s\nLeftAt: %s\n",
                "CARPLT1", "3.0", "MID_LEVEL", parkAt, leaveAt
        );

        String resultedReceipt = parkingManager.printReceipt("CARPLT1", leaveAt);

        Assert.assertEquals(expectedReceipt, resultedReceipt);
    }

    @Test
    public void givenACarGettingReadyToLeaveWhenPrintReceiptCalledAndMidLevelAndLessThanAnHourExpectsCorrectReceiptBilledWithOneHour() {
        LocalDateTime parkAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        LocalDateTime leaveAt = LocalDateTime.of(2023, 1, 1, 14, 5);

        IParkingSpotRepository repository = new CarLotBuilder()
                .withOccupiedSpot("MID_LEVEL", "CARPLT1", parkAt)
                .build();

        CarParkingManager parkingManager = new CarParkingManager(repository);

        String expectedReceipt = String.format(
                "Car: %s\nPrice: %s\nPriceLevel: %s\nReservedAt: %s\nLeftAt: %s\n",
                "CARPLT1", "1.5", "MID_LEVEL", parkAt, leaveAt
        );

        String resultedReceipt = parkingManager.printReceipt("CARPLT1", leaveAt);

        Assert.assertEquals(expectedReceipt, resultedReceipt);
    }

    @Test
    public void givenACarGettingReadyToLeaveWhenPrintReceiptCalledAndPremiumLevelExpectsCorrectReceipt() {
        LocalDateTime parkAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        LocalDateTime leaveAt = LocalDateTime.of(2023, 1, 1, 16, 0);

        IParkingSpotRepository repository = new CarLotBuilder()
                .withOccupiedSpot("PREMIUM_LEVEL", "CARPLT1", parkAt)
                .build();

        CarParkingManager parkingManager = new CarParkingManager(repository);

        String expectedReceipt = String.format(
                "Car: %s\nPrice: %s\nPriceLevel: %s\nReservedAt: %s\nLeftAt: %s\n",
                "CARPLT1", "6.0", "PREMIUM_LEVEL", parkAt, leaveAt
        );

        String resultedReceipt = parkingManager.printReceipt("CARPLT1", leaveAt);

        Assert.assertEquals(expectedReceipt, resultedReceipt);
    }

    @Test
    public void givenACarGettingReadyToLeaveWhenPrintReceiptCalledAndPremiumLevelAndLessThanAnHourExpectsCorrectReceiptBilledWithOneHour() {
        LocalDateTime parkAt = LocalDateTime.of(2023, 1, 1, 14, 0);
        LocalDateTime leaveAt = LocalDateTime.of(2023, 1, 1, 14, 5);

        IParkingSpotRepository repository = new CarLotBuilder()
                .withOccupiedSpot("PREMIUM_LEVEL", "CARPLT1", parkAt)
                .build();

        CarParkingManager parkingManager = new CarParkingManager(repository);

        String expectedReceipt = String.format(
                "Car: %s\nPrice: %s\nPriceLevel: %s\nReservedAt: %s\nLeftAt: %s\n",
                "CARPLT1", "3.0", "PREMIUM_LEVEL", parkAt, leaveAt
        );

        String resultedReceipt = parkingManager.printReceipt("CARPLT1", leaveAt);

        Assert.assertEquals(expectedReceipt, resultedReceipt);
    }
}
