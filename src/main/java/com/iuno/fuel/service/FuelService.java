package com.iuno.fuel.service;

import com.iuno.fuel.entity.FuelStation;
import com.iuno.fuel.repository.FuelRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;


@Service
public class FuelService {

    private final FuelRepository repository;
    private final FuelApiClient fuelApiClient;

    public FuelService(FuelRepository repository, FuelApiClient fuelApiClient) {
        this.repository = repository;
        this.fuelApiClient = fuelApiClient;
    }

    public void getFuelStationPrice() {
        var priceMap = fuelApiClient.fetchFullPrice();
        addFuelPriceToDatabase(priceMap, "Jet");
        addFuelPriceToDatabase(priceMap, "Avanti");
    }

    private void addFuelPriceToDatabase(Map<String, Double> priceMap, String key) {
        FuelStation fuelStation = new FuelStation();
        LocalDateTime localDateTime = LocalDateTime.now(Clock.systemUTC());
        if (priceMap.containsKey(key)) {
            double fuelPrice = priceMap.get(key);
            fuelStation.setPrice(fuelPrice);
            fuelStation.setName(key);
            fuelStation.setTime(localDateTime.toInstant(ZoneOffset.UTC));
            repository.save(fuelStation);
        }
    }
}
