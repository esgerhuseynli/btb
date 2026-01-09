package az.btb.mobilebanking;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import moxy.MvpView;

public interface MainView extends MvpView {
	void setAppBankCards(List<BankCard> bankCards);
	void setAppBankAccounts(List<BankAccount> bankAccounts);
}
