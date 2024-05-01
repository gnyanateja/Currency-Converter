package com.somanchi.exchange.currency.rate.data.repository;


import com.google.gson.JsonObject;
import com.somanchi.exchange.currency.rate.BuildConfig;
import com.somanchi.exchange.currency.rate.network.RetrofitClient;
import retrofit2.Call;

public class CurrencyRepository {
    private RetrofitClient retrofitClient;

    public CurrencyRepository() {
        this.retrofitClient = RetrofitClient.getInstance();
    }

    public Call<JsonObject> getLatestRates(String outputCurrencies) {
        return retrofitClient.getOpenExchangeRatesAPI().getLatestRates(BuildConfig.API_KEY, outputCurrencies);
    }

    public Call<JsonObject> getUsageInfo() {
        return retrofitClient.getOpenExchangeRatesAPI().getUsageInfo(BuildConfig.API_KEY);
    }
}