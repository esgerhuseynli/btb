package az.btb.mobilebanking.ui.sign_in_by_email;

import moxy.MvpView;

interface SignInByEmailView extends MvpView {
    void showLoading(boolean check);
    void showError(String message);
    void killActivity();
    void showSignUpInfo();
}
