package az.btb.mobilebanking.ui.operations_history.local_transfer;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.LocalAccountTransfer;
import moxy.MvpView;

public interface LocalTransfersHistoryView extends MvpView {
    void showError(String msg);
    void showCards(List<BankCard> itemList);
    void showAccounts(List<BankAccount> bankAccounts);
    void showHistory(List<LocalAccountTransfer> itemList);
}
