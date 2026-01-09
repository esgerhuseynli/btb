package az.btb.mobilebanking.ui.products.plastic_cards.order;

import androidx.annotation.NonNull;

import java.util.List;

import az.btb.mobilebanking.models.BankAccount;
import az.btb.mobilebanking.models.BankCard;
import moxy.MvpView;

public interface ProductOrderPlasticCardView extends MvpView {
    void showError(@NonNull String msg);
    void showCards(List<BankCard> bankCards);
    void showAccounts(List<BankAccount> bankAccounts);
    void showOrderResult(int plasticCardOrderStatus);
}
