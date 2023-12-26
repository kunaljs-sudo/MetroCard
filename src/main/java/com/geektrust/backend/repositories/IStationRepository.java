package com.geektrust.backend.repositories;

import java.util.Optional;
import com.geektrust.backend.entities.Station;

public interface IStationRepository extends CRUDRepository<Station, String> {
    public Optional<Station> findByName(String stationName);

    public void deleteByStationName(String stationName);

}
