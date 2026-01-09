package az.btb.mobilebanking.ui.change_password;

import moxy.MvpView;

interface ChangePasswordView extends MvpView {
    void showError(String msg);
    void showLoading(boolean isLoading);
}
