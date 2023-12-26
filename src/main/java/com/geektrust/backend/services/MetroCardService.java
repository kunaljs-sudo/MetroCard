package com.geektrust.backend.services;

import java.util.Optional;
import com.geektrust.backend.entities.MetroCard;
import com.geektrust.backend.exceptions.NoMetroCardFoundException;
import com.geektrust.backend.repositories.IMetroCardRepository;

public class MetroCardService implements IMetroCardService {

    private IMetroCardRepository metroCardRepository;

    public MetroCardService(IMetroCardRepository metroCardRepository) {
        this.metroCardRepository = metroCardRepository;
    }

    @Override
    public Integer payMoney(String metrocardId, Integer amount) {
        MetroCard metroCard = getMetroCard(metrocardId);
        Double toPay;

        boolean discountApplicable = evaluateDiscountEligibility(metrocardId);

        if (discountApplicable) {
            amount = amount / 2;
        }

        if (metroCard.getBalance() < amount) {
            Double amountDiff = (double) (amount - metroCard.getBalance());
            Double trxnChrg = 0.02 * amountDiff;
            toPay = amount + trxnChrg;
            metroCard.setBalance(0);
        } else {
            toPay = (double) amount;
            metroCard.setBalance(metroCard.getBalance() - amount);
        }
        // If we are paying means we are making a journey
        metroCard.incrementTotalJourney();
        saveToMetroCardRepository(metroCard);

        return (int) Math.ceil(toPay);
    }

    @Override
    public MetroCard create(String metrocardId, Integer balance) {
        MetroCard metroCard = new MetroCard(metrocardId, balance);
        return metroCardRepository.save(metroCard);
    }

    @Override
    public boolean evaluateDiscountEligibility(String metrocardId) {
        Optional<MetroCard> oMetroCard = metroCardRepository.findById(metrocardId);
        if (oMetroCard.isEmpty()) {
            throw new NoMetroCardFoundException(
                    "Invalid metroCardName: " + metrocardId + "checking for discount eligibilty");
        }

        MetroCard metroCard = oMetroCard.get();
        Integer totalJourney = metroCard.getTotalJourney();
        if (totalJourney != 0 && totalJourney % 2 == 1) {
            return true;
        }
        return false;
    }

    private MetroCard getMetroCard(String metrocardId) {
        Optional<MetroCard> oMetroCard = metroCardRepository.findById(metrocardId);
        if (oMetroCard.isEmpty()) {
            throw new NoMetroCardFoundException(
                    "While paying Card not found with cardId: " + metrocardId);
        }

        return oMetroCard.get();
    }

    private MetroCard saveToMetroCardRepository(MetroCard metroCard) {
        return metroCardRepository.save(metroCard);
    }

}
