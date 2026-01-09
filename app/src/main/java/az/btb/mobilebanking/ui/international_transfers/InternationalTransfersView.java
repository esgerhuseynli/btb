package az.btb.mobilebanking.ui.international_transfers;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import moxy.MvpView;

public interface InternationalTransfersView extends MvpView {
    void showError(String msg);
    void showSuccessResult(String transferNumber);
    void showCards(List<BankCard> itemList);
    void showAccounts(List<BankAccount> bankAccounts);
}
