package az.btb.mobilebanking.ui.products.orders.order_details;

import javax.inject.Inject;

import az.btb.mobilebanking.models.ProductOrder;
import az.btb.mobilebanking.screens.MainScreens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import moxy.MvpView;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ProductOrderDetailsPresenter extends MvpPresenter<MvpView> {

    private final Router router;

    @Inject ProductOrderDetailsPresenter(final Router router) {
        this.router = router;
    }

    void goBack() {
        router.exit();
    }

    void goToPayment(ProductOrder order) {
        router.navigateTo(new MainScreens.ProductOrderPaymentScreen(order));
    }
}
