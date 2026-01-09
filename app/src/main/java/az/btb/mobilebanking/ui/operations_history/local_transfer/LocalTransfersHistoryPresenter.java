package az.btb.mobilebanking.ui.operations_history.local_transfer;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.InternationalTransfersHistoryRequest;
import az.btb.mobilebanking.models.LocalAccountTransfer;
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
public class LocalTransfersHistoryPresenter extends MvpPresenter<LocalTransfersHistoryView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject LocalTransfersHistoryPresenter(Router router, AuthService authService) {
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
                            getViewState().showError("getCards");
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
                            getViewState().showError("getBankAccounts");
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
//            "02-08-2019",
//            "10-09-2020"
            fromDate,
            toDate
        );

        compositeDisposable.add(
            authService
                .getLocalTransfersHistory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        getCards();
    
                        //                    System.out.println("response error: " + new Gson().toJson(response.getResponseInfo()));
    //                    System.out.println("appInfoObject: " + new Gson().toJson(request.getRequestInfo().getAppInfo()));

                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showHistory(response.getLocalAccountTransfers());
                        else
                            getViewState().showError("gethistory");
                    },
                    error -> {
    //                    System.out.println("throwable error");
                        getViewState().showError(error.getMessage());
                    }
                )
        );
    }

    void showDetails(LocalAccountTransfer transferItem) {
        router.navigateTo(new MainScreens.LocalTransferDetailsScreen(transferItem));
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
