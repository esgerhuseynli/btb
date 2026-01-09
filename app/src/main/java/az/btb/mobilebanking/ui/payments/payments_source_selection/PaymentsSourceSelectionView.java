package az.btb.mobilebanking.ui.payments.payments_source_selection;

import java.math.BigDecimal;
import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import moxy.MvpView;

public interface PaymentsSourceSelectionView extends MvpView {
    void showError(String msg);
    void showCards(List<BankCard> bankCards);
    void showAccounts(List<BankAccount> bankAccounts);
    void showPaymentResult(int paidInvoiceStatus, String paidInvoiceOperationDateTime, BigDecimal paidInvoicePaymentAmount);
}
