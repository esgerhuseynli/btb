package az.btb.mobilebanking.ui.international_transfers;

import java.math.BigDecimal;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ForeignReceiverInfo;
import az.btb.mobilebanking.models.InternationalTransferPayerInfo;
import az.btb.mobilebanking.models.InternationalTransferRequest;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.collections.CollectionsKt;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class InternationalTransfersPresenter extends MvpPresenter<InternationalTransfersView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject InternationalTransfersPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.backTo(new MainScreens.HomeNavScreen());
    }

    void getCards() {
        compositeDisposable.add(
            authService
                .listBankCards(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showCards(
                                CollectionsKt.filter(
                                    response.getBankCards(),
                                    ba -> ba.getCurrency() != 0
                                )
                            );
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void getBankAccounts() {
        compositeDisposable.add(
            authService
                .listBankAccounts(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showAccounts(
                                CollectionsKt.filter(
                                    response.getBankAccounts(),
                                    ba -> ba.getCurrency() != 0
                                )
                            );
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void makeInternationalTransfer(
        InternationalTransferPayerInfo payerInfo,
        String transferNumber,
        ForeignReceiverInfo foreignReceiverInfo,
        BigDecimal transferAmount,
        int transferCurrency,
        String operationDescription,
        String operationAdditionalDescription
    ) {
        InternationalTransferRequest request = new InternationalTransferRequest(
            Utils.getCommonRequest(),
            payerInfo,
            transferNumber,
            foreignReceiverInfo,
            transferAmount,
            transferCurrency,
            operationDescription,
            operationAdditionalDescription
        );
        compositeDisposable.add(
            authService
                .makeInternationalTransfer(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showSuccessResult(response.getTransferNumber());
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
