package com.geektrust.backend.services;

import com.geektrust.backend.entities.MetroCard;

public interface IMetroCardService {
    public Integer payMoney(String metrocardId, Integer amount);

    public MetroCard create(String metrocardId, Integer balance);

    public boolean evaluateDiscountEligibility(MetroCard metroCard);
}
