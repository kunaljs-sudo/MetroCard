package com.geektrust.backend.entities;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private final String stationName;
    private Integer total_collection = 0;
    private List<UserType> userTypes;
    private Integer total_discount_given = 0;

    public Station(String stationName) {
        this.stationName = stationName;
        this.userTypes = new ArrayList<>();
    }


    public Station(String stationName, Integer total_collection, List<UserType> userTypes,
            Integer total_discount_given) {
        this.stationName = stationName;
        this.total_collection = total_collection;
        this.userTypes = userTypes;
        this.total_discount_given = total_discount_given;
    }


    public Station(Station station) {
        this(station.stationName, station.total_collection, station.userTypes,
                station.total_discount_given);
    }

    public String getStationName() {
        return stationName;
    }

    public Integer getTotal_collection() {
        return total_collection;
    }

    public void addToCollection(Integer collection) {
        this.total_collection += collection;
    }

    public List<UserType> getUserTypes() {
        return userTypes;
    }

    public void addUserType(UserType userType) {
        this.userTypes.add(userType);
    }

    public Integer getTotal_discount_given() {
        return total_discount_given;
    }

    public void setTotal_discount_given(Integer total_discount_given) {
        this.total_discount_given = total_discount_given;
    }

    public void addTotal_discount_given(Integer discount) {
        this.total_discount_given += discount;
    }



    @Override
    public String toString() {
        return "Station [stationName=" + stationName + ", total_collection=" + total_collection
                + ", total_discount_given=" + total_discount_given + ", userTypes="
                + userTypes.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStationName() == null) ? 0 : getStationName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Station other = (Station) obj;
        if (this.getStationName() == null) {
            if (other.getClass() != null)
                return false;
        } else if (!this.getStationName().equals(other.getStationName()))
            return false;
        return true;
    }



}
