package az.btb.mobilebanking.ui.money_transfers.transferring.step2;

import java.math.BigDecimal;

import moxy.MvpView;

public interface MoneyTransferringStep2View extends MvpView {
    void showError(String responseMessage);
    void setMinAmount(BigDecimal minAmount);
    void setMaxAmount(BigDecimal maxAmount);
}
