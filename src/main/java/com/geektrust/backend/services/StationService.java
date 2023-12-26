package com.geektrust.backend.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public void checkIn(String metroCardId, UserType userType, String stationName) {
        Optional<MetroCard> oMetroCard = metroCardRepository.findById(metroCardId);
        if (oMetroCard.isEmpty()) {
            throw new NoMetroCardFoundException(
                    "To checkIn the given metroCard is invalid: " + metroCardId);
        }
        MetroCard metroCard = oMetroCard.get();
        Optional<Station> oStation = stationRepository.findByName(stationName);
        Station station;
        if (oStation.isEmpty()) {
            station = create(stationName);
        } else {
            station = oStation.get();
        }

        Integer ticketamount = 0;

        if (userType == UserType.ADULT) {
            ticketamount = 200;
        } else if (userType == UserType.SENIOR_CITIZEN) {
            ticketamount = 100;
        } else if (userType == UserType.KID) {
            ticketamount = 50;
        } else {
            throw new UserTypeNotFoundException(
                    "While checkIn userType provided is Invalid" + userType);
        }
        boolean discoundApplicable =
                metroCardService.evaluateDiscountEligibility(metroCard.getId());
        Integer discountGivenOnTicket = discoundApplicable ? ticketamount / 2 : 0;
        Integer moneyCollectedOnTicket = metroCardService.payMoney(metroCard.getId(), ticketamount);

        station.addToCollection(moneyCollectedOnTicket);
        station.addTotal_discount_given(discountGivenOnTicket);
        station.addUserType(userType);
        stationRepository.save(station);

        metroCard.incrementTotalJourney();
        metroCardRepository.save(metroCard);

    }

    private String getSortedUserTypes(Station station) {
        List<UserType> userTypes = station.getUserTypes();
        Map<UserType, Integer> frequencyMap = new HashMap<>();
        for (UserType user : userTypes) {
            frequencyMap.put(user, frequencyMap.getOrDefault(user, 0) + 1);
        }
        List<UserType> requiredUserType = new ArrayList<>();
        requiredUserType.add(UserType.ADULT);
        requiredUserType.add(UserType.KID);
        requiredUserType.add(UserType.SENIOR_CITIZEN);
        Comparator<UserType> comparator = new Comparator<UserType>() {
            public int compare(UserType ut1, UserType ut2) {
                int freqComp = Integer.compare(frequencyMap.getOrDefault(ut2, 0),
                        frequencyMap.getOrDefault(ut1, 0));
                if (freqComp != 0) {
                    return freqComp;
                }
                return String.valueOf(ut1).compareTo(String.valueOf(ut2));
            }
        };

        Collections.sort(requiredUserType, comparator);
        String res = "";
        for (UserType userType : requiredUserType) {
            if (frequencyMap.containsKey(userType)) {
                res += (userType + " " + frequencyMap.getOrDefault(userType, 0));
                res += "\n";
            }
        }
        return res;
    }

    private String detailsOfStation(Station station) {
        String res = "";
        res += "TOTAL_COLLECTION " + station.getStationName() + " " + station.getTotal_collection()
                + " " + station.getTotal_discount_given() + "\n";
        res += "PASSENGER_TYPE_SUMMARY\n";
        String sortedStationUsers = getSortedUserTypes(station);
        res += sortedStationUsers;

        return res;
    }

    @Override
    public void printSummary() {

        if (stationRepository.count() == 0) {
            throw new StationNotFoundException(
                    "While printing there were no station to print Summary for");
        }
        String res = "";
        if (stationRepository.existsById("CENTRAL")) {
            res += detailsOfStation(stationRepository.findByName("CENTRAL").get());
        }
        if (stationRepository.existsById("AIRPORT")) {
            res += detailsOfStation(stationRepository.findByName("AIRPORT").get());
        }

        System.out.println(res);


    }

}
