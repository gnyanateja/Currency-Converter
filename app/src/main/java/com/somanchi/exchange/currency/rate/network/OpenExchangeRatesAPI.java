package com.somanchi.exchange.currency.rate.network;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenExchangeRatesAPI {
    @GET("latest.json")
    Call<JsonObject> getLatestRates(@Query("app_id") String appId,
                                    @Query("symbols") String symbols);

    @GET("usage.json")
    Call<JsonObject> getUsageInfo(@Query("app_id") String appId);
}