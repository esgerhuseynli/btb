package az.btb.mobilebanking.ui.money_transfers.transferring.step1;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.MoneyTransferCountriesRequest;
import az.btb.mobilebanking.models.MoneyTransferPaymentPointsRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.ui.money_transfers.MoneyTransfersFragment;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MoneyTransferringPresenter extends MvpPresenter<MoneyTransferringStep1View> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject MoneyTransferringPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void goToStep2(MoneyTransfersFragment.MoneyTransferData moneyTransferData) {
        router.navigateTo(new MainScreens.MoneyTransferringStep2Screen(moneyTransferData));
    }

    void getCountriesBy(String moneyTransferUniqueName) {
        MoneyTransferCountriesRequest request = new MoneyTransferCountriesRequest(
            Utils.getCommonRequest(),
            moneyTransferUniqueName
        );

        compositeDisposable.add(
            authService
                .getCountries(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showCountries(response.getMoneyTransferCountries());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void getToPointsBy(int pointType, String moneyTransferUniqueName, String countryIso) {
        MoneyTransferPaymentPointsRequest request = new MoneyTransferPaymentPointsRequest(
            Utils.getCommonRequest(),
            pointType,
            moneyTransferUniqueName,
            countryIso
        );

        compositeDisposable.add(
            authService
                .getPaymentPoints(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            if (pointType == 1)
                                getViewState().showToPoints(response.getMtPoints());
                            else if (pointType == 2)
                                getViewState().showToPointCities(response.getMtPointCities());
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
