package com.geektrust.backend.commands;

import java.util.List;
import com.geektrust.backend.entities.UserType;
import com.geektrust.backend.services.IStationService;

public class CheckInCommand implements ICommand {

    private IStationService stationService;

    public CheckInCommand(IStationService stationService) {
        this.stationService = stationService;
    }

    @Override
    public void execute(List<String> tokens) {
        String metroCardName = tokens.get(1);
        UserType userType = UserType.valueOf(tokens.get(2));
        String stationName = tokens.get(3);

        stationService.checkIn(metroCardName, userType, stationName);
    }

}
