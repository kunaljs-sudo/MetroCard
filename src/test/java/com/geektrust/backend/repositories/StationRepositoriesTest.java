package com.geektrust.backend.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.geektrust.backend.entities.Station;
import com.geektrust.backend.entities.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testing for StationRepository")
public class StationRepositoriesTest {

    private StationRepository stationRepository;

    @BeforeEach
    void setup() {
        stationRepository = new StationRepository();
    }

    @Test
    @DisplayName("Checking Save methhod of StationRepository")
    public void saveTest() {
        List<UserType> usertypes = new ArrayList<>();
        usertypes.add(UserType.ADULT);
        usertypes.add(UserType.ADULT);
        usertypes.add(UserType.KID);
        usertypes.add(UserType.KID);
        usertypes.add(UserType.SENIOR_CITIZEN);

        Station station = new Station("CHENNAI", 600, usertypes, 0);
        Station actual = stationRepository.save(station);

        Assertions.assertEquals(station, actual);
    }


    @Test
    @DisplayName("Checking findByname method")
    public void findByNameTest() {
        Station station1 = new Station("CHENNAI");
        Station station2 = new Station("AIRPORT");
        Station station3 = new Station("LKO");
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);

        Optional<Station> actual = stationRepository.findByName("CHENNAI");

        Assertions.assertEquals(Optional.of(station1), actual);
    }

    


}


