package az.btb.mobilebanking.ui.password_recovery_change;

import android.content.Context;

import javax.inject.Inject;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ChangeForgotPasswordRequest;
import az.btb.mobilebanking.models.ForgotPasswordRequest;
import az.btb.mobilebanking.screens.AuthScreens;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.SIGN_IN_SCREEN_FAKE_TOKEN;

@InjectViewState
public class PasswordRecoveryChangePresenter extends MvpPresenter<PasswordRecoveryChangeView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Context context;

    @Inject PasswordRecoveryChangePresenter(Router router, AuthService authService, Context context) {
        this.router = router;
        this.authService = authService;
        this.context = context;
    }

    void sendNewPassword(ChangeForgotPasswordRequest changeForgotPasswordRequest) {
        compositeDisposable.add(
            authService
                .changeForgotPassword(changeForgotPasswordRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(s -> getViewState().showLoading(true))
                .doFinally(() -> getViewState().showLoading(false))
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            getViewState().showPasswordError(context.getString(R.string.password_recovery_password_changed));
                            router.newRootScreen(new AuthScreens.SignInScreen(SIGN_IN_SCREEN_FAKE_TOKEN, SIGN_IN_SCREEN_FAKE_TOKEN));
                        } else
                            getViewState().showCodeError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showCodeError(error.getMessage())
                )
        );
    }

    void sendAgain(ForgotPasswordRequest forgotPasswordRequest) {
        compositeDisposable.add(
            authService
                .forgotPassword(forgotPasswordRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> getViewState().showCodeError(response.getResponseInfo().getResponseMessage()),
                    error -> getViewState().showCodeError(error.getMessage())
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
