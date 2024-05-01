package com.somanchi.exchange.currency.rate.ui.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.somanchi.exchange.currency.rate.data.model.Country;
import com.somanchi.exchange.currency.rate.data.model.Currency;
import com.somanchi.exchange.currency.rate.data.repository.CurrencyRepository;
import com.somanchi.exchange.currency.rate.util.JSONReader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyViewModel extends ViewModel {
    private final CurrencyRepository repository = new CurrencyRepository();
    private final MutableLiveData<List<Country>> countriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Country> selectedFromCountry = new MutableLiveData<>();
    private final MutableLiveData<Long> currencyFromCountry = new MutableLiveData<>();
    private final MutableLiveData<Country> selectedToCountry = new MutableLiveData<>();
    private final MutableLiveData<String> currencyToCountry = new MutableLiveData<>();
    private final MutableLiveData<String> usageMessage = new MutableLiveData<>();

    public MutableLiveData<Long> getCurrencyFromCountry() {
        return currencyFromCountry;
    }

    public MutableLiveData<String> getCurrencyToCountry() {
        return currencyToCountry;
    }

    public LiveData<List<Country>> getCountriesLiveData() {
        return countriesLiveData;
    }

    public MutableLiveData<Country> getSelectedFromCountry() {
        return selectedFromCountry;
    }

    public MutableLiveData<Country> getSelectedToCountry() {
        return selectedToCountry;
    }

    public MutableLiveData<String> getUsageMessage() {
        return usageMessage;
    }


    public void fetchExchangeRate() {
        String baseCurrency = selectedFromCountry.getValue().getCode();
        String toCurrency = selectedToCountry.getValue().getCode();
        String outputCurrencies = baseCurrency+","+toCurrency;
        repository.getLatestRates(outputCurrencies).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: "+response.body().toString());
                    JsonObject rates = response.body().getAsJsonObject("rates");
                    // Assuming you have a selectedCountry variable holding the user's selection
                    if (rates.has(baseCurrency) && rates.has(toCurrency)) {
                        double exchangeRate = (rates.get(baseCurrency).getAsDouble())/rates.get(toCurrency).getAsDouble();
                        currencyToCountry.setValue(String.valueOf(exchangeRate));
                    }
                    if(requestsRemaining!=-1){
                        requestsRemaining-=1;
                        usageMessage.setValue(getUsageInfoMessage());
                    }
                } else {
                    // Handle API error
                    Log.e("ERROR", "onResponse: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle network error
                Log.e("ERROR", "onResponse: "+t.getMessage());
            }
        });
    }

    public void fetchCountries(Context context){
        countriesLiveData.setValue(JSONReader.readCountries(context));
    }

    public void invertSelection(){
        try {
            Country temp = selectedFromCountry.getValue();
            selectedFromCountry.setValue(selectedToCountry.getValue());
            selectedToCountry.setValue(temp);
            currencyFromCountry.setValue(1L);
            currencyToCountry.setValue("");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void fetchUsageInfo(){
        repository.getUsageInfo().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject data = response.body().getAsJsonObject("data").getAsJsonObject("usage");
                    requestsRemaining = data.get("requests_remaining").getAsInt();
                    daysRemaining = data.get("days_remaining").getAsInt();
                    Log.d("TAG", "onResponse: "+data.toString());
                    usageMessage.setValue(getUsageInfoMessage());
                } else {
                    // Handle API error
                    Log.e("ERROR", "onResponse: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle network error
                Log.e("ERROR", "onResponse: "+t.getMessage());
            }
        });
    }

    private int requestsRemaining = -1;
    private int daysRemaining = -1;
    private String getUsageInfoMessage(){
        return  "You have "+requestsRemaining+" remaining requests for the next "+daysRemaining+" days";
    }
}
