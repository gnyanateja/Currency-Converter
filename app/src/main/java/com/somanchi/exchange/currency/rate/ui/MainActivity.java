package com.somanchi.exchange.currency.rate.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.somanchi.exchange.currency.rate.databinding.ActivityMainBinding;
import com.somanchi.exchange.currency.rate.ui.viewmodel.CurrencyViewModel;
import com.somanchi.exchange.currency.rate.util.KeyboardUtils;

public class MainActivity extends AppCompatActivity {
    private CurrencyViewModel viewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        binding.ivBack.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });
        viewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        observeViewModels();
        binding.edFromCountry.getEditText().setOnClickListener(v -> openSelectCountryDialog(SelectCountryDialog.Type.FROM_COUNTRY));
        binding.edFromCountry.setEndIconOnClickListener(v -> openSelectCountryDialog(SelectCountryDialog.Type.FROM_COUNTRY));
        binding.edToCountry.getEditText().setOnClickListener(v -> openSelectCountryDialog(SelectCountryDialog.Type.TO_COUNTRY));
        binding.edToCountry.setEndIconOnClickListener(v -> openSelectCountryDialog(SelectCountryDialog.Type.TO_COUNTRY));
        binding.ivConverter.setOnClickListener(v -> {
            viewModel.invertSelection();
        });
       binding.btnConvert.setOnClickListener(v -> {
           KeyboardUtils.hideKeyboard(this);
           binding.btnConvert.setVisibility(View.GONE);
           binding.pgSubmit.setVisibility(View.VISIBLE);
           viewModel.getCurrencyFromCountry().setValue(Long.valueOf(binding.edFromCurrency.getEditText().getText().toString()));
           viewModel.fetchExchangeRate();
       });
       viewModel.fetchUsageInfo();
    }

    private void observeViewModels(){
        // Observe changes in currency data
        viewModel.getCurrencyToCountry().observe(this, currency -> {
            // Update UI with currency data
            if(!currency.isEmpty()){
                binding.pgSubmit.setVisibility(View.GONE);
                binding.btnConvert.setVisibility(View.VISIBLE);
            }
            binding.edToCurrency.getEditText().setText(currency);
        });
        viewModel.getSelectedFromCountry().observe(this, country -> {
            // Update UI with currency data
            binding.edFromCountry.getEditText().setText(country.getName());
            binding.tvFromCode.setText(country.getCode());
        });
        viewModel.getSelectedToCountry().observe(this, country -> {
            // Update UI with currency data
            binding.edToCountry.getEditText().setText(country.getName());
            binding.tvToCode.setText(country.getCode());
        });
        viewModel.getUsageMessage().observe(this, msg -> {
            binding.tvUsage.setText(msg);
        });
    }

    private void openSelectCountryDialog(SelectCountryDialog.Type type) {
        SelectCountryDialog bottomSheet = SelectCountryDialog.createFragment(type);
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }
}