package com.geektrust.backend.entities;

public class MetroCard extends BaseEntity {

    private Integer balance = 0;
    private Integer totalJourney = 0;

    public MetroCard(String id) {
        this.id = id;
    }

    public MetroCard(String id, Integer balance) {
        this.id = id;
        this.balance = balance;
    }

    public MetroCard(Integer balance, Integer totalJourney) {
        this.balance = balance;
        this.totalJourney = totalJourney;
    }

    public MetroCard(String id, Integer balance, Integer totalJourney) {
        this(balance, totalJourney);
        this.id = id;
    }

    public MetroCard(MetroCard metroCard) {
        this(metroCard.getId(), metroCard.getBalance(), metroCard.getTotalJourney());
    }

    public void addBalance(Integer new_amount) {
        balance += new_amount;
    }

    public Integer setBalance(Integer amount) {
        this.balance = amount;
        return this.balance;
    }

    public Integer getBalance() {
        return balance;
    }

    public Integer getTotalJourney() {
        return totalJourney;
    }

    public Integer setTotalJourney(Integer totalJourney) {
        this.totalJourney = totalJourney;
        return this.totalJourney;
    }

    public void incrementTotalJourney() {
        this.totalJourney++;
    }

    @Override
    public String toString() {
        return "MetroCard [id=" + id + ", balance=" + balance + ", totalJourney=" + totalJourney
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        MetroCard other = (MetroCard) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }



}
