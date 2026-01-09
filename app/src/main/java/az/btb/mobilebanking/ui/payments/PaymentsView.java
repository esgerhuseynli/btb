package az.btb.mobilebanking.ui.payments;

import java.util.List;

import az.btb.mobilebanking.models.PaymentProviderGroup;
import az.btb.mobilebanking.utils.Constants;
import moxy.MvpView;

public interface PaymentsView extends MvpView {
    void showError(String msg);
    void showPaymentProviders(List<PaymentProviderGroup> providerGroups);
    void showQrCodeErrorResult(@Constants.QrCodeValidationResults int qrCodeValidationResultCode);
    void showError(int msg);
}
