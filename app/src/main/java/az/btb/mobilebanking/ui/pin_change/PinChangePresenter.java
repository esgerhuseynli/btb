package az.btb.mobilebanking.ui.pin_change;

import android.content.SharedPreferences;

import javax.inject.Inject;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ChangeKeystoreRequest;
import az.btb.mobilebanking.models.MobileDeviceSpecifications;
import az.btb.mobilebanking.models.SignInRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.HAS_ACTIVE_SESSION;
import static az.btb.mobilebanking.utils.Constants.PASSWORD_HASH;
import static az.btb.mobilebanking.utils.Constants.PIN_HASH;
import static az.btb.mobilebanking.utils.Constants.SESSION_KEY;
import static az.btb.mobilebanking.utils.Constants.USERNAME;


@InjectViewState
public class PinChangePresenter extends MvpPresenter<PinChangeView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final SharedPreferences preferences;

    @Inject PinChangePresenter(Router router, AuthService authService, SharedPreferences preferences) {
        this.router = router;
        this.authService = authService;
        this.preferences = preferences;
    }

    void changePin(String pin2) {
        getViewState().disableButtons(false);

        AppData.getInstance().getRequestInfo().getMobileUser().setSaltSignature(AppData.getInstance().getSessionKey());
        ChangeKeystoreRequest request = new ChangeKeystoreRequest(AppData.getInstance().getRequestInfo(), 1, new MobileDeviceSpecifications("Available", "Available", "NotAvailable"));

        compositeDisposable.add(
            authService
                .changeKeystore(request)
                .subscribeOn(Schedulers.io())
                .flatMap(changeKeystoreResponse -> {
                    if (changeKeystoreResponse.getResponseInfo().getResponseType() == 0) {
                        SignInRequest newSignInRequest = new SignInRequest(AppData.getInstance().getRequestInfo(), 1, 1, "", "");
                        newSignInRequest.getRequestInfo().getMobileUser().setUsername(preferences.getString(USERNAME, ""));
                        newSignInRequest.getRequestInfo().getMobileUser().setPasswordHash(changeKeystoreResponse.getPasswordHash());

                        preferences.edit().putString(PASSWORD_HASH, changeKeystoreResponse.getPasswordHash()).apply();

                        return authService.signIn(newSignInRequest).subscribeOn(Schedulers.io());
                    } else
                        throw new NumberFormatException(changeKeystoreResponse.getResponseInfo().getResponseMessage());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signInResponse -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    if (signInResponse.getResponseInfo().getResponseType() == 0) {
                        editor.putString(PIN_HASH, Utils.passwordHash(pin2));
                        editor.putString(SESSION_KEY, signInResponse.getSessionKey());
                        editor.apply();
                        AppData.getInstance().setSessionKey(signInResponse.getSessionKey());
                        router.exit();
                    } else {
                        editor.putBoolean(HAS_ACTIVE_SESSION, false).apply();
                        getViewState().showError(signInResponse.getResponseInfo().getResponseMessage());
                        router.newRootScreen(new MainScreens.IntroScreen());
                    }
                })
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
