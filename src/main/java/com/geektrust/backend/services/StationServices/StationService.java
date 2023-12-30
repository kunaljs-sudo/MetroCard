package com.geektrust.backend.services.StationServices;

import java.util.List;
import java.util.Optional;
import com.geektrust.backend.constants.MagicNumbers;
import com.geektrust.backend.entities.MetroCard;
import com.geektrust.backend.entities.Station;
import com.geektrust.backend.entities.UserType;
import com.geektrust.backend.exceptions.NoMetroCardFoundException;
import com.geektrust.backend.exceptions.UserTypeNotFoundException;
import com.geektrust.backend.repositories.IMetroCardRepository;
import com.geektrust.backend.repositories.IStationRepository;
import com.geektrust.backend.services.IMetroCardService;

public class StationService implements IStationService {

    private final IMetroCardRepository metroCardRepository;
    private final IStationRepository stationRepository;
    private final IMetroCardService metroCardService;

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
        String metroCardId = args.get(MagicNumbers.ZERO);
        UserType userType = getUserType(args);
        String stationName = args.get(MagicNumbers.TWO);

        // Retrieve MetroCard and Station objects
        MetroCard metroCard = getMetroCardByID(metroCardId);
        Station station = getStationByName(stationName);

        // Calculate ticket amount and check for discount eligibility
        Integer ticketAmount = getTicketPrice(userType);
        boolean discountApplicable = metroCardService.evaluateDiscountEligibility(metroCard);
        Integer discountGivenOnTicket = discountApplicable ? ticketAmount / MagicNumbers.TWO : MagicNumbers.ZERO;

        // Process payment and update station information
        Integer moneyCollectedOnTicket = metroCardService.payMoney(metroCard.getId(), ticketAmount);
        station.addToCollection(moneyCollectedOnTicket);
        station.addTotal_discount_given(discountGivenOnTicket);
        station.addUserType(userType);

        // Save updated station information to the repository
        saveToStationRepository(station);
    }

    // Helper method to convert string to UserType with error handling
    private final UserType getUserType(List<String> args) {
        try {
            return UserType.valueOf(args.get(MagicNumbers.ONE));
        } catch (IllegalArgumentException e) {
            // Handle the case where the string cannot be converted to a valid UserType
            throw new IllegalArgumentException("Invalid UserType: " + args.get(MagicNumbers.ONE));
        }
    }

    private final Station saveToStationRepository(Station station) {
        return stationRepository.save(station);
    }

    private final MetroCard getMetroCardByID(String metroCardId) {
        Optional<MetroCard> oMetroCard = metroCardRepository.findById(metroCardId);
        if (oMetroCard.isEmpty()) {
            throw new NoMetroCardFoundException(
                    "To checkIn the given metroCard is invalid: " + metroCardId);
        }
        MetroCard metroCard = oMetroCard.get();
        return metroCard;
    }

    private final Station getStationByName(String stationName) {
        Optional<Station> oStation = stationRepository.findByName(stationName);
        Station station;
        if (oStation.isEmpty()) {
            station = create(stationName);
        } else {
            station = oStation.get();
        }
        return station;
    }

    private final Integer getTicketPrice(UserType userType) {
        switch (userType) {
            case ADULT:
                return userType.getVal();
            case SENIOR_CITIZEN:
                return userType.getVal();
            case KID:
                return userType.getVal();
            default:
                throw new UserTypeNotFoundException("Invalid UserType during checkIn: " + userType);
        }
    }



}
