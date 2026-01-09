package az.btb.mobilebanking.ui.products.loans.order;

import androidx.annotation.NonNull;

import moxy.MvpView;

public interface ProductOrderLoanView extends MvpView {
    void showError(@NonNull String msg);
    void showOrderResult(int plasticCardOrderStatus);
}
