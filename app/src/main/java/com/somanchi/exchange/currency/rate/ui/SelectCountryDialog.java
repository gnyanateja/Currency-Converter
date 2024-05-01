package com.somanchi.exchange.currency.rate.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.somanchi.exchange.currency.rate.data.model.Country;
import com.somanchi.exchange.currency.rate.databinding.FragmentSelectCountryBinding;
import com.somanchi.exchange.currency.rate.ui.adapter.CountryAdapter;
import com.somanchi.exchange.currency.rate.ui.viewmodel.CurrencyViewModel;
import com.somanchi.exchange.currency.rate.util.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectCountryDialog extends BottomSheetDialogFragment implements CountryAdapter.OnCountryClickListener {
    private FragmentSelectCountryBinding binding;
    private CountryAdapter adapter;
    private final List<Country> countryList = new ArrayList<>();
    private CurrencyViewModel viewModel;
    private Type type;

    enum Type {
        FROM_COUNTRY,
        TO_COUNTRY
    }
    public static SelectCountryDialog createFragment(Type type){
        SelectCountryDialog fragment = new SelectCountryDialog();
        fragment.type = type;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectCountryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CurrencyViewModel.class);
        setupRecyclerView();
        observeViewModel();
        setupSearchView();
    }

    private void setupRecyclerView() {
        adapter = new CountryAdapter(countryList, this);
        binding.rvCountries.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCountries.setAdapter(adapter);
    }

    private void observeViewModel() {
        if(viewModel.getCountriesLiveData().getValue()==null || viewModel.getCountriesLiveData().getValue().isEmpty()){
            viewModel.getCountriesLiveData().observe(getViewLifecycleOwner(), this::loadCountries);
            viewModel.fetchCountries(requireContext());
        } else{
            loadCountries(viewModel.getCountriesLiveData().getValue());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCountries(List<Country> countries){
        countryList.clear();
        countryList.addAll(countries);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCountryClick(Country country) {
        if(type == Type.FROM_COUNTRY){
            viewModel.getSelectedFromCountry().setValue(country);
        } else {
            viewModel.getSelectedToCountry().setValue(country);
            viewModel.getCurrencyToCountry().setValue("");
        }
        KeyboardUtils.hideKeyboard(requireActivity());
        dismiss();
    }

    private String previousSearch = "";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private void setupSearchView() {
        binding.edSearch.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence data, int i, int i1, int i2) {
                handler.removeCallbacksAndMessages(null); // Remove previous callbacks
                handler.postDelayed(() -> {
                    if(previousSearch!=data.toString()){
                        previousSearch = data.toString();
                        filter(previousSearch);
                    }
                }, 300); // Delay for 300 milliseconds
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void filter(String query) {
        if(query.isEmpty()){
            adapter.setData(countryList);
        } else {
            List<Country> filteredList = new ArrayList<>();
            for (Country country : countryList) {
                String countryName = country.getCode()+country.getName();
                if (countryName.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(country);
                }
            }
            adapter.setData(filteredList);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardUtils.hideKeyboard(requireActivity());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
