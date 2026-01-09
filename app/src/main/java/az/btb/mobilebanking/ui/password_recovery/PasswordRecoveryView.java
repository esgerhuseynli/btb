package az.btb.mobilebanking.ui.password_recovery;

import moxy.MvpView;

interface PasswordRecoveryView extends MvpView {
    void showError(String responseMessage);

    void showLoading(boolean check);
}
