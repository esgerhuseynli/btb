package az.btb.mobilebanking.ui.sign_in_by_number;

import moxy.MvpView;

interface SignInByNumberView extends MvpView {
    void showLoading(boolean check);
    void showError(String message);
    void killActivity();
    void showSignUpInfo();
}
