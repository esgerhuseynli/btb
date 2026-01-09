package az.btb.mobilebanking.ui.transfer_submission;

import moxy.MvpView;

interface TransferSubmissionView extends MvpView {
    void showLoading(boolean isLoading);
    void showTransferResult(boolean wasSucceeded);
    void showError(String responseMessage);
}
