package az.btb.mobilebanking.ui.sign_in_by_number;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ChangeKeystoreRequest;
import az.btb.mobilebanking.models.MobileDeviceSpecifications;
import az.btb.mobilebanking.models.MobileUser;
import az.btb.mobilebanking.models.SignInRequest;
import az.btb.mobilebanking.screens.AuthScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.HAS_ACTIVE_SESSION;
import static az.btb.mobilebanking.utils.Constants.SESSION_KEY;
import static az.btb.mobilebanking.utils.Constants.SIGN_IN_TYPE;
import static az.btb.mobilebanking.utils.Constants.SIGN_IN_UP_TYPE_NUMBER;

@InjectViewState
public class SignInByNumberPresenter extends MvpPresenter<SignInByNumberView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences preferences;

    @Inject
    SignInByNumberPresenter(Router router, AuthService authService, SharedPreferences preferences) {
        this.router = router;
        this.authService = authService;
        this.preferences = preferences;
    }

    void signIn(@NonNull String phoneNumber, String password) {
        MobileUser mobileUser = new MobileUser(
            phoneNumber.replace(" ", ""),
            Utils.passwordHash(password),
            null, null
        );
        AppData.getInstance().getRequestInfo().setMobileUser(mobileUser);

        SignInRequest signInRequest = new SignInRequest(
            AppData.getInstance().getRequestInfo(),
            0,
            1,
            null, null
        );

        compositeDisposable.add(
            authService
                .signIn(signInRequest)
                .subscribeOn(Schedulers.io())
                .flatMap(signInResponse -> {
                    if (signInResponse.getResponseInfo().getResponseType() == 0) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(HAS_ACTIVE_SESSION, true);
                        editor.putString(SESSION_KEY, signInResponse.getSessionKey());
                        editor.putInt(SIGN_IN_TYPE, SIGN_IN_UP_TYPE_NUMBER);
                        editor.apply();
                        
                        AppData.getInstance().getRequestInfo().getMobileUser().setSaltSignature(signInResponse.getSessionKey());
                        ChangeKeystoreRequest changeKeystoreRequest = new ChangeKeystoreRequest(AppData.getInstance().getRequestInfo(), 1, new MobileDeviceSpecifications("Available", "Available", "NotAvailable"));
                        return authService.changeKeystore(changeKeystoreRequest).subscribeOn(Schedulers.io());
                    } else if (signInResponse.getSignInActionCode() == 3) {
                        // user registered but current device is also should be registered.
                        // so, redirect to sign up screen from popup.
                        throw new IllegalStateException();
                    } else
                        throw new NumberFormatException(signInResponse.getResponseInfo().getResponseMessage());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(changeKeystoreResponse -> {
                    getViewState().showLoading(false);

                    if (changeKeystoreResponse.getResponseInfo().getResponseType() == 0) {
                        router.replaceScreen(
                            new AuthScreens.SignUpPinScreen(
                                SIGN_IN_UP_TYPE_NUMBER,
                                signInRequest.getRequestInfo().getMobileUser().getUsername(),
                                changeKeystoreResponse.getPasswordHash(),
                                true
                            )
                        );
                    } else
                        getViewState().showError(changeKeystoreResponse.getResponseInfo().getResponseMessage());
                }, e -> {
                    getViewState().showLoading(false);
                    if (e instanceof NumberFormatException)
                        getViewState().showError(e.getMessage());

                    if (e instanceof IllegalStateException)
                        getViewState().showSignUpInfo();
                }
            )
        );
    }

    void goToForgotPassword() {
        router.navigateTo(new AuthScreens.PasswordRecoveryByTypesScreen());
    }

    void goToSignUp() {
        // there is no need to specify screenType value. you can pass any value
        router.navigateTo(new AuthScreens.SignUpByTypesScreen(-1, null, null, null, null));
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
