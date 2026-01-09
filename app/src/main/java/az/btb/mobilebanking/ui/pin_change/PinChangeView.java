package az.btb.mobilebanking.ui.pin_change;

import moxy.MvpView;

interface PinChangeView extends MvpView {
    void showError(String error);
    void disableButtons(boolean disabled);
}
