package az.btb.mobilebanking.ui.operations_history.international_transfer;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ForeignAccountTransfer;
import az.btb.mobilebanking.models.InternationalTransfersHistoryRequest;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class InternationalTransfersHistoryPresenter extends MvpPresenter<InternationalTransfersHistoryView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject InternationalTransfersHistoryPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
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
                            getViewState().showCards(response.getBankCards());
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
                            getViewState().showAccounts(response.getBankAccounts());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void getHistory(String cardId, String accountIban, String fromDate, String toDate) {
        InternationalTransfersHistoryRequest request = new InternationalTransfersHistoryRequest(
            Utils.getCommonRequest(),
            cardId,
            accountIban,
            fromDate,//"02-08-2019",
            toDate//"10-09-2020"
        );

        compositeDisposable.add(
            authService
                .getInternationalTransfersHistory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showHistory(response.getForeignAccountTransfers());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void showDetails(ForeignAccountTransfer transferItem) {
        router.navigateTo(new MainScreens.InternationalTransferDetailsScreen(transferItem));
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
