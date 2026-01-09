package az.btb.mobilebanking.ui.sign_up_by_asan_imza.step2;

import moxy.MvpView;

interface SignUpByAsanImzaStep2View extends MvpView {
    void showLoading(boolean check);
    void showError(String msg);
}
