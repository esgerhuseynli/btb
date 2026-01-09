package az.btb.mobilebanking.ui.verify_profile_changes;

import android.content.SharedPreferences;

import javax.inject.Inject;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ChangeMobileUserDataRequest;
import az.btb.mobilebanking.models.KeystoreIncidentRequest;
import az.btb.mobilebanking.models.MobileUser;
import az.btb.mobilebanking.models.NewMobileUserData;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.models.SignInRequest;
import az.btb.mobilebanking.models.VerifyMobileUserDataChangeRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.PASSWORD_HASH;
import static az.btb.mobilebanking.utils.Constants.SIGN_IN_TYPE;
import static az.btb.mobilebanking.utils.Constants.USERNAME;

@InjectViewState
public class VerifyProfileChangesPresenter extends MvpPresenter<VerifyProfileChangesView> {

    private final Router router;
    private final AuthService authService;
    private final SharedPreferences preferences;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    VerifyProfileChangesPresenter(Router router, AuthService authService, SharedPreferences preferences) {
        this.router = router;
        this.authService = authService;
        this.preferences = preferences;
    }

    void completeVerification(int mode, String password) {
        VerifyMobileUserDataChangeRequest verifyCodeRequest =
            new VerifyMobileUserDataChangeRequest(Utils.getCommonRequest(), mode, password);

        compositeDisposable.add(
            authService
                .verifyMobileUserDataChange(verifyCodeRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(s -> getViewState().showLoading(true))
                .doFinally(() -> getViewState().showLoading(false))
                .subscribe(
                    verificationResponse -> {
                        if (verificationResponse.getResponseInfo().getResponseType() == 0)
                            getViewState().showSuccessDialog(verifyCodeRequest.getChangeMobileUserDataMode(), preferences.getInt(SIGN_IN_TYPE, -1));
                        else
                            getViewState().showError(verificationResponse.getResponseInfo().getResponseMessage());
                    }
                )
        );
    }

    void sendAgain(NewMobileUserData newMobileUserData) {
        ChangeMobileUserDataRequest request = new ChangeMobileUserDataRequest(
            Utils.getCommonRequest(),
            newMobileUserData
        );

        compositeDisposable.add(
            authService
                .changeMobileUserData(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(s -> getViewState().showLoading(true))
                .doFinally(() -> getViewState().showLoading(false))
                .subscribe(
                    verificationResponse ->
                        getViewState().showError(verificationResponse.getResponseInfo().getResponseMessage()),
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goHome() {
        router.newRootScreen(new MainScreens.HomeNavScreen());
    }

    void signOut() {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setUsername(preferences.getString(USERNAME,"").replace(" ", ""));
        mobileUser.setPasswordHash(preferences.getString(PASSWORD_HASH,""));

        AppData.getInstance().getRequestInfo().setMobileUser(mobileUser);

        SignInRequest signInRequestForSignOut = new SignInRequest(AppData.getInstance().getRequestInfo(),1,1,"","");

        compositeDisposable.add(
            authService
                .signIn(signInRequestForSignOut)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    signInResponse -> {
                        if (signInResponse.getResponseInfo().getResponseType() == 0) {
                            AppData.getInstance().setSessionKey(signInResponse.getSessionKey());
                            performSignOut();
                        } else {
                            if (signInResponse.getResponseInfo().getResponseType() == 2)
                                reportKeystoreIncident();
                        }
                    },
                    error -> {
                        //reportKeystoreIncident();
                        performSignOutActions();
                    }
                )
        );
    }

    private void reportKeystoreIncident() {
        compositeDisposable.add(
            authService
                .keystoreIncident(
                    // "incidentType = 1" means "OpenFaultAttempt"
                    new KeystoreIncidentRequest(Utils.getCommonRequest(), 1, 0)
                )
                .subscribeOn(Schedulers.io())
                .subscribe()
        );
    }

    private void performSignOut() {
        compositeDisposable.add(
            authService
                .signOut(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    signOutResponse -> performSignOutActions(),
                    error -> performSignOutActions()
                )
        );
    }

    private void performSignOutActions() {
        getViewState().clearAccountData();
        getViewState().showLoading(false);
        router.newRootScreen(new MainScreens.IntroScreen());
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}