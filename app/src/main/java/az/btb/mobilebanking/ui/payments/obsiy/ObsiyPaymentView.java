package az.btb.mobilebanking.ui.payments.obsiy;

import androidx.annotation.StringRes;

import az.btb.mobilebanking.utils.Constants;
import moxy.MvpView;

public interface ObsiyPaymentView extends MvpView {
    void showQrCodeErrorResult(@Constants.QrCodeValidationResults int qrCodeValidationResultCode);
    void showError(String message);
    void showError(@StringRes int message);
}
