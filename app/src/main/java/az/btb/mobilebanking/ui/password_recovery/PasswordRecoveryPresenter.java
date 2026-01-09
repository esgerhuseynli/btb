package az.btb.mobilebanking.ui.password_recovery;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ForgotPasswordRequest;
import az.btb.mobilebanking.screens.AuthScreens;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PasswordRecoveryPresenter extends MvpPresenter<PasswordRecoveryView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject PasswordRecoveryPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void send(ForgotPasswordRequest forgotPasswordRequest) {
        compositeDisposable.add(authService.forgotPassword(forgotPasswordRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    getViewState().showLoading(false);

                    if (response.getResponseInfo().getResponseType() != 0)
                        getViewState().showError(response.getResponseInfo().getResponseMessage());
                    else
                        router.navigateTo(new AuthScreens.PasswordRecoveryChangeScreen(forgotPasswordRequest, response.getMobileNumber()));
                },
                error -> {
                    getViewState().showLoading(false);
                    getViewState().showError(error.getMessage());
                }
            ));
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
