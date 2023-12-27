package com.geektrust.backend.entities;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private final String stationName;
    private Integer totalCollection = 0;
    private List<UserType> userTypes;
    private Integer totalDiscountGiven = 0;

    public Station(String stationName) {
        this.stationName = stationName;
        this.userTypes = new ArrayList<>();
    }


    public Station(String stationName, Integer totalCollection, List<UserType> userTypes,
            Integer totalDiscountGiven) {
        this.stationName = stationName;
        this.totalCollection = totalCollection;
        this.userTypes = userTypes;
        this.totalDiscountGiven = totalDiscountGiven;
    }


    public Station(Station station) {
        this(station.stationName, station.totalCollection, station.userTypes,
                station.totalDiscountGiven);
    }

    public String getStationName() {
        return stationName;
    }

    public Integer getTotalCollection() {
        return totalCollection;
    }

    public void addToCollection(Integer collection) {
        this.totalCollection += collection;
    }

    public List<UserType> getUserTypes() {
        return userTypes;
    }

    public void addUserType(UserType userType) {
        this.userTypes.add(userType);
    }

    public Integer getTotalDiscountGiven() {
        return totalDiscountGiven;
    }

    public void setTotalDiscountGiven(Integer totalDiscountGiven) {
        this.totalDiscountGiven = totalDiscountGiven;
    }

    public void addTotal_discount_given(Integer discountAmount) {
        this.totalDiscountGiven += discountAmount;
    }



    @Override
    public String toString() {
        return "Station [stationName=" + stationName + ", total_collection=" + totalCollection
                + ", total_discount_given=" + totalDiscountGiven + ", userTypes="
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
            if (other.getStationName() != null)
                return false;
        } else if (!this.getStationName().equals(other.getStationName()))
            return false;
        return true;
    }



}
