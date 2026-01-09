package az.btb.mobilebanking.ui.exchange_rates.non_cash_exchange_rates;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import az.btb.mobilebanking.adapters.CashNonCashRatesAdapter;
import az.btb.mobilebanking.databinding.FragmentCashNonCashExchangeRatesBinding;
import az.btb.mobilebanking.utils.ExchangeRateData;

import static az.btb.mobilebanking.utils.Constants.EXCHANGE_RATE_NON_CASH;

public class NonCashExchangeRatesFragment extends Fragment {

    private FragmentCashNonCashExchangeRatesBinding binding;
    private Pair<List<ExchangeRateData>, List<ExchangeRateData>> cashNonCashRates;

    public NonCashExchangeRatesFragment() {
    }

    public NonCashExchangeRatesFragment(Pair<List<ExchangeRateData>, List<ExchangeRateData>> cashNonCashRates) {
        super();
        this.cashNonCashRates = cashNonCashRates;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCashNonCashExchangeRatesBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CashNonCashRatesAdapter adapter = new CashNonCashRatesAdapter(EXCHANGE_RATE_NON_CASH, cashNonCashRates);
        binding.cashNonCashRatesList.setAdapter(adapter);
    }
}
