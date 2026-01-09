package az.btb.mobilebanking.ui.my_items.my_deposits;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.BankDeposit;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.ui.my_items.MyItemsView;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MyDepositsPresenter extends MvpPresenter<MyItemsView<BankDeposit>> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    MyDepositsPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void getMyDepositsData() {
        RequestInfoRequest requestInfoRequest = new RequestInfoRequest(Utils.getCommonRequest());

        compositeDisposable.add(
            authService
                .listBankDeposits(requestInfoRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> getViewState().showItemsList(response.getBankDeposits()),
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

    void showMyDepositInfo(BankDeposit bankDeposit) {
        router.replaceScreen(new MainScreens.MyDepositInfoScreen(bankDeposit));
    }
}
