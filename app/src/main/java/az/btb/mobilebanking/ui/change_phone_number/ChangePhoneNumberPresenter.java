package az.btb.mobilebanking.ui.change_phone_number;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.ChangeMobileUserDataRequest;
import az.btb.mobilebanking.models.NewMobileUserData;
import az.btb.mobilebanking.models.RequestInfo;
import az.btb.mobilebanking.screens.MainScreens;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.PROFILE_UPDATE_TYPE_MOBILE_NUMBER;

@InjectViewState
public class ChangePhoneNumberPresenter extends MvpPresenter<ChangePhoneNumberView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    ChangePhoneNumberPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void updateProfileData(@NonNull String newPhoneNumber) {
        RequestInfo requestInfo = AppData.getInstance().getRequestInfo();
        requestInfo.getMobileUser().setSaltSignature(AppData.getInstance().getSessionKey());

        NewMobileUserData newMobileUserData = new NewMobileUserData();
        newMobileUserData.setChangeMobileUserDataMode(PROFILE_UPDATE_TYPE_MOBILE_NUMBER);
        newMobileUserData.setMobileNumber(newPhoneNumber.replace(" ", ""));

        ChangeMobileUserDataRequest request = new ChangeMobileUserDataRequest(
            requestInfo,
            newMobileUserData
        );

        compositeDisposable.add(
            authService
                .changeMobileUserData(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        getViewState().showLoading(false);
                        if (response.getResponseInfo().getResponseType() == 0)
                            router.navigateTo(new MainScreens.ProfileUpdateVerificationScreen(newMobileUserData));
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> {
                        getViewState().showLoading(false);
                        getViewState().showError(error.getMessage());
                    }
                )
        );
    }

    void goBack() {
        router.exit();
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
