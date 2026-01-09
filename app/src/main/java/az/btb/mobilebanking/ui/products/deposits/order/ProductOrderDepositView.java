package az.btb.mobilebanking.ui.products.deposits.order;

import moxy.MvpView;

public interface ProductOrderDepositView extends MvpView {
    void showError(String msg);
    void showOrderResult(int plasticCardOrderStatus);
}
