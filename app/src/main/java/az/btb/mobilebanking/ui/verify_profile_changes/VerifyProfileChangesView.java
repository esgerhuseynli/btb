package az.btb.mobilebanking.ui.verify_profile_changes;

import moxy.MvpView;

interface VerifyProfileChangesView extends MvpView {
    void showLoading(boolean check);
    void showError(String error);
    void clearCode();
    void showSuccessDialog(int type, int signInUpType);
    void clearAccountData();
}
