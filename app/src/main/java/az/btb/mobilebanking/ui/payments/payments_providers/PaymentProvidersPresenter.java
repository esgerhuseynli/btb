package az.btb.mobilebanking.ui.payments.payments_providers;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.PaymentProvidersRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PaymentProvidersPresenter extends MvpPresenter<PaymentProvidersView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public PaymentProvidersPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void getPaymentProviders(final int paymentProviderGroupId) {
        compositeDisposable.add(
            authService
                .getPaymentProviders(new PaymentProvidersRequest(Utils.getCommonRequest(), paymentProviderGroupId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showPaymentProviders(response.getPaymentProviders());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goToObsiyPaymentsScreen(int providerGroupId, int providerId, String paymentProviderName) {
        router.navigateTo(new MainScreens.ObsiyPaymentsScreen(providerGroupId, providerId, paymentProviderName));
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
