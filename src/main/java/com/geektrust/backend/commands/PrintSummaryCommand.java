package com.geektrust.backend.commands;

import java.util.List;
import com.geektrust.backend.services.StationServices.IGetStationSummaryService;

public class PrintSummaryCommand implements ICommand {

    private IGetStationSummaryService stationSummaryService;

    public PrintSummaryCommand(IGetStationSummaryService stationSummaryService) {
        this.stationSummaryService = stationSummaryService;
    }


    @Override
    public void execute(List<String> tokens) {
        String res = stationSummaryService.getSummary();
        System.out.println(res);
    }

}
