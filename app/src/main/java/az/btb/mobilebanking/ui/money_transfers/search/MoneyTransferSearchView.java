package az.btb.mobilebanking.ui.money_transfers.search;

import az.btb.mobilebanking.models.TransferStatusInfo;
import moxy.MvpView;

public interface MoneyTransferSearchView extends MvpView {
    void showError(String msg);
    void showMoneyTransferResult(TransferStatusInfo object);
}
