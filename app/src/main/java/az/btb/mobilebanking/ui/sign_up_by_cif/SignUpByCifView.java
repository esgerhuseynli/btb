package az.btb.mobilebanking.ui.sign_up_by_cif;

import moxy.MvpView;

interface SignUpByCifView extends MvpView {
    void showLoading(boolean check);
    void showError(String error);
}
