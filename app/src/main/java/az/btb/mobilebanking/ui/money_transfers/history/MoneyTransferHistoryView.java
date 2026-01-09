package az.btb.mobilebanking.ui.money_transfers.history;

import az.btb.mobilebanking.models.TransferStatusInfo;
import moxy.MvpView;

public interface MoneyTransferHistoryView extends MvpView {
    void showError(String msg);
    void showMoneyTransferResult(TransferStatusInfo object);
}
