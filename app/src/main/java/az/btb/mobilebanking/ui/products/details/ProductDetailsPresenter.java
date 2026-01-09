package az.btb.mobilebanking.ui.products.details;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import az.btb.mobilebanking.screens.MainScreens;
import az.btb.mobilebanking.utils.Product;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ProductDetailsPresenter extends MvpPresenter<MvpView> {

    private final Router router;

    @Inject
    public ProductDetailsPresenter(final Router router) {
        this.router = router;
    }

    void goBack() { router.exit(); }

    void goToOrderPlasticCardScreen(int productId, String productHeaderName) {
        router.navigateTo(new MainScreens.ProductOrderPlasticCardScreen(productId, productHeaderName));
    }

    void goToOrderLoanScreen(int productId, String productHeaderName, @NonNull Product.OrderData orderData) {
        router.navigateTo(new MainScreens.ProductOrderLoanScreen(productId, productHeaderName, orderData));
    }

    void goToOrderDepositScreen(int productId, String productHeaderName, @NonNull Product.OrderData orderData) {
        router.navigateTo(new MainScreens.ProductOrderDepositScreen(productId, productHeaderName, orderData));
    }

    void goToOrderEmbassyReferenceScreen(int productId, String productHeaderName) {
        router.navigateTo(new MainScreens.ProductOrderEmbassyReferenceScreen(productId, productHeaderName));
    }

    void goToOrderFinancialReferenceScreen(int productId, String productHeaderName) {
        router.navigateTo(new MainScreens.ProductOrderFinancialReferenceScreen(productId, productHeaderName));
    }
}
