package az.btb.mobilebanking.ui.change_email;

import moxy.MvpView;

interface ChangeEmailView extends MvpView {
    void showError(String msg);
    void showLoading(boolean isLoading);
}
