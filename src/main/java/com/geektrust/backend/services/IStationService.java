package com.geektrust.backend.services;

import java.util.List;
import com.geektrust.backend.entities.Station;

public interface IStationService {
    public Station create(String stationName);

    public void checkIn(List<String> args);

    public String getSummary();
}
