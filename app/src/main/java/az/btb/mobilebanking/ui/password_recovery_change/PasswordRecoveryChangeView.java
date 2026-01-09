package az.btb.mobilebanking.ui.password_recovery_change;

import moxy.MvpView;

interface PasswordRecoveryChangeView extends MvpView {

    void showPasswordError(String error);
    void showCodeError(String error);
    void showLoading(boolean check);
}
