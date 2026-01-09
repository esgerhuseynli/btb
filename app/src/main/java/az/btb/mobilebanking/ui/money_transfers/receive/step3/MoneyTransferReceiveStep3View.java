package az.btb.mobilebanking.ui.money_transfers.receive.step3;

import az.btb.mobilebanking.models.MoneyTransferReceiverInfo;
import moxy.MvpView;

public interface MoneyTransferReceiveStep3View extends MvpView {
    void showError(String msg);
    void showResult(MoneyTransferReceiverInfo transferReceiverInfo);
}
