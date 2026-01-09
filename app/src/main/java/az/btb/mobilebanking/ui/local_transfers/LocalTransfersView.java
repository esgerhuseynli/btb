package az.btb.mobilebanking.ui.local_transfers;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import moxy.MvpView;

public interface LocalTransfersView extends MvpView {
    void showError(String msg);
    void showSuccessResult(String transferNumber);
    void showCards(List<BankCard> bankCards);
    void showAccounts(List<BankAccount> bankAccounts);
}
