package az.btb.mobilebanking.ui.money_transfers.history;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.MoneyTransferOperationStatusCheckRequest;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MoneyTransferHistoryPresenter extends MvpPresenter<MoneyTransferHistoryView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    MoneyTransferHistoryPresenter(final Router router, final AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void checkOperationStatusBy(final String transferNumber) {
        MoneyTransferOperationStatusCheckRequest request = new MoneyTransferOperationStatusCheckRequest(
            Utils.getCommonRequest(),
            transferNumber
        );

        compositeDisposable.add(
            authService
                .getTransferStatusBy(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showMoneyTransferResult(response.getTransferStatusInfo());
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
