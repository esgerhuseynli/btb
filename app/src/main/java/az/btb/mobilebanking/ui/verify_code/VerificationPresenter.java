package az.btb.mobilebanking.ui.verify_code;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.CardSendRequest;
import az.btb.mobilebanking.models.VerifyCodeRequest;
import az.btb.mobilebanking.screens.AuthScreens;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class VerificationPresenter extends MvpPresenter<VerificationView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject VerificationPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goToSignIn(
        int requestType, VerifyCodeRequest verifyCodeRequest, @Nullable String phone, @Nullable String email
    ) {
        compositeDisposable.add(authService.verifyCode(verifyCodeRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(s -> getViewState().showLoading(true))
                .doFinally(() -> getViewState().showLoading(false))
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() != 0) {
                            getViewState().showLoading(false);
                            getViewState().clearCode();
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                        } else {
                            final int signUpResult = response.getMobileUserSignUpStatus();

                            if (signUpResult == 0) {
                                // verification kodunu mobil nomre/email ile qeydiyyat sehifesini gonder
                                router.navigateTo(
                                    new AuthScreens.SignUpByTypesScreen(
                                        requestType,
                                        verifyCodeRequest.getVerificationCode(),
                                        phone,
                                        email,
                                        null
                                    )
                                );
                            } else if (signUpResult == 1) {
                                // sign up success olub, login sehifesine yonlendir
                                router.replaceScreen(new AuthScreens.SignInScreen(phone, email));
                            } else if (signUpResult == 2) {
                                // TODO: possible I should call showError() method here
                                //       waiting response from whatsapp group...
                            } else {
                                getViewState().clearCode();
                                getViewState().showError(R.string.unknown_error_occurred);
                            }
                        }
                    },
                    error -> getViewState().showError(error.getLocalizedMessage())
                )
        );
    }

    void sendAgain(CardSendRequest cardSendRequest) {
        compositeDisposable.add(authService.signUp(cardSendRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(s -> getViewState().showLoading(true))
            .doFinally(() -> getViewState().showLoading(false))
            .subscribe(
                cardSendResponse -> getViewState().showError(cardSendResponse.getResponseInfo().getResponseMessage()),
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

    void goBack() {
        router.exit();
    }
}