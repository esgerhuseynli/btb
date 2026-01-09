package az.btb.mobilebanking.ui.home;

import az.btb.mobilebanking.utils.Constants;
import moxy.MvpView;

interface HomeView extends MvpView {
    void showError(String msg);
    void showQrCodeErrorResult(@Constants.QrCodeValidationResults int qrCodeValidationResultCode);
    void showError(int payment_validation_error);
}
