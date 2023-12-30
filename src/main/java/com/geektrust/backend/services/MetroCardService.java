package com.geektrust.backend.services;

import java.util.Optional;
import com.geektrust.backend.constants.MagicNumbers;
import com.geektrust.backend.entities.MetroCard;
import com.geektrust.backend.exceptions.NoMetroCardFoundException;
import com.geektrust.backend.repositories.IMetroCardRepository;

public class MetroCardService implements IMetroCardService {

    private final IMetroCardRepository metroCardRepository;
    private Double trxnChrgPercentage = MagicNumbers.PERCENTAGE;

    public MetroCardService(IMetroCardRepository metroCardRepository) {
        this.metroCardRepository = metroCardRepository;
    }

    @Override
    public Integer payMoney(String metrocardId, Integer amount) {
        MetroCard metroCard = getMetroCard(metrocardId);
        boolean discountApplicable = evaluateDiscountEligibility(metroCard);

        amount = applyDiscount(amount, discountApplicable);
        amount = handleTransactionCharges(metroCard, amount);

        // If we are paying means we are making a journey
        metroCard.incrementTotalJourney();
        saveToMetroCardRepository(metroCard);
        return amount;
    }

    private Integer applyDiscount(Integer amount, boolean discountApplicable) {
        if (discountApplicable) {
            amount /= MagicNumbers.TWO;
        }
        return amount;
    }

    private Integer handleTransactionCharges(MetroCard metroCard, Integer amount) {

        if (metroCard.getBalance() < amount) {
            Double amountDiff = (double) (amount - metroCard.getBalance());
            Double trxnChrg = trxnChrgPercentage * amountDiff;
            amount += (int) Math.ceil(trxnChrg);
            metroCard.setBalance(MagicNumbers.ZERO);
        } else {
            metroCard.setBalance(metroCard.getBalance() - amount);
        }
        return amount;
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
        if ((totalJourney.intValue() % MagicNumbers.TWO) == MagicNumbers.ONE) {
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

    public Double getTrxnChrgPercentage() {
        return trxnChrgPercentage;
    }

    public void setTrxnChrgPercentage(Double trxnChrgPercentage) {
        this.trxnChrgPercentage = trxnChrgPercentage;
    }

}
