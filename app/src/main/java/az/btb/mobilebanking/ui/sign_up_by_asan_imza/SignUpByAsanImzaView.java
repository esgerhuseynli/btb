package az.btb.mobilebanking.ui.sign_up_by_asan_imza;

import moxy.MvpView;

interface SignUpByAsanImzaView extends MvpView {
    void showLoading(boolean check);
    void showError(String msg);
    void showError(int asanImzaAuthCode);
}
