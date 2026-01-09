package az.btb.mobilebanking.adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.databinding.CashNonCashRatesListItemBinding;
import az.btb.mobilebanking.utils.ExchangeRateData;
import az.btb.mobilebanking.utils.Utils;

import static az.btb.mobilebanking.utils.Constants.EXCHANGE_RATE_CASH;
import static az.btb.mobilebanking.utils.Constants.EXCHANGE_RATE_NON_CASH;

public class CashNonCashRatesAdapter extends RecyclerView.Adapter<CashNonCashRatesAdapter.CashNonCashRatesAdapterViewHolder>{

    private final int cashOrNonCash;
    private final Pair<List<ExchangeRateData>, List<ExchangeRateData>> cashNonCashRates;

    public CashNonCashRatesAdapter(int cashOrNonCash, Pair<List<ExchangeRateData>, List<ExchangeRateData>> cashNonCashRates) {
        this.cashOrNonCash = cashOrNonCash;
        this.cashNonCashRates = cashNonCashRates;
    }

    @NonNull
    @Override
    public CashNonCashRatesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CashNonCashRatesAdapterViewHolder(
            CashNonCashRatesListItemBinding.inflate(
                layoutInflater, parent, false
            )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CashNonCashRatesAdapterViewHolder holder, int position) {
        if (cashOrNonCash == EXCHANGE_RATE_CASH)
            holder.bindValues(cashNonCashRates.first.get(position));
        if (cashOrNonCash == EXCHANGE_RATE_NON_CASH)
            holder.bindValues(cashNonCashRates.second.get(position));
    }

    @Override
    public int getItemCount() {
        return cashNonCashRates.first.size();
    }

    static class CashNonCashRatesAdapterViewHolder extends RecyclerView.ViewHolder {

        private CashNonCashRatesListItemBinding binding;

        CashNonCashRatesAdapterViewHolder(@NonNull CashNonCashRatesListItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void bindValues(@NonNull ExchangeRateData rates) {
            binding.currencyIcon.setCompoundDrawablesWithIntrinsicBounds(getCurrencyIcon(rates.currency), 0, 0, 0);

            binding.setCurrencyName(Utils.getCurrency(rates.currency));
            binding.setCurrencyBuyRate(rates.buyRate);
            binding.setCurrencySellRate(rates.sellRate);

            binding.executePendingBindings();
        }

        private @DrawableRes int getCurrencyIcon(int currencyNumber) {
            switch (currencyNumber) {
                case 1:
                    return R.drawable.ic_usd;
                case 2:
                    return R.drawable.ic_eur;
                case 3:
                    return R.drawable.ic_rub;
                case 4:
                    return R.drawable.ic_gbp;
                case 5:
                    return R.drawable.ic_try;
                default:
                    return R.drawable.ic_unknown_currency;
            }
        }
    }
}
