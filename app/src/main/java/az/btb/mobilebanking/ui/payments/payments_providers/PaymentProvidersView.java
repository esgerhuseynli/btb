package az.btb.mobilebanking.ui.payments.payments_providers;

import java.util.List;

import az.btb.mobilebanking.models.PaymentProvider;
import moxy.MvpView;

public interface PaymentProvidersView extends MvpView {
    void showError(String msg);
    void showPaymentProviders(List<PaymentProvider> providerGroups);
}
