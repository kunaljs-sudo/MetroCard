package com.geektrust.backend.commands;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.List;
import com.geektrust.backend.entities.UserType;
import com.geektrust.backend.services.IStationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("CheckInCommandTest")
@ExtendWith(MockitoExtension.class)
public class CheckInCommandTest {

    @Mock
    private IStationService stationService;

    @InjectMocks
    private CheckInCommand checkInCommand;

    @Test
    @DisplayName("executeTest")
    public void executeTest() {
        doNothing().when(stationService).checkIn(anyString(), any(UserType.class), anyString());
        checkInCommand.execute(List.of("CHECKIN", "MC1", "ADULT", "CENTRAL"));
        verify(stationService, times(1)).checkIn(anyString(), any(UserType.class), anyString());

    }
}
