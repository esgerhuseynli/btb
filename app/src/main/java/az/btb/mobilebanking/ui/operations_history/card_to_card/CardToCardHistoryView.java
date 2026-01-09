package az.btb.mobilebanking.ui.operations_history.card_to_card;

import java.util.List;

import az.btb.mobilebanking.models.BankCard;
import az.btb.mobilebanking.models.BankCardOperation;
import moxy.MvpView;

public interface CardToCardHistoryView extends MvpView {
    void setOperationsHistory(List<BankCardOperation> bankCardOperations);
    void showError(String responseMessage);
    void setBankCards(List<BankCard> bankCards);
}
