package az.btb.mobilebanking.ui.sign_in_pin_fingerprint;

import androidx.annotation.Nullable;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import moxy.MvpView;

interface SignInPinFingerprintView extends MvpView {
    void showError(@Nullable String msg);
    void showLoading(boolean isLoading);
	void clearAccountData();
	void setAppBankCards(List<BankCard> bankCards);
	void setAppBankAccounts(List<BankAccount> bankAccounts);
}
