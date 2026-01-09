package az.btb.mobilebanking.ui.my_items.my_card_info;

import moxy.MvpView;

interface MyCardInfoView extends MvpView {
    void showError(String message);
    void showResult(String cardNewAltName, int cardNewColor);
}
