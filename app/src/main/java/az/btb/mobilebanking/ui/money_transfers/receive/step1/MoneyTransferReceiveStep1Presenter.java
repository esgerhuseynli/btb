package az.btb.mobilebanking.ui.money_transfers.receive.step1;

import java.math.BigDecimal;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.MoneyTransferReceiveCheckRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MoneyTransferReceiveStep1Presenter extends MvpPresenter<MoneyTransferReceiveStep1View> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    MoneyTransferReceiveStep1Presenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void checkTransferNumber(final String transferNumber, final BigDecimal amount, final int amountCurrency) {
        MoneyTransferReceiveCheckRequest request = new MoneyTransferReceiveCheckRequest(
            Utils.getCommonRequest(),
            transferNumber,
            amount,
            amountCurrency
        );

        compositeDisposable.add(
            authService
                .receiveMoneyTransferCheck(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            router.navigateTo(
                                new MainScreens.MoneyTransferReceiveStep2Screen(
                                    response.getCheckTransferBeforeReceiveInfo()
                                )
                            );
                        else
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
