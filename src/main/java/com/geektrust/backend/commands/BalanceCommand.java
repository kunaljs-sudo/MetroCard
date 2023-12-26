package com.geektrust.backend.commands;

import java.util.List;
import com.geektrust.backend.services.IMetroCardService;

public class BalanceCommand implements ICommand {

    private IMetroCardService metroCardService;

    public BalanceCommand(IMetroCardService metroCardService) {
        this.metroCardService = metroCardService;
    }


    @Override
    public void execute(List<String> tokens) {
        String metroCardName = tokens.get(1);
        String balance = tokens.get(2);
        metroCardService.create(metroCardName, Integer.valueOf(balance));
    }

}
