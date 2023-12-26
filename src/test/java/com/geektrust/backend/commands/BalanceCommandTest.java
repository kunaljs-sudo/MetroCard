package com.geektrust.backend.commands;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import com.geektrust.backend.entities.MetroCard;
import com.geektrust.backend.services.IMetroCardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("BalanceCommandTest")
@ExtendWith(MockitoExtension.class)
public class BalanceCommandTest {

    @Mock
    private IMetroCardService metroCardService;

    @InjectMocks
    private BalanceCommand balanceCommand;


    @Test
    @DisplayName("executeTest")
    public void executeTest() {
        when(metroCardService.create(anyString(), anyInt())).thenReturn(new MetroCard("", 0));
        balanceCommand.execute(List.of("BALANCE", "MC1", "1000"));
        verify(metroCardService, times(1)).create(anyString(), anyInt());

    }


}
