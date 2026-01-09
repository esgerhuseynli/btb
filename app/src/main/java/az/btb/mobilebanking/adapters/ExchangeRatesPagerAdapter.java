package az.btb.mobilebanking.adapters;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import az.btb.mobilebanking.ui.exchange_rates.cash_exchange_rates.CashExchangeRatesFragment;
import az.btb.mobilebanking.ui.exchange_rates.non_cash_exchange_rates.NonCashExchangeRatesFragment;
import az.btb.mobilebanking.utils.ExchangeRateData;

public class ExchangeRatesPagerAdapter extends FragmentStateAdapter {

    private final Pair<List<ExchangeRateData>, List<ExchangeRateData>> cashNonCashRates;

    public ExchangeRatesPagerAdapter(
        @NonNull Fragment fragment,
        @NonNull Pair<List<ExchangeRateData>, List<ExchangeRateData>> cashNonCashRates
    ) {
        super(fragment);
        this.cashNonCashRates = cashNonCashRates;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new CashExchangeRatesFragment(cashNonCashRates);
        else
            return new NonCashExchangeRatesFragment(cashNonCashRates);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
