package com.geektrust.backend.services.StationServices;

import java.util.List;
import java.util.Optional;
import com.geektrust.backend.entities.MetroCard;
import com.geektrust.backend.entities.Station;
import com.geektrust.backend.entities.UserType;
import com.geektrust.backend.exceptions.NoMetroCardFoundException;
import com.geektrust.backend.exceptions.UserTypeNotFoundException;
import com.geektrust.backend.repositories.IMetroCardRepository;
import com.geektrust.backend.repositories.IStationRepository;
import com.geektrust.backend.services.IMetroCardService;

public class StationService implements IStationService {

    private IMetroCardRepository metroCardRepository;
    private IStationRepository stationRepository;
    private IMetroCardService metroCardService;

    private static final int METRO_CARD_NAME_INDEX = 0;
    private static final int USER_TYPE_INDEX = 1;
    private static final int STATION_NAME_INDEX = 2;

    public StationService(IMetroCardRepository metroCardRepository,
            IStationRepository stationRepository, IMetroCardService metroCardService) {
        this.metroCardRepository = metroCardRepository;
        this.stationRepository = stationRepository;
        this.metroCardService = metroCardService;
    }

    @Override
    public Station create(String stationName) {
        Station station = new Station(stationName);
        stationRepository.save(station);
        return station;
    }

    @Override
    public void checkIn(List<String> args) {
        // Extract information from the input arguments
        String metroCardId = args.get(METRO_CARD_NAME_INDEX);
        UserType userType = getUserType(args);
        String stationName = args.get(STATION_NAME_INDEX);

        // Retrieve MetroCard and Station objects
        MetroCard metroCard = getMetroCardByID(metroCardId);
        Station station = getStationByName(stationName);

        // Calculate ticket amount and check for discount eligibility
        Integer ticketAmount = getTicketPrice(userType);
        boolean discountApplicable = metroCardService.evaluateDiscountEligibility(metroCard);
        Integer discountGivenOnTicket = discountApplicable ? ticketAmount / 2 : 0;

        // Process payment and update station information
        Integer moneyCollectedOnTicket = metroCardService.payMoney(metroCard.getId(), ticketAmount);
        station.addToCollection(moneyCollectedOnTicket);
        station.addTotal_discount_given(discountGivenOnTicket);
        station.addUserType(userType);

        // Save updated station information to the repository
        saveToStationRepository(station);
    }

    // Helper method to convert string to UserType with error handling
    private UserType getUserType(List<String> args) {
        try {
            return UserType.valueOf(args.get(USER_TYPE_INDEX));
        } catch (IllegalArgumentException e) {
            // Handle the case where the string cannot be converted to a valid UserType
            throw new IllegalArgumentException("Invalid UserType: " + args.get(USER_TYPE_INDEX));
        }
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
        switch (userType) {
            case ADULT:
                return 200;
            case SENIOR_CITIZEN:
                return 100;
            case KID:
                return 50;
            default:
                throw new UserTypeNotFoundException("Invalid UserType during checkIn: " + userType);
        }
    }



}
