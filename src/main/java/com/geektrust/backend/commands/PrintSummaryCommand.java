package com.geektrust.backend.commands;

import java.util.List;
import com.geektrust.backend.services.IStationService;

public class PrintSummaryCommand implements ICommand {

    private IStationService stationService;

    public PrintSummaryCommand(IStationService stationService) {
        this.stationService = stationService;
    }


    @Override
    public void execute(List<String> tokens) {
        String res = stationService.getSummary();
        System.out.println(res);
    }

}
