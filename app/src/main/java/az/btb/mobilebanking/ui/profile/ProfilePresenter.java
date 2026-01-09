package az.btb.mobilebanking.ui.profile;

import android.content.SharedPreferences;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.MobileUserDataRequest;
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
public class ProfilePresenter extends MvpPresenter<ProfileView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences preferences;

    @Inject ProfilePresenter(Router router, AuthService authService, SharedPreferences preferences) {
        this.router = router;
        this.authService = authService;
        this.preferences = preferences;
    }

    void getProfileData() {
        getViewState().showLoading(true);

        final int signInType = preferences.getInt(SIGN_IN_TYPE, 0);
        final String username = preferences.getString(USERNAME, "");
        final String password = preferences.getString(PASSWORD_HASH, "");

        MobileUserDataRequest request = new MobileUserDataRequest(
            Utils.getCommonRequest(),
            signInType, username, password
        );

        compositeDisposable.add(
            authService
                .getMobileUserData(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> getViewState().setProfileInfo(response.getMobileUserData()),
                    Throwable::printStackTrace
                    //error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goBack() {
        router.exit();
    }

    void editEmail() {
        router.navigateTo(new MainScreens.ChangeEmailScreen());
    }

    void editPhoneNumber() {
        router.navigateTo(new MainScreens.ChangePhoneNumberScreen());
    }

    void changePassword() {
        router.navigateTo(new MainScreens.ChangePasswordScreen());
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
