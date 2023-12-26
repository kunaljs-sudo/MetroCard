package com.geektrust.backend.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.geektrust.backend.entities.MetroCard;
import com.geektrust.backend.entities.Station;
import com.geektrust.backend.entities.UserType;
import com.geektrust.backend.exceptions.StationNotFoundException;
import com.geektrust.backend.exceptions.UserTypeNotFoundException;
import com.geektrust.backend.repositories.IMetroCardRepository;
import com.geektrust.backend.repositories.IStationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("StationServiceTest")
@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

    @Mock
    private IMetroCardRepository metroCardRepository;

    @Mock
    private IStationRepository stationRepository;

    @Mock
    private IMetroCardService metroCardService;

    @InjectMocks
    private StationService stationService;

    @Test
    @DisplayName("createStationTest")
    public void createStationTest() {
        Station station = new Station("CHENNAI");

        when(stationRepository.save(any(Station.class))).thenReturn(station);

        Station actual = stationService.create("CHENNAI");

        Assertions.assertEquals(station, actual);
        verify(stationRepository, times(1)).save(any(Station.class));
    }

    @Test
    @DisplayName("checkInTest")
    public void checkInTest() {
        MetroCard metroCard = new MetroCard("MC1", 1000, 3);
        List<UserType> usertypes = new ArrayList<>();
        usertypes.add(UserType.ADULT);
        usertypes.add(UserType.ADULT);
        usertypes.add(UserType.KID);
        usertypes.add(UserType.KID);
        usertypes.add(UserType.SENIOR_CITIZEN);

        Station station = new Station("CHENNAI", 600, usertypes, 0);
        when(metroCardRepository.findById(anyString())).thenReturn(Optional.of(metroCard));
        when(stationRepository.findByName(anyString())).thenReturn(Optional.of(station));
        when(stationRepository.save(any(Station.class))).thenReturn(station);
        

        stationService.checkIn(List.of("MC1", String.valueOf(UserType.ADULT), "CHENNAI"));

        verify(metroCardRepository, times(1)).findById(anyString());
        verify(stationRepository, times(1)).findByName(anyString());
        verify(stationRepository, times(0)).count();

    }

    @Test
    @DisplayName("checkInTest Throws Error")
    public void checkInTestThrowsError() {
        MetroCard metroCard = new MetroCard("MC1", 1000, 3);
        List<UserType> usertypes = new ArrayList<>();
        usertypes.add(UserType.ADULT);
        usertypes.add(UserType.ADULT);
        usertypes.add(UserType.KID);
        usertypes.add(UserType.KID);
        usertypes.add(UserType.SENIOR_CITIZEN);

        Station station = new Station("CHENNAI", 600, usertypes, 0);
        when(metroCardRepository.findById(anyString())).thenReturn(Optional.of(metroCard));
        when(stationRepository.findByName(anyString())).thenReturn(Optional.of(station));



        Assertions.assertThrows(UserTypeNotFoundException.class, () -> stationService
                .checkIn(List.of("MC1", String.valueOf(UserType.TEST), "CHENNAI")));

        verify(metroCardRepository, times(1)).findById(anyString());
        verify(metroCardRepository, times(0)).save(any(MetroCard.class));
        verify(stationRepository, times(1)).findByName(anyString());
        verify(stationRepository, times(0)).count();

    }

    @Test
    @DisplayName("getSummaryTest Throws Error")
    public void getSummaryTestThrowsError() {

        when(stationRepository.count()).thenReturn((long) 0);

        Assertions.assertThrows(StationNotFoundException.class, () -> stationService.getSummary());

        verify(stationRepository, times(1)).count();


    }



}
