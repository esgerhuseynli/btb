package az.btb.mobilebanking.ui.exchange_rates;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.BankExchangeRate;
import az.btb.mobilebanking.models.ExchangeRatesRequest;
import az.btb.mobilebanking.utils.ExchangeRateData;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.EXCHANGE_RATE_TYPE_BUY;
import static az.btb.mobilebanking.utils.Constants.EXCHANGE_RATE_TYPE_SELL;

@InjectViewState
public class ExchangeRatesPresenter extends MvpPresenter<ExchangeRatesView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject ExchangeRatesPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void getExchangeRates(String targetDate) {
        ExchangeRatesRequest request = new ExchangeRatesRequest(Utils.getCommonRequest(), targetDate, targetDate);
        compositeDisposable.add(
            authService
                .getExchangeRates(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            // first, get all exchange rates.
                            final List<BankExchangeRate> allExchangeRates = response.getBankExchangeRates();

                            // find all elements count
                            final int listCount = allExchangeRates.size();

                            // divide the size by 2 to find each list how much element should contain.
                            final int elementCountForEach = listCount / 2;

                            // create 2 different lists to be shown in our ViewPager.
                            final List<ExchangeRateData> cashRates = new ArrayList<>(elementCountForEach);
                            final List<ExchangeRateData> nonCashRates = new ArrayList<>(elementCountForEach);

                            // iterate over all elements of the primary list to fill the lists above.
                            for (int i = 0; i < listCount; i+=2) {
                                final BankExchangeRate currentItem = allExchangeRates.get(i);
                                final BankExchangeRate nextItem = allExchangeRates.get(i + 1);

                                if (currentItem.getRateType() == EXCHANGE_RATE_TYPE_BUY && nextItem.getRateType() == EXCHANGE_RATE_TYPE_SELL) {
                                    cashRates.add(
                                        new ExchangeRateData(
                                            currentItem.getCurrency(),
                                            currentItem.getRateForCash(),
                                            nextItem.getRateForCash()
                                        )
                                    );
                                    nonCashRates.add(
                                        new ExchangeRateData(
                                            currentItem.getCurrency(),
                                            currentItem.getRateForNonCash(),
                                            nextItem.getRateForNonCash()
                                        )
                                    );
                                } else {
                                    cashRates.add(
                                        new ExchangeRateData(
                                            currentItem.getCurrency(),
                                            nextItem.getRateForCash(),
                                            currentItem.getRateForCash()
                                        )
                                    );
                                    nonCashRates.add(
                                        new ExchangeRateData(
                                            currentItem.getCurrency(),
                                            nextItem.getRateForNonCash(),
                                            currentItem.getRateForNonCash()
                                        )
                                    );
                                }
                            }

                            // pass both lists as pair to the fragment.
                            getViewState().setExchangeRatesData(new Pair<>(cashRates, nonCashRates));
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goBack() {
        router.exit();
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
