package az.btb.mobilebanking.ui.sign_up_by_email;

import moxy.MvpView;

interface SignUpByEmailView extends MvpView {
    void showLoading(boolean check);
    void showError(String msg);
}
