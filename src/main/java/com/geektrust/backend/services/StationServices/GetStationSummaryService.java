package com.geektrust.backend.services.StationServices;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import com.geektrust.backend.entities.Station;
import com.geektrust.backend.exceptions.StationNotFoundException;
import com.geektrust.backend.repositories.IStationRepository;
import com.geektrust.backend.services.IUserStatisticsService;

public class GetStationSummaryService implements IGetStationSummaryService {

    private IStationRepository stationRepository;
    private IUserStatisticsService userStatisticsService;
    private IStationService stationService;

    public GetStationSummaryService(IStationRepository stationRepository,
            IUserStatisticsService userStatisticsService, IStationService stationService) {
        this.stationRepository = stationRepository;
        this.userStatisticsService = userStatisticsService;
        this.stationService = stationService;
    }

    private final String detailsOfStation(Station station) {
        return String.format("TOTAL_COLLECTION %s %d %d%n", station.getStationName(),
                station.getTotalCollection(), station.getTotalDiscountGiven())
                + "PASSENGER_TYPE_SUMMARY\n"
                + userStatisticsService.getUserStatistics(station.getUserTypes());
    }

    private final Station getStationByName(String stationName) {
        Optional<Station> oStation = stationRepository.findByName(stationName);
        Station station;
        if (oStation.isEmpty()) {
            station = stationService.create(stationName);
        } else {
            station = oStation.get();
        }
        return station;
    }

    @Override
    public String getSummary() {

        if (stationRepository.count() == 0) {
            throw new StationNotFoundException(
                    "While printing there were no stations to print Summary for");
        }

        return Arrays.asList("CENTRAL", "AIRPORT").stream().filter(stationRepository::existsById)
                .map(this::getStationByName).map(this::detailsOfStation)
                .collect(Collectors.joining("\n"));
    }
}
