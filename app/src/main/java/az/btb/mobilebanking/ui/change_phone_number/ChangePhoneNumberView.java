package az.btb.mobilebanking.ui.change_phone_number;

import moxy.MvpView;

interface ChangePhoneNumberView extends MvpView {
    void showError(String msg);
    void showLoading(boolean isLoading);
}
