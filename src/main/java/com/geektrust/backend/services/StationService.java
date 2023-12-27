package com.geektrust.backend.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.geektrust.backend.entities.MetroCard;
import com.geektrust.backend.entities.Station;
import com.geektrust.backend.entities.UserType;
import com.geektrust.backend.exceptions.NoMetroCardFoundException;
import com.geektrust.backend.exceptions.StationNotFoundException;
import com.geektrust.backend.exceptions.UserTypeNotFoundException;
import com.geektrust.backend.repositories.IMetroCardRepository;
import com.geektrust.backend.repositories.IStationRepository;

public class StationService implements IStationService {

    private IMetroCardRepository metroCardRepository;
    private IStationRepository stationRepository;
    private IMetroCardService metroCardService;
    private IUserStatisticsService userStatisticsService;

    public StationService(IMetroCardRepository metroCardRepository,
            IStationRepository stationRepository, IMetroCardService metroCardService,
            IUserStatisticsService userStatisticsService) {
        this.metroCardRepository = metroCardRepository;
        this.stationRepository = stationRepository;
        this.metroCardService = metroCardService;
        this.userStatisticsService = userStatisticsService;
    }

    @Override
    public Station create(String stationName) {
        Station station = new Station(stationName);
        stationRepository.save(station);
        return station;
    }

    @Override
    public void checkIn(List<String> args) {

        String metroCardId = args.get(0);
        UserType userType = UserType.valueOf(args.get(1));
        String stationName = args.get(2);

        MetroCard metroCard = getMetroCardByID(metroCardId);
        Station station = getStationByName(stationName);

        Integer ticketamount = getTicketPrice(userType);


        boolean discoundApplicable = metroCardService.evaluateDiscountEligibility(metroCard);
        Integer discountGivenOnTicket = discoundApplicable ? ticketamount / 2 : 0;
        Integer moneyCollectedOnTicket = metroCardService.payMoney(metroCard.getId(), ticketamount);

        station.addToCollection(moneyCollectedOnTicket);
        station.addTotal_discount_given(discountGivenOnTicket);
        station.addUserType(userType);

        saveToStationRepository(station);

    }

    private Station saveToStationRepository(Station station) {
        return stationRepository.save(station);
    }

    private MetroCard getMetroCardByID(String metroCardId) {
        Optional<MetroCard> oMetroCard = metroCardRepository.findById(metroCardId);
        if (oMetroCard.isEmpty()) {
            throw new NoMetroCardFoundException(
                    "To checkIn the given metroCard is invalid: " + metroCardId);
        }
        MetroCard metroCard = oMetroCard.get();
        return metroCard;
    }

    private Station getStationByName(String stationName) {
        Optional<Station> oStation = stationRepository.findByName(stationName);
        Station station;
        if (oStation.isEmpty()) {
            station = create(stationName);
        } else {
            station = oStation.get();
        }
        return station;
    }

    private Integer getTicketPrice(UserType userType) {
        if (userType == UserType.ADULT) {
            return 200;
        } else if (userType == UserType.SENIOR_CITIZEN) {
            return 100;
        } else if (userType == UserType.KID) {
            return 50;
        } else {
            throw new UserTypeNotFoundException(
                    "While checkIn userType provided is Invalid" + userType);
        }
    }



    private String detailsOfStation(Station station) {
        return String.format("TOTAL_COLLECTION %s %d %d%n",
            station.getStationName(),
            station.getTotalCollection(),
            station.getTotalDiscountGiven())
            + "PASSENGER_TYPE_SUMMARY\n"
            + userStatisticsService.getUserStatistics(station.getUserTypes());
    }

    @Override
        public String getSummary() {

            if (stationRepository.count() == 0) {
                throw new StationNotFoundException("While printing there were no stations to print Summary for");
            }
            
            return Arrays.asList("CENTRAL", "AIRPORT")
                    .stream()
                    .filter(stationRepository::existsById)
                    .map(this::getStationByName)
                    .map(this::detailsOfStation)
                    .collect(Collectors.joining("\n"));
        }

}
