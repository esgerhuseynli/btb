package az.btb.mobilebanking.ui.operations_history.payment;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.MobilePayment;
import az.btb.mobilebanking.utils.PaymentHistoryProviderItem;
import moxy.MvpView;

public interface PaymentHistoryView extends MvpView {
    void showError(String msg);
    void showCards(List<BankCard> itemList);
    void showAccounts(List<BankAccount> bankAccounts);
    void showPaymentProviderGroups(List<PaymentHistoryProviderItem> groups);
    void showPaymentProviders(List<PaymentHistoryProviderItem> providers);
    void showHistory(List<MobilePayment> itemList);
}
