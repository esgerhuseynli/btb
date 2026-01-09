package az.btb.mobilebanking.ui.sign_up_by_number;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ResponseInfo;
import az.btb.mobilebanking.models.SignUpRequest;
import az.btb.mobilebanking.screens.AuthScreens;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class SignUpByNumberPresenter extends MvpPresenter<SignUpByNumberView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    SignUpByNumberPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    public void signUp(SignUpRequest request, int signUpTypeNumber, String phoneNumber, String password) {
        compositeDisposable.add(
            authService
                .registerMobileUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    signUpResponse -> {
                        getViewState().showLoading(false);

                        final ResponseInfo responseInfo = signUpResponse.getResponseInfo();
                        if (responseInfo.getErrorCode() == 0 && responseInfo.getResponseType() == 0)
                            router.replaceScreen(new AuthScreens.SignUpPinScreen(signUpTypeNumber, phoneNumber, password, false));
                        else
                            getViewState().showError(responseInfo.getResponseMessage());
                    },
                    error -> {
                        getViewState().showLoading(false);
                        getViewState().showError(error.getLocalizedMessage());
                    }
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

    void goBack() {
        router.exit();
    }
}
