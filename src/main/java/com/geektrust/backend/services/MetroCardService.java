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
        boolean discountApplicable = evaluateDiscountEligibility(metroCard);
        Integer toPay;
        if (discountApplicable) {
            amount = amount / 2;
        }

        Double trxnChrgPercentage = 0.02;

        if (metroCard.getBalance() < amount) {
            Double amountDiff = (double) (amount - metroCard.getBalance());
            Double trxnChrg = trxnChrgPercentage * amountDiff;
            toPay = amount + (int) Math.ceil(trxnChrg);
            metroCard.setBalance(0);
        } else {
            toPay = amount;
            metroCard.setBalance(metroCard.getBalance() - amount);
        }
        // If we are paying means we are making a journey
        metroCard.incrementTotalJourney();
        saveToMetroCardRepository(metroCard);

        return toPay;
    }

    @Override
    public MetroCard create(String metrocardId, Integer balance) {
        MetroCard metroCard = new MetroCard(metrocardId, balance);
        return metroCardRepository.save(metroCard);
    }

    @Override
    public boolean evaluateDiscountEligibility(MetroCard metroCard) {

        Integer totalJourney = metroCard.getTotalJourney();
        // odd
        if ((totalJourney.intValue() % 2) == 1) {
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
