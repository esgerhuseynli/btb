package az.btb.mobilebanking.ui.my_items.my_account_info;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.UpdateAccountSettingsRequest;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MyAccountInfoPresenter extends MvpPresenter<MyAccountInfoView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    MyAccountInfoPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void changeAccountData(String accountNumber, String accountNewAltName, int accountNewColor) {
        String a = accountNewAltName;
        int len = a.trim().length();
        if (len < 3) {
            StringBuilder cardNewAltNameBuilder = new StringBuilder(accountNewAltName);
            for (int i = 3; i > len; i--)
                cardNewAltNameBuilder.append(" ");
            a = cardNewAltNameBuilder.toString();
        }

        UpdateAccountSettingsRequest request = new UpdateAccountSettingsRequest(
            Utils.getCommonRequest(), accountNumber, a, accountNewColor
        );
    //    System.out.println("request: "+ new Gson().toJson(request));
        compositeDisposable.add(
            authService
                .updateAccountSettings(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showResult(accountNewAltName, accountNewColor);
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
