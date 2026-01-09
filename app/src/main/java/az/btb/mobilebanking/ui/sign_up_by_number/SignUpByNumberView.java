package az.btb.mobilebanking.ui.sign_up_by_number;

import moxy.MvpView;

interface SignUpByNumberView extends MvpView {
    void showLoading(boolean check);
    void showError(String error);
}
