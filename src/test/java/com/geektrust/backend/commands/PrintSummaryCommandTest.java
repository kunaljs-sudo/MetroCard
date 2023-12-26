package com.geektrust.backend.commands;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import com.geektrust.backend.services.IStationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    @DisplayName("executeTest")
    void executeTest() {
        // Arrange
        String expected = "TOTAL_COLLECTION CENTRAL 300 0\n" + "PASSENGER_TYPE_SUMMARY\n"
                + "ADULT 1\n" + "SENIOR_CITIZEN 1\n" + "TOTAL_COLLECTION AIRPORT 403 100\n"
                + "PASSENGER_TYPE_SUMMARY\n" + "ADULT 2\n" + "KID 2";

        when(stationService.getSummary()).thenReturn(expected);

        // Act
        printSummaryCommand.execute(List.of("PRINT_SUMMARY"));

        // Assert
        Assertions.assertEquals(expected, outputStreamCaptor.toString().trim());
        verify(stationService, times(1)).getSummary();

    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

}
