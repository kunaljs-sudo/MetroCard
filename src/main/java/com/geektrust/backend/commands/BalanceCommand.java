package com.geektrust.backend.commands;

import java.util.List;
import com.geektrust.backend.services.IMetroCardService;

public class BalanceCommand implements ICommand {

    private IMetroCardService metroCardService;
    private static final int METRO_CARD_NAME_INDEX = 1;
    private static final int BALANCE_INDEX = 2;

    public BalanceCommand(IMetroCardService metroCardService) {
        this.metroCardService = metroCardService;
    }


    @Override
    public void execute(List<String> tokens) {
        String metroCardName = tokens.get(METRO_CARD_NAME_INDEX);
        String balance = tokens.get(BALANCE_INDEX);
        metroCardService.create(metroCardName, Integer.valueOf(balance));
    }

}
