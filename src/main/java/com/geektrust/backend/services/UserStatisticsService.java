package com.geektrust.backend.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.geektrust.backend.entities.UserType;

public class UserStatisticsService implements IUserStatisticsService {

    @Override
    public String getUserStatistics(List<UserType> userTypes) {
        Map<UserType, Long> frequencyMap = getFrequencyMap(userTypes);

        Comparator<UserType> comparator = getCustomComparator(frequencyMap);
        List<UserType> requiredUserType = getRequiredUserTypes();
        Collections.sort(requiredUserType, comparator);

        return requiredUserType.stream().filter(userType -> frequencyMap.containsKey(userType))
                .map(userType -> userType + " " + frequencyMap.getOrDefault(userType, 0L))
                .collect(Collectors.joining("\n"));


    }

    private Map<UserType, Long> getFrequencyMap(List<UserType> userTypes) {
        return userTypes.stream()
                .collect(Collectors.groupingBy(userType -> userType, Collectors.counting()));
    }

    private List<UserType> getRequiredUserTypes() {
        List<UserType> requiredUserType = new ArrayList<>();
        requiredUserType.add(UserType.ADULT);
        requiredUserType.add(UserType.KID);
        requiredUserType.add(UserType.SENIOR_CITIZEN);
        return requiredUserType;
    }

    private Comparator<UserType> getCustomComparator(Map<UserType, Long> frequencyMap) {
        return Comparator
                .<UserType, Long>comparing(userType -> frequencyMap.getOrDefault(userType, 0L),
                        Comparator.reverseOrder())
                .thenComparing(UserType::toString);
    }

}
