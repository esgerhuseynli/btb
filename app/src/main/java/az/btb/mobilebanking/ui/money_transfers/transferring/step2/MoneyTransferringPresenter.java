package az.btb.mobilebanking.ui.money_transfers.transferring.step2;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.MoneyTransferCommissionRequest;
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
public class MoneyTransferringPresenter extends MvpPresenter<MoneyTransferringStep2View> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject MoneyTransferringPresenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    public void goBack() {
        router.exit();
    }

    void goToStep3(
        @NonNull MoneyTransfersFragment.MoneyTransferData moneyTransferData,
        @NonNull MoneyTransfersFragment.MoneyTransferReceiverData moneyTransferReceiverData
    ) {
        MoneyTransferCommissionRequest request = new MoneyTransferCommissionRequest(
            Utils.getCommonRequest(),
            moneyTransferData.getPointType(),
            moneyTransferData.getTransferUniqueName(),
            moneyTransferData.getFromPointData().getCountryIso(),
            moneyTransferData.getToPointData().getPointId(),
            moneyTransferData.getFromPointData().getPointId(),
            moneyTransferReceiverData.getAmount(),
            moneyTransferReceiverData.getCurrency()
        );

        compositeDisposable.add(
            authService
                .getMoneyTransferCommission(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            if (moneyTransferReceiverData.getAmount().compareTo(response.getSendTransferCustomerCommission().getMinTransferSum()) < 0)
                                getViewState().setMinAmount(response.getSendTransferCustomerCommission().getMinTransferSum());
                            else if (moneyTransferReceiverData.getAmount().compareTo(response.getSendTransferCustomerCommission().getMaxTransferSum()) > 0)
                                getViewState().setMaxAmount(response.getSendTransferCustomerCommission().getMaxTransferSum());
                            else
                                router.navigateTo(
                                    new MainScreens.MoneyTransferringStep3Screen(
                                        moneyTransferData,
                                        moneyTransferReceiverData,
                                        response.getSendTransferCustomerCommission().getCalculatedCommission()
                                    )
                                );
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
