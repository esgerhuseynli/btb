package az.btb.mobilebanking.ui.exchange_rates;

import android.util.Pair;

import java.util.List;

import az.btb.mobilebanking.utils.ExchangeRateData;
import moxy.MvpView;

interface ExchangeRatesView extends MvpView {
    void showError(String message);
    void setExchangeRatesData(Pair<List<ExchangeRateData>, List<ExchangeRateData>> buyAndSellRates);
}
