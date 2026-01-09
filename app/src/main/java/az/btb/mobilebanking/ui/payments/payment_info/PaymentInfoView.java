package az.btb.mobilebanking.ui.payments.payment_info;

import java.math.BigDecimal;

import moxy.MvpView;

public interface PaymentInfoView extends MvpView {
    void showError(String responseMessage);
    void showPaymentResult(int paidInvoiceStatus, String paidInvoiceOperationDateTime, BigDecimal paidInvoicePaymentAmount);
}
