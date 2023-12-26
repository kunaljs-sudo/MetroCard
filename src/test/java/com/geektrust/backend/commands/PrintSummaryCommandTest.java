package com.geektrust.backend.commands;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.List;
import com.geektrust.backend.services.IStationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("PrintSummaryCommandTest")
@ExtendWith(MockitoExtension.class)
public class PrintSummaryCommandTest {

    @Mock
    private IStationService stationService;

    @InjectMocks
    private PrintSummaryCommand printSummaryCommand;



    @Test
    @DisplayName("executeTest")
    void executeTest() {
        doNothing().when(stationService).printSummary();
        // Act
        printSummaryCommand.execute(List.of("PRINT_SUMMARY"));
        verify(stationService, times(1)).printSummary();

    }

}
