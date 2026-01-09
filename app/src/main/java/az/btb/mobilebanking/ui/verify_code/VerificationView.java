package az.btb.mobilebanking.ui.verify_code;

import androidx.annotation.StringRes;

import moxy.MvpView;

interface VerificationView extends MvpView {
    void showLoading(boolean check);
    void showError(String error);
    void showError(@StringRes int error);
    void clearCode();
}
