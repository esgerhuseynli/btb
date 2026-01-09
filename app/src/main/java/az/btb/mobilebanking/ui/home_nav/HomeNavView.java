package az.btb.mobilebanking.ui.home_nav;

import moxy.MvpView;

public interface HomeNavView extends MvpView {
    void showCustomerName(String cif);
    void showError(String msg);
	void clearAccountData();
	void showLoading(boolean shouldShow);
}
