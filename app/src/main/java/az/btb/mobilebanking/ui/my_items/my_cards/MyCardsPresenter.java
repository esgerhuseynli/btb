package az.btb.mobilebanking.ui.my_items.my_cards;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.BankCard;
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
public class MyCardsPresenter extends MvpPresenter<MyItemsView<BankCard>> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject MyCardsPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void getMyCardsData() {
        RequestInfoRequest requestInfoRequest = new RequestInfoRequest(Utils.getCommonRequest());

        compositeDisposable.add(
            authService
                .listBankCards(requestInfoRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showItemsList(response.getBankCards());
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

    void showMyCardInfo(BankCard bankCard) {
        router.navigateTo(new MainScreens.MyCardInfoScreen(bankCard));
    }
}
