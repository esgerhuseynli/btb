package az.btb.mobilebanking.ui.payments;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import az.btb.mobilebanking.R;
import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.PaymentValidationRequest;
import az.btb.mobilebanking.models.QrCodeValidationInfo;
import az.btb.mobilebanking.models.QrCodeValidationRequest;
import az.btb.mobilebanking.models.RequestInfoRequest;
import az.btb.mobilebanking.models.ValidatePayment;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.PaymentInfoData;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PaymentsPresenter extends MvpPresenter<PaymentsView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public PaymentsPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void goBack() {
        router.exit();
    }

    void getPaymentProviderGroups() {
        compositeDisposable.add(
            authService
                .getPaymentProviderGroups(new RequestInfoRequest(Utils.getCommonRequest()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0)
                            getViewState().showPaymentProviders(response.getPaymentProviderGroups());
                        else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goToProvidersScreen(final int paymentProviderGroupId, final String paymentProviderGroupName) {
        router.navigateTo(new MainScreens.PaymentProvidersScreen(paymentProviderGroupId, paymentProviderGroupName));
    }
    
    void validateQrCode(final String qrCodeContent) {
        compositeDisposable.add(
            authService
                .validateQrCode(new QrCodeValidationRequest(Utils.getCommonRequest(), qrCodeContent))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            if (response.getQrCodeValidationInfo().getQrCodeValidationResultType() == Constants.QrCodeValidationResults.QR_CODE_SUCCESS)
                                // https://app.clickup.com/t/6hz6dc
                                validateQrCodePaymentRequest(response.getQrCodeValidationInfo(), qrCodeContent);
                            else
                                getViewState().showQrCodeErrorResult(response.getQrCodeValidationInfo().getQrCodeValidationResultType());
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    private void validateQrCodePaymentRequest(@NonNull QrCodeValidationInfo qrCodeValidationInfo, @NonNull String qrCodeValue) {
        PaymentValidationRequest request =
            new PaymentValidationRequest(
                Utils.getCommonRequest(),
                new ValidatePayment(
                    1,
                    0,
                    qrCodeValidationInfo.getIdPaymentProvider(),
                    qrCodeValidationInfo.getPaymentProviderRequestParameters()
                )
            );

        compositeDisposable.add(
            authService
                .validatePayment(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            if (response.getValidatePaymentTemplateStatus() < 2)
                                router.navigateTo(
                                    new MainScreens.PaymentsSourceSelectionScreen(
                                        qrCodeValidationInfo.getPaymentProviderName(),
                                        response.getPaymentCommonInvoiceInfo(),
                                        qrCodeValidationInfo.getIdPaymentProvider(),
                                        true,
                                        qrCodeValue,
                                        new PaymentInfoData()
                                    )
                                );
                            else
                                getViewState().showError(R.string.payment_validation_error);
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
