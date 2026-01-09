package az.btb.mobilebanking.ui.sign_up_by_asan_imza;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.CardSendRequest;
import az.btb.mobilebanking.models.RequestInfoRequest;
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
public class SignUpByAsanImzaPresenter extends MvpPresenter<SignUpByAsanImzaView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject SignUpByAsanImzaPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void goNext(String phoneNumberData, String passwordData) {
        CardSendRequest request = new CardSendRequest(
            Utils.getCommonRequest(),
            3, // asan imza
            "", "","",
            phoneNumberData, passwordData
        );
        compositeDisposable.add(
            authService
                .signUp(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            AsanImzaData data = new AsanImzaData();
                            data.mobileNumber = phoneNumberData;
                            data.mobileNumberSecretCode = passwordData;
                            // ONLY in this case, we need this response message string.
                            // It is 4 digit ASAN imza code, which will be used in the future.
                            data.fourDigitVerificationCode = response.getResponseInfo().getResponseMessage();

                            startVerification(data);
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    private void startVerification(AsanImzaData data) {
        compositeDisposable.add(
            authService
                .verifyAsanImzaCode(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            switch (response.getAuthenticateStatusType()) {
                                case 1:
                                    data.certificates = response.getCustomerSigningCertificates();
                                    router.navigateTo(
                                        new AuthScreens.SignUpByAsanImzaStep2Screen(data)
                                    );
                                    break;
                                case 2:
                                    startVerification(data);
                                    break;
                                default:
                                    getViewState().showError(response.getAuthenticateStatusType());
                                    break;
                            }
                        } else
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
