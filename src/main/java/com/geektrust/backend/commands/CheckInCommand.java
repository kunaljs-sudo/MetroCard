package com.geektrust.backend.commands;

import java.util.List;
import com.geektrust.backend.entities.UserType;
import com.geektrust.backend.services.StationServices.IStationService;

public class CheckInCommand implements ICommand {

    private IStationService stationService;
    private static final int METRO_CARD_NAME_INDEX = 1;
    private static final int USER_TYPE_INDEX = 2;
    private static final int STATION_NAME_INDEX = 3;


    public CheckInCommand(IStationService stationService) {
        this.stationService = stationService;
    }

    @Override
    public void execute(List<String> tokens) {
        String metroCardName = tokens.get(METRO_CARD_NAME_INDEX);
        UserType userType;
        try {
            userType = UserType.valueOf(tokens.get(USER_TYPE_INDEX));
        } catch (IllegalArgumentException e) {
            // Handle the case where the string cannot be converted to a valid UserType
            throw new IllegalArgumentException("Invalid UserType: " + tokens.get(USER_TYPE_INDEX));
        }
        String stationName = tokens.get(STATION_NAME_INDEX);

        stationService.checkIn(List.of(metroCardName, String.valueOf(userType), stationName));
    }

}
