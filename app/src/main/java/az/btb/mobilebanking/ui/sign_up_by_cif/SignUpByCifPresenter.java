package az.btb.mobilebanking.ui.sign_up_by_cif;

import javax.inject.Inject;

import az.btb.mobilebanking.AppData;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.CardSendRequest;
import az.btb.mobilebanking.screens.AuthScreens;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.SIGN_UP_TYPE_CIF;

import android.util.Log;

import java.io.IOException;

@InjectViewState
public class SignUpByCifPresenter extends MvpPresenter<SignUpByCifView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    SignUpByCifPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    public void signUp(CardSendRequest request) {
        compositeDisposable.add(authService.signUp(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cardSendResponse -> {
                            getViewState().showLoading(false);
                            if (cardSendResponse.getResponseInfo().getResponseType() != 0)
                                getViewState().showError(cardSendResponse.getResponseInfo().getResponseMessage());
                            else {
                                AppData.getInstance().setSignUpCif(request.getCustomerNumber());
                                AppData.getInstance().setSignUpDateOfBirth(request.getCustomerBirthdate());

                                AppData.getInstance().setRequestInfo(request.getRequestInfo());
                                router.navigateTo(new AuthScreens.VerificationScreen(SIGN_UP_TYPE_CIF, cardSendResponse.getMobileNumber(), cardSendResponse.getEmail()));
                            }
                        },
                        error -> {
                            Log.e("API_ERROR", error.getMessage());
                            getViewState().showLoading(false);
                            getViewState().showError(error.getMessage());
                        }
                )
        );
    }

//    void goToWebView(String url) {
//        router.navigateTo(new AuthScreens.WebViewScreen(url));
//    }

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
