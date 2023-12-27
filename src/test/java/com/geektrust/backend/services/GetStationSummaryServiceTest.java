package com.geektrust.backend.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.geektrust.backend.exceptions.StationNotFoundException;
import com.geektrust.backend.repositories.IStationRepository;
import com.geektrust.backend.services.StationServices.GetStationSummaryService;
import com.geektrust.backend.services.StationServices.IStationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GetStationSummaryServiceTest")
@ExtendWith(MockitoExtension.class)
public class GetStationSummaryServiceTest {
    
    @Mock
    private IStationRepository stationRepository;

    @Mock
    private IUserStatisticsService userStatisticsService;

    @Mock
    private IStationService stationService;

    @InjectMocks
    private GetStationSummaryService getStationSummaryServiceTest;

    @Test
    @DisplayName("getSummaryTest Throws Error")
    public void getSummaryTestThrowsError() {

        when(stationRepository.count()).thenReturn((long) 0);

        Assertions.assertThrows(StationNotFoundException.class, () -> getStationSummaryServiceTest.getSummary());

        verify(stationRepository, times(1)).count();
    }
}
