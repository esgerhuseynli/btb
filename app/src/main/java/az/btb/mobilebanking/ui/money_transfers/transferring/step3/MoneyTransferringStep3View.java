package az.btb.mobilebanking.ui.money_transfers.transferring.step3;

import az.btb.mobilebanking.models.SendTransferInfo;
import moxy.MvpView;

public interface MoneyTransferringStep3View extends MvpView {
    void showError(String responseMessage);
    void showResult(SendTransferInfo sendTransferInfo);
}
