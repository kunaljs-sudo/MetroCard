package com.geektrust.backend.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.geektrust.backend.entities.Station;

public class StationRepository implements IStationRepository {

    private Map<String, Station> stationMap;

    public StationRepository() {
        this.stationMap = new HashMap<>();
    }

    @Override
    public Station save(Station entity) {
        stationMap.put(entity.getStationName(), entity);
        return entity;
    }

    @Override
    public List<Station> findAll() {
        List<Station> allStations = new ArrayList<>(stationMap.values());
        return allStations;
    }

    @Override
    public Optional<Station> findById(String id) {
        return stationMap.values().stream().filter(s -> s.getStationName().equals(id)).findFirst();
    }

    @Override
    public Optional<Station> findByName(String stationName) {
        return stationMap.values().stream().filter(s -> s.getStationName().equals(stationName))
                .findFirst();
    }

    @Override
    public boolean existsById(String id) {
        return stationMap.containsKey(id);
    }

    @Override
    public void delete(Station entity) {
        if (entity.getStationName() != null) {
            stationMap.remove(entity.getStationName());
        }

    }

    @Override
    public void deleteById(String id) {
        stationMap.remove(id);
    }

    @Override
    public void deleteByStationName(String stationName) {
        stationMap.remove(stationName);
    }



    @Override
    public long count() {
        return stationMap.size();
    }


}
