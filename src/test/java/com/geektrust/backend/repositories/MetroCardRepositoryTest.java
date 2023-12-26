package com.geektrust.backend.repositories;

import java.util.Optional;
import com.geektrust.backend.entities.MetroCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testing for MetroCardRepository")
public class MetroCardRepositoryTest {


    private MetroCardRepository metroCardRepository;

    @BeforeEach
    void setup() {
        metroCardRepository = new MetroCardRepository();
    }


    @Test
    @DisplayName("Checking Save methhod of MetroCardRepository")
    public void saveTest() {
        MetroCard metroCard = new MetroCard("MC1", 500);
        MetroCard actual = metroCardRepository.save(metroCard);
        Assertions.assertEquals(metroCard, actual);
    }

    @Test
    @DisplayName("Checking findById method of MetroCardRepository TO Get ID whihc does not exist")
    public void findByIdNotPresentElementTest() {
        MetroCard metroCard = new MetroCard("MC1", 500);
        MetroCard metroCard1 = new MetroCard("MC2", 100);
        MetroCard metroCard2 = new MetroCard("MC3", 50);
        metroCardRepository.save(metroCard1);
        metroCardRepository.save(metroCard2);
        metroCardRepository.save(metroCard);

        Optional<MetroCard> oMetroCard = metroCardRepository.findById("MC4");

        Assertions.assertEquals(Optional.empty(), oMetroCard);
    }
}
