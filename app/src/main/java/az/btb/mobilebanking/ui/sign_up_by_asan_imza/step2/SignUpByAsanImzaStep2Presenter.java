package az.btb.mobilebanking.ui.sign_up_by_asan_imza.step2;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.AsanImzaSignUpRequest;
import az.btb.mobilebanking.screens.AuthScreens;
import az.btb.mobilebanking.utils.AsanImzaData;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class SignUpByAsanImzaStep2Presenter extends MvpPresenter<SignUpByAsanImzaStep2View> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject SignUpByAsanImzaStep2Presenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void goNext(AsanImzaData data) {
        AsanImzaSignUpRequest request = new AsanImzaSignUpRequest(
            Utils.getCommonRequest(),
            data.citizenType,
            data.citizenType == 1 ? data.pinCodeOrTaxNumber : "",
            data.citizenType == 1 ? "" : data.pinCodeOrTaxNumber
        );
        compositeDisposable.add(
            authService
                .signUpWithAsanImza(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            router.navigateTo(
                                new AuthScreens.SignUpByTypesScreen(
                                    3,
                                    data.fourDigitVerificationCode,
                                    response.getMobileNumber(),
                                    response.getEmail(),
                                    data
                                )
                            );
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
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
}
