package com.geektrust.backend.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.Optional;
import com.geektrust.backend.entities.MetroCard;
import com.geektrust.backend.exceptions.NoMetroCardFoundException;
import com.geektrust.backend.repositories.IMetroCardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Testing for MetroCardService")
@ExtendWith(MockitoExtension.class)
public class MetroCardServiceTest {

    @Mock
    private IMetroCardRepository metroCardRepository;

    @InjectMocks
    private MetroCardService metroCardService;

    // @BeforeEach
    // void setUp() {
    // MockitoAnnotations.openMocks(this);
    // }

    @Test
    @DisplayName("Testing paymoney method")
    public void payMoneyTest() {
        MetroCard metroCard = new MetroCard("MC1", 500, 3);
        // MetroCard metroCard1 = new MetroCard("MC2", 100, 1);
        // MetroCard metroCard2 = new MetroCard("MC3", 50, 4);
        // metroCardRepository.save(metroCard1);
        // metroCardRepository.save(metroCard2);
        // metroCardRepository.save(metroCard);

        when(metroCardRepository.findById(anyString())).thenReturn(Optional.of(metroCard));

        Integer actual = metroCardService.payMoney("MC1", 200);
        Assertions.assertEquals(100, actual);
    }

    @Test
    @DisplayName("Testing paymoney method to throw error")
    public void payMoneyThrowErrorTest() {

        when(metroCardRepository.findById(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoMetroCardFoundException.class,
                () -> metroCardService.payMoney("MC1", 200));
    }

    @Test
    @DisplayName("Testing createMetroCard")
    public void createMetroCardTest() {
        MetroCard expected = new MetroCard("MC1", 1000);
        when(metroCardRepository.save(any(MetroCard.class))).thenReturn(expected);
        MetroCard actual = metroCardService.create("MC1", 1000);

        Assertions.assertEquals(expected, actual);
    }

    



}
