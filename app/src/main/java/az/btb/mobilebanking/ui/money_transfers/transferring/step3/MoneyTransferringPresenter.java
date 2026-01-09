package az.btb.mobilebanking.ui.money_transfers.transferring.step3;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.MoneyTransferRequest;
import az.btb.mobilebanking.models.PayerInfo;
import az.btb.mobilebanking.models.TransferReceiverInfo;
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
public class MoneyTransferringPresenter extends MvpPresenter<MoneyTransferringStep3View> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject MoneyTransferringPresenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack(final MoneyTransfersFragment.MoneyTransferData moneyTransferData) {
        router.exit();
    }

    void doMoneyTransfer(
        MoneyTransfersFragment.MoneyTransferData moneyTransferData,
        MoneyTransfersFragment.MoneyTransferReceiverData moneyTransferReceiverData
    ) {
        MoneyTransferRequest request = new MoneyTransferRequest(
            Utils.getCommonRequest(),
            new PayerInfo(
                moneyTransferData.getTransferPaymentType(),
                moneyTransferData.getCardOrAccountData().getCardId(),
                moneyTransferData.getCardOrAccountData().getAccountIBAN()
            ),
            moneyTransferData.getPointType(),
            moneyTransferData.getTransferUniqueName(),
            moneyTransferData.getFromPointData().getCountryIso(),
            moneyTransferData.getToPointData().getPointId(),
            moneyTransferData.getFromPointData().getPointId(),
            moneyTransferReceiverData.getAmount(),
            moneyTransferReceiverData.getCurrency(),
            new TransferReceiverInfo(
                moneyTransferReceiverData.getSurname(),
                moneyTransferReceiverData.getName(),
                moneyTransferReceiverData.getFatherName(),
                moneyTransferReceiverData.getPhoneNumber()
            )
        );

        compositeDisposable.add(
            authService
                .doMoneyTransfer(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showResult(response.getSendTransferInfo());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goHome() {
        router.backTo(new MainScreens.HomeNavScreen());
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
