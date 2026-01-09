package az.btb.mobilebanking.ui.card_statements;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.BankCardStatement;
import az.btb.mobilebanking.models.BankCardStatementsRequest;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class CardStatementsPresenter extends MvpPresenter<CardStatementsView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<BankCardStatement>> mCardStatements = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<BankCardStatement>> cardStatements = mCardStatements;

    @Inject CardStatementsPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void getCardStatements(String fromCardId, String fromDate, String toDate) {
        BankCardStatementsRequest request = new BankCardStatementsRequest(
            Utils.getCommonRequest(),
            fromCardId,
            fromDate,
            toDate
        );

        compositeDisposable.add(
            authService
                .listCardStatements(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            mCardStatements.postValue(response.getBankCardStatement());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goBack() {
        router.exit();
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
