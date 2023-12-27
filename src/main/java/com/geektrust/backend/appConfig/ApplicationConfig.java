package com.geektrust.backend.appConfig;

import com.geektrust.backend.commands.BalanceCommand;
import com.geektrust.backend.commands.CheckInCommand;
import com.geektrust.backend.commands.CommandInvoker;
import com.geektrust.backend.commands.PrintSummaryCommand;
import com.geektrust.backend.repositories.IMetroCardRepository;
import com.geektrust.backend.repositories.IStationRepository;
import com.geektrust.backend.repositories.MetroCardRepository;
import com.geektrust.backend.repositories.StationRepository;
import com.geektrust.backend.services.IMetroCardService;
import com.geektrust.backend.services.IUserStatisticsService;
import com.geektrust.backend.services.MetroCardService;
import com.geektrust.backend.services.UserStatisticsService;
import com.geektrust.backend.services.StationServices.GetStationSummaryService;
import com.geektrust.backend.services.StationServices.IGetStationSummaryService;
import com.geektrust.backend.services.StationServices.IStationService;
import com.geektrust.backend.services.StationServices.StationService;

public class ApplicationConfig {

    private final IMetroCardRepository metroCardRepository = new MetroCardRepository();
    private final IStationRepository stationRepository = new StationRepository();

    private final IMetroCardService metroCardService = new MetroCardService(metroCardRepository);
    private final IUserStatisticsService userStatisticsService = new UserStatisticsService();
    private final IStationService stationService =
            new StationService(metroCardRepository, stationRepository, metroCardService);
    private final IGetStationSummaryService getStationSummaryService =
            new GetStationSummaryService(stationRepository, userStatisticsService, stationService);

    private final BalanceCommand balanceCommand = new BalanceCommand(metroCardService);
    private final CheckInCommand checkInCommand = new CheckInCommand(stationService);
    private final PrintSummaryCommand printSummaryCommand =
            new PrintSummaryCommand(getStationSummaryService);

    private final CommandInvoker commandInvoker = new CommandInvoker();

    public CommandInvoker getCommandInvoker() {
        commandInvoker.register("BALANCE", balanceCommand);
        commandInvoker.register("CHECK_IN", checkInCommand);
        commandInvoker.register("PRINT_SUMMARY", printSummaryCommand);

        return commandInvoker;
    }

}
