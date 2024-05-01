package com.somanchi.exchange.currency.rate.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.somanchi.exchange.currency.rate.R;
import com.somanchi.exchange.currency.rate.data.model.Country;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONReader {

    public static List<Country> readCountries(Context context) {
        List<Country> countries = new ArrayList<>();

        try {
            // Read the JSON file from resources
            InputStream inputStream = context.getResources().openRawResource(R.raw.countries);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");

            // Convert JSON string to Map
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            Map<String, String> countriesMap = gson.fromJson(json, type);
            // Now you can use the 'countriesMap' map
            for (Map.Entry<String, String> entry : countriesMap.entrySet()) {
                countries.add(new Country(entry.getKey(), entry.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countries;
    }
}
