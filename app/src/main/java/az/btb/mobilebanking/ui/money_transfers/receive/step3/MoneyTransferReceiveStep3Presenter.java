package az.btb.mobilebanking.ui.money_transfers.receive.step3;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.CheckTransferBeforeReceiveInfo;
import az.btb.mobilebanking.models.MoneyTransferReceiveRequest;
import az.btb.mobilebanking.models.PayerInfo;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MoneyTransferReceiveStep3Presenter extends MvpPresenter<MoneyTransferReceiveStep3View> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject MoneyTransferReceiveStep3Presenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void receiveMoney(final CheckTransferBeforeReceiveInfo checkTransferBeforeReceiveInfo, int transferPaymentType, final String id) {
        PayerInfo pi = new PayerInfo(transferPaymentType, transferPaymentType == 2 ? id : "", transferPaymentType == 1 ? id : "");
        MoneyTransferReceiveRequest request = new MoneyTransferReceiveRequest(
            Utils.getCommonRequest(),
            pi,
            checkTransferBeforeReceiveInfo.getMtUniqueName(),
            checkTransferBeforeReceiveInfo.getTransferNumber(),
            checkTransferBeforeReceiveInfo.getTransferAmount(),
            checkTransferBeforeReceiveInfo.getTransferCurrency()
        );

        compositeDisposable.add(
            authService
                .receiveMoneyTransfer(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showResult(response.getTransferReceiverInfo());
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
