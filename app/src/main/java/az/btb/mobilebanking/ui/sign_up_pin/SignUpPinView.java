package az.btb.mobilebanking.ui.sign_up_pin;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import moxy.MvpView;

interface SignUpPinView extends MvpView {
    void progressBarState(boolean isLoading);
    void showError(String error);
    void disableButtons(boolean disabled);
    void setAppBankCards(List<BankCard> bankCards);
    void setAppBankAccounts(List<BankAccount> bankAccounts);
}
