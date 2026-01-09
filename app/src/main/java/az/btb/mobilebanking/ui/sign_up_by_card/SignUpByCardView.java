package az.btb.mobilebanking.ui.sign_up_by_card;

import moxy.MvpView;

interface SignUpByCardView extends MvpView {
    void showLoading(boolean check);
    void showError(String error);

    void showProgressBar(boolean b);
}
