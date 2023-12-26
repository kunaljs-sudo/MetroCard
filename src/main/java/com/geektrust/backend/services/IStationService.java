package com.geektrust.backend.services;

import com.geektrust.backend.entities.Station;
import com.geektrust.backend.entities.UserType;

public interface IStationService {
    public Station create(String stationName);

    public void checkIn(String metroCardId, UserType userType, String stationName);

    public void printSummary();
}
