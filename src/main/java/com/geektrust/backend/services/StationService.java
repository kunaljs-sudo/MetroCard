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

    private Map<UserType, Integer> getFrequencyMap(List<UserType> userTypes) {
        Map<UserType, Integer> frequencyMap = new HashMap<>();
        for (UserType user : userTypes) {
            frequencyMap.put(user, frequencyMap.getOrDefault(user, 0) + 1);
        }
        return frequencyMap;
    }

    private List<UserType> getRequiredUserTypes() {
        List<UserType> requiredUserType = new ArrayList<>();
        requiredUserType.add(UserType.ADULT);
        requiredUserType.add(UserType.KID);
        requiredUserType.add(UserType.SENIOR_CITIZEN);
        return requiredUserType;
    }

    private Comparator<UserType> getCustomComparator(Map<UserType, Integer> frequencyMap) {
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
        return comparator;
    }

    private final String getSortedUserTypes(Station station) {

        List<UserType> userTypes = station.getUserTypes();
        Map<UserType, Integer> frequencyMap = getFrequencyMap(userTypes);

        Comparator<UserType> comparator = getCustomComparator(frequencyMap);
        List<UserType> requiredUserType = getRequiredUserTypes();
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

    private final String detailsOfStation(Station station) {
        String res = "";
        res += "TOTAL_COLLECTION " + station.getStationName() + " " + station.getTotal_collection()
                + " " + station.getTotal_discount_given() + "\n";
        res += "PASSENGER_TYPE_SUMMARY\n";
        String sortedStationUsers = getSortedUserTypes(station);
        res += sortedStationUsers;

        return res;
    }

    @Override
    public String getSummary() {

        if (stationRepository.count() == 0) {
            throw new StationNotFoundException(
                    "While printing there were no station to print Summary for");
        }
        String res = "";
        if (stationRepository.existsById("CENTRAL")) {
            res += detailsOfStation(getStationByName("CENTRAL"));
        }
        if (stationRepository.existsById("AIRPORT")) {
            res += detailsOfStation(getStationByName("AIRPORT"));
        }

        return res;
    }

}
