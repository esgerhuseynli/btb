package az.btb.mobilebanking.ui.operations_history.card_to_card;


import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.OperationsHistoryRequest;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class CardToCardHistoryPresenter extends MvpPresenter<CardToCardHistoryView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject CardToCardHistoryPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void getOperationsHistory(String sourceCardId, String operationType, String fromDate, String toDate) {
        OperationsHistoryRequest request = new OperationsHistoryRequest(
            Utils.getCommonRequest(),
            sourceCardId,
            operationType,
            fromDate,
            toDate
        );

        compositeDisposable.add(
            authService
                .listOperationsHistory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().setOperationsHistory(response.getBankCardOperations());
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
