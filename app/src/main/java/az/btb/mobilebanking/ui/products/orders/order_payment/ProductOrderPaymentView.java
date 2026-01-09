package az.btb.mobilebanking.ui.products.orders.order_payment;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import moxy.MvpView;

public interface ProductOrderPaymentView extends MvpView {
    void showError(String msg);
    void showCards(List<BankCard> bankCards);
    void showAccounts(List<BankAccount> bankAccounts);
    void showOrderResult(int plasticCardOrderStatus);
}
