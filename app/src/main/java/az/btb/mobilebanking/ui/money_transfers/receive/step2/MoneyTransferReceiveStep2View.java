package az.btb.mobilebanking.ui.money_transfers.receive.step2;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import moxy.MvpView;

public interface MoneyTransferReceiveStep2View extends MvpView {
    void showError(String msg);

    void showCards(List<BankCard> bankCards);

    void showAccounts(List<BankAccount> bankAccounts);
}
