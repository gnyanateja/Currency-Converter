package com.somanchi.exchange.currency.rate.data.model;

public class Currency {
    private final String country;
    private final double exchangeRate;

    public Currency(String country, double exchangeRate) {
        this.country = country;
        this.exchangeRate = exchangeRate;
    }

    public String getCountry() {
        return country;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}