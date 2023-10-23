# Car Parking Manager
This is a very simple refactoring exercise/kata meant to showcase a step by step approach to refactoring in Java

# Business rules
* A car can be parked whenever a parking spot is available
* A parking spot is considered available when there is no car on it
* There are multiple price ranges based on some levels (ENTRY_LEVEL, MID_LEVEL, PREMIUM_LEVEL), these price ranges affect the overall price
* A receipt should be printed before leaving, the price on the receipt should be computed 
based on the price range above
* If a car stays less than an hour then the price will be computed for 1h


# Rules for this kata

* You must not start the refactoring process until you have a good code coverage of `CarParkingManager` class
* Try avoiding changing the public interface of `CarParkingManager` in the beginning 

