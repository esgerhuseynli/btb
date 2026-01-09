package az.btb.mobilebanking.ui.sign_up_pin;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import javax.inject.Inject;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ChangeKeystoreRequest;
import az.btb.mobilebanking.models.FcmTokenRequest;
import az.btb.mobilebanking.models.KeystoreIncidentRequest;
import az.btb.mobilebanking.models.MobileDeviceSpecifications;
import az.btb.mobilebanking.models.MobileUser;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.models.SignInRequest;
import az.btb.mobilebanking.screens.AuthScreens;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.FCM_NOTIFICATION_TOKEN;
import static az.btb.mobilebanking.utils.Constants.HAS_ACTIVE_SESSION;
import static az.btb.mobilebanking.utils.Constants.IS_FINGERPRINT_ENABLED;
import static az.btb.mobilebanking.utils.Constants.PASSWORD_HASH;
import static az.btb.mobilebanking.utils.Constants.PIN_HASH;
import static az.btb.mobilebanking.utils.Constants.SESSION_KEY;
import static az.btb.mobilebanking.utils.Constants.USERNAME;

@InjectViewState
public class SignUpPinPresenter extends MvpPresenter<SignUpPinView> {

    private final Router router;
    private final AuthService authService;
    private final SharedPreferences preferences;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    
    private int signUpType;
    private final MobileUser mobileUser = new MobileUser();

    @Inject SignUpPinPresenter(Router router, AuthService authService, SharedPreferences preferences) {
        this.router = router;
        this.authService = authService;
        this.preferences = preferences;
    }

    void finishSignUp(
        int signUpType, @NonNull String username, @NonNull String password,
        String pin, boolean isComingFromSignInScreen, @NonNull FragmentActivity activity
    ) {
        this.signUpType = signUpType;

        mobileUser.setUsername(username.replace(" ", ""));
        mobileUser.setPasswordHash(password);

        AppData.getInstance().getRequestInfo().setMobileUser(mobileUser);

        SignInRequest signInRequest = new SignInRequest(AppData.getInstance().getRequestInfo(), 0, signUpType, "", "");
        if (!isComingFromSignInScreen)
            chainedSignIn(signInRequest, pin, activity);
        else {
            signInRequest.setKeystoreType(1);
            singleSignIn(signInRequest, pin, activity);
        }
    }

    private void singleSignIn(SignInRequest signInRequest, String pin, @NonNull FragmentActivity activity) {
        getViewState().disableButtons(false);
        compositeDisposable.add(
            authService
                .signIn(signInRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(
                    signInResponse -> {
                        if (signInResponse.getResponseInfo().getResponseType() == 0) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean(HAS_ACTIVE_SESSION, true);
                            editor.putString(PIN_HASH, Utils.passwordHash(pin));
                            editor.putString(USERNAME, signInRequest.getRequestInfo().getMobileUser().getUsername());
                            editor.putString(PASSWORD_HASH, signInRequest.getRequestInfo().getMobileUser().getPasswordHash());
                            editor.putString(SESSION_KEY, signInResponse.getSessionKey());
                            editor.apply();

                            AppData.getInstance().setSessionKey(signInResponse.getSessionKey());

                            sendFCMToken();

                            return authService
                                .listBankCards(new RequestInfoRequest(Utils.getCommonRequest()))
                                .subscribeOn(Schedulers.io());
                        } else
                            throw new UnsupportedOperationException(signInResponse.getResponseInfo().getResponseMessage());
                    }
                )
                .flatMap(
                    bankCardsResponse -> {
                        if (bankCardsResponse.getResponseInfo().getResponseType() == 0) {
                            getViewState().setAppBankCards(bankCardsResponse.getBankCards());

                            return authService
                                .listBankAccounts(new RequestInfoRequest(Utils.getCommonRequest()))
                                .subscribeOn(Schedulers.io());
                        } else
                            throw new UnsupportedOperationException(bankCardsResponse.getResponseInfo().getResponseMessage());
                    }
                )
                .subscribe(
                    bankAccountsResponse -> {
                        if (bankAccountsResponse.getResponseInfo().getResponseType() == 0) {
                            getViewState().setAppBankAccounts(bankAccountsResponse.getBankAccounts());

                            if (Utils.isFingerprintServiceAvailable(activity))
                                new Handler(Looper.getMainLooper()).post(
                                    () -> router.navigateTo(new AuthScreens.FingerprintScreen(true))
                                );
                            else
                                new Handler(Looper.getMainLooper()).post(
                                    () -> goToHomeWithOutFingerprintSubmission(activity)
                                );
                        } else {
                            getViewState().progressBarState(false);
                            getViewState().showError(bankAccountsResponse.getResponseInfo().getResponseMessage());
                        }
                    },
                    error -> {
                        getViewState().progressBarState(false);
                        getViewState().showError(error.getMessage());
                    }
                )
        );
    }

    private void chainedSignIn(SignInRequest signInRequest, String pin, @NonNull FragmentActivity activity) {
        getViewState().disableButtons(false);
        compositeDisposable.add(
            authService
                .signIn(signInRequest)
                .subscribeOn(Schedulers.io())
                .flatMap(signInResponse -> {
                    if (signInResponse.getResponseInfo().getResponseType() == 0) {
                        AppData.getInstance().setSessionKey(signInResponse.getSessionKey());
                        AppData.getInstance().getRequestInfo().getMobileUser().setSaltSignature(AppData.getInstance().getSessionKey());
                        ChangeKeystoreRequest changeKeystoreRequest = new ChangeKeystoreRequest(AppData.getInstance().getRequestInfo(), 1, new MobileDeviceSpecifications("Available", "Available", "NotAvailable"));
                        return authService.changeKeystore(changeKeystoreRequest).subscribeOn(Schedulers.io());
                    } else {
                        if (signInResponse.getResponseInfo().getResponseType() == 2)
                            reportKeystoreIncident();

                        throw new NumberFormatException(signInResponse.getResponseInfo().getResponseMessage());
                    }
                })
                .flatMap(changeKeystoreResponse -> {
                    if (changeKeystoreResponse.getResponseInfo().getResponseType() == 0) {
                        AppData.getInstance().getRequestInfo().setMobileUser(mobileUser);

                        SignInRequest newSignInRequest = new SignInRequest(AppData.getInstance().getRequestInfo(), 1, signUpType, "", "");
                        newSignInRequest.getRequestInfo().getMobileUser().setPasswordHash(changeKeystoreResponse.getPasswordHash());
                        return authService.signIn(newSignInRequest);
                    } else
                        throw new NumberFormatException(changeKeystoreResponse.getResponseInfo().getResponseMessage());
                })
                .flatMap(
                    secondSignInRequestResponse -> {
                        if (secondSignInRequestResponse.getResponseInfo().getResponseType() == 0) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean(HAS_ACTIVE_SESSION, true);
                            editor.putString(PIN_HASH, Utils.passwordHash(pin));
                            editor.putString(USERNAME, signInRequest.getRequestInfo().getMobileUser().getUsername());
                            editor.putString(PASSWORD_HASH, signInRequest.getRequestInfo().getMobileUser().getPasswordHash());
                            editor.putString(SESSION_KEY, secondSignInRequestResponse.getSessionKey());
                            editor.apply();

                            AppData.getInstance().setSessionKey(secondSignInRequestResponse.getSessionKey());

                            sendFCMToken();

                            return authService
                                .listBankCards(new RequestInfoRequest(Utils.getCommonRequest()))
                                .subscribeOn(Schedulers.io());
                        } else {
                            if (secondSignInRequestResponse.getResponseInfo().getResponseType() == 2)
                                reportKeystoreIncident();

                            throw new UnsupportedOperationException(secondSignInRequestResponse.getResponseInfo().getResponseMessage());
                        }
                    }
                )
                .flatMap(
                    bankCardsResponse -> {
                        if (bankCardsResponse.getResponseInfo().getResponseType() == 0) {
                            getViewState().setAppBankCards(bankCardsResponse.getBankCards());

                            return authService
                                .listBankAccounts(new RequestInfoRequest(Utils.getCommonRequest()))
                                .subscribeOn(Schedulers.io());
                        } else
                            throw new UnsupportedOperationException(bankCardsResponse.getResponseInfo().getResponseMessage());
                    }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    bankAccountsResponse -> {
                        if (bankAccountsResponse.getResponseInfo().getResponseType() == 0) {
                            getViewState().setAppBankAccounts(bankAccountsResponse.getBankAccounts());

                            if (Utils.isFingerprintServiceAvailable(activity))
                                new Handler(Looper.getMainLooper()).post(
                                    () -> router.navigateTo(new AuthScreens.FingerprintScreen(true))
                                );
                            else
                                new Handler(Looper.getMainLooper()).post(
                                    () -> goToHomeWithOutFingerprintSubmission(activity)
                                );
                        } else {
                            getViewState().progressBarState(false);
                            getViewState().showError(bankAccountsResponse.getResponseInfo().getResponseMessage());
                        }
                    },
                    error -> {
                        getViewState().progressBarState(false);
                        getViewState().showError(error.getMessage());
                    }
                )
        );
    }

    private void reportKeystoreIncident() {
        compositeDisposable.add(
            authService.keystoreIncident(
                // "incidentType = 1" means "OpenFaultAttempt"
                new KeystoreIncidentRequest(Utils.getCommonRequest(), 1, 0)
            ).subscribeOn(Schedulers.io()).subscribe()
        );
    }

    /**
     * Writes a preference key-value pair to the proper file.
     */
    private void goToHomeWithOutFingerprintSubmission(@NonNull FragmentActivity activity) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_FINGERPRINT_ENABLED, false);
        editor.putBoolean(HAS_ACTIVE_SESSION, true);
        editor.apply();
        
        Utils.stopForceBypassPinFingerprintScreen(activity);
        
        router.newRootScreen(new MainScreens.HomeNavScreen());
    }

    private void sendFCMToken() {
        final String fcmToken = preferences.getString(FCM_NOTIFICATION_TOKEN, "");
        if (fcmToken.length() != 0) {
            FcmTokenRequest tokenRequest = new FcmTokenRequest(Utils.getCommonRequest(), fcmToken);
            compositeDisposable.add(
                authService
                    .sendFCMToken(tokenRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            );
        }
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
