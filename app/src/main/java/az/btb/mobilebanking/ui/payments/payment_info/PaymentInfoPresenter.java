package az.btb.mobilebanking.ui.payments.payment_info;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

import javax.inject.Inject;

import az.btb.mobilebanking.api.AuthService;
import az.btb.mobilebanking.models.PaymentCommonInvoiceInfo;
import az.btb.mobilebanking.models.PaymentInfo;
import az.btb.mobilebanking.models.PaymentOperationPayerInfo;
import az.btb.mobilebanking.models.PaymentSubmissionRequest;
import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Constants;
import az.btb.mobilebanking.utils.Constants.PaymentDataFillingMethod;
import az.btb.mobilebanking.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PaymentInfoPresenter extends MvpPresenter<PaymentInfoView> {

    private final Router router;
    private final AuthService authService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public PaymentInfoPresenter(Router router, AuthService authService) {
        this.router = router;
        this.authService = authService;
    }

    void doPayment(
        @NonNull PaymentCommonInvoiceInfo paymentCommonInvoiceInfo, int providerId,
        @Constants.MoneySourceTypes int sourceType, String fromIdCard, String fromIbanAccount,
        BigDecimal amount, boolean isQrCodeScanned, String qrCodeValue, boolean isIpotekaPayment
    ) {
        if (!isIpotekaPayment && paymentCommonInvoiceInfo.getInvoices().size() == 1)
            paymentCommonInvoiceInfo.getInvoices().get(0).setInvoiceAmount(amount);

        PaymentSubmissionRequest request =
            new PaymentSubmissionRequest(
                Utils.getCommonRequest(),
                new PaymentInfo(
                    2,
                    0,
                    providerId,
                    amount,
                    paymentCommonInvoiceInfo.getInvoices(),
                    new PaymentOperationPayerInfo(
                        sourceType,
                        fromIdCard,
                        fromIbanAccount,
                        "",
                        ""
                    ),
                    isQrCodeScanned ? PaymentDataFillingMethod.FROM_QR_CODE : PaymentDataFillingMethod.MANUAL,
                    qrCodeValue
                )
            );

        compositeDisposable.add(
            authService
                .submitPayment(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        if (response.getResponseInfo().getResponseType() == 0) {
                            getViewState().showPaymentResult(
                                response.getPaidInvoices().get(0).getPaidInvoiceStatus(),
                                response.getPaidInvoices().get(0).getPaidInvoiceOperationDateTime(),
                                response.getPaidInvoices().get(0).getPaidInvoicePaymentAmount()
                            );
                        } else
                            getViewState().showError(response.getResponseInfo().getResponseMessage());
                    },
                    error -> getViewState().showError(error.getMessage())
                )
        );
    }

    void goBack() {
        router.exit();
    }

    void goHome() {
        router.backTo(new MainScreens.HomeNavScreen());
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
