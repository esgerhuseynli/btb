package az.btb.mobilebanking.ui.sign_up_by_card;

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
import ru.terrakok.cicerone.Router;

import static az.btb.mobilebanking.utils.Constants.SIGN_UP_TYPE_PAN;

@InjectViewState
public class SignUpByCardPresenter extends MvpPresenter<SignUpByCardView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject SignUpByCardPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void signUp(CardSendRequest cardSendRequest) {
        compositeDisposable.add(
            authService
                .signUp(cardSendRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    cardSendResponse -> {
                        getViewState().showProgressBar(false);
                        getViewState().showLoading(false);

                        if (cardSendResponse.getResponseInfo().getResponseType() != 0)
                            getViewState().showError(cardSendResponse.getResponseInfo().getResponseMessage());
                        else {
                            AppData.getInstance().setSignUpPan(cardSendRequest.getPan());
                            AppData.getInstance().setRequestInfo(cardSendRequest.getRequestInfo());
                            router.navigateTo(new AuthScreens.VerificationScreen(SIGN_UP_TYPE_PAN, cardSendResponse.getMobileNumber(), cardSendResponse.getEmail()));
                        }
                    },
                    error -> {
                        getViewState().showProgressBar(false);
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
