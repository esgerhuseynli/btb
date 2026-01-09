package az.btb.mobilebanking.ui.my_items.my_account_info;

import moxy.MvpView;

interface MyAccountInfoView extends MvpView {
    void showError(String message);
    void showResult(String accountNewAltName, int accountNewColor);
}
