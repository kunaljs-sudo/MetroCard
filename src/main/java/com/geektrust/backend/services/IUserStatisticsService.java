package com.geektrust.backend.services;

import java.util.List;
import com.geektrust.backend.entities.UserType;

public interface IUserStatisticsService {
    public String getUserStatistics(List<UserType> userTypes);
}
